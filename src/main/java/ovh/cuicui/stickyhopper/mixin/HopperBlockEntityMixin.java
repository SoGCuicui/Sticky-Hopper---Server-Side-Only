package ovh.cuicui.stickyhopper.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.cuicui.stickyhopper.Main;
import ovh.cuicui.stickyhopper.StickyHopperBlockEntity;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity {
    public HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) { super(blockEntityType, blockPos, blockState); }

    @Shadow
    private static native boolean insert(World world, BlockPos pos, BlockState state, Inventory inventory);

    // A Sticky Hopper can insert an item to the next inventory only if there is more than one item in a slot
    // ≥ 1.17: We cannot access "isEmpty" directly as "insert" is now static, so we cannot use "this.getType" anymore
    // ≥ v3.0: We allow to insert a non-stackable item from the center slot (#2) if needed (this method is then called a second time from sh_transfer_head below)
    @Redirect(method = "insert", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;getStack(I)Lnet/minecraft/item/ItemStack;", ordinal = 0))
    private static ItemStack sh_insert_getStack(Inventory inventory, int slot) {
        ItemStack itemStack = inventory.getStack(slot);
        if (!(inventory instanceof StickyHopperBlockEntity)
         || (((StickyHopperBlockEntity)inventory).needsNonStackableInsert && slot == 2)
         || itemStack.getCount() > 1) {
            return (itemStack);
        }
        return (ItemStack.EMPTY);
    }

    // A Hopper (Sticky or not) can extract an item from a slot of Sticky Hopper above only if there is more than one item
    // "hopper" is the extracting one, "inventory" is the inventory above it
    @Inject(method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private static void sh_extract_head(Hopper hopper, Inventory inventory, int slot, Direction side, CallbackInfoReturnable<Boolean> info) {
        if (inventory instanceof StickyHopperBlockEntity && inventory.getStack(slot).getCount() <= 1) {
            info.setReturnValue(false);
        }
    }

    // ≥ v3.0: If the "recipe" is set, we try to transfer filtered non-stackable item
    @Inject(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void sh_transfer_head(@Nullable Inventory from, Inventory to, ItemStack stack, @Nullable Direction direction, CallbackInfoReturnable<ItemStack> info) {
        // This "recipe" requires only one item on each slot, so we can use the overridden to.isEmpty() method in this test
        if (to instanceof StickyHopperBlockEntity && Main.config.nonstackable_filter
         && to.getStack(0).isOf(Items.TRIPWIRE_HOOK) && to.getStack(1).isOf(Items.STRING)
         && to.getStack(4).isOf(Items.TRIPWIRE_HOOK) && to.getStack(3).isOf(Items.STRING)
         && to.getStack(2).getMaxCount() == 1 && to.isEmpty()) {
            // If the incoming item is stackable, we must ignore it so new Tripwire Hooks and Strings won't be merged
            // Of course, if it's different from the item in the center, we ignore it as well
            if (stack.getMaxCount() > 1 || !stack.isOf(to.getStack(2).getItem())) {
                info.setReturnValue(stack); // Item rejected, transfer canceled
                return;
            }

            ((StickyHopperBlockEntity)to).needsNonStackableInsert = true;
            boolean nonStackableInserted = insert(((StickyHopperBlockEntity) to).getWorld(), ((StickyHopperBlockEntity) to).getPos(), ((StickyHopperBlockEntity) to).getCachedState(), to);
            ((StickyHopperBlockEntity)to).needsNonStackableInsert = false;

            // Last but not least, if the non-stackable item can't be inserted to another inventory we cancel the transfer, otherwise the item would get lost
            if (!nonStackableInserted) {
                info.setReturnValue(stack); // Item rejected, transfer canceled
                return;
            }
            to.setStack(2, stack);
            info.setReturnValue(ItemStack.EMPTY); // Item accepted, transfer done
        }
    }
}

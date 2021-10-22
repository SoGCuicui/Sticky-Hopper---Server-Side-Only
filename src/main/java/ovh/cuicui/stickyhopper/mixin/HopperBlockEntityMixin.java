package ovh.cuicui.stickyhopper.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
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
    // ≥ v3.0: We allow to insert a non-stackable item from the slot defined by Main.config.nsif_observed_slot if needed (this method is then called a second time from sh_transfer_head below)
    @Redirect(method = "insert", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;getStack(I)Lnet/minecraft/item/ItemStack;", ordinal = 0))
    private static ItemStack sh_insert_getStack(Inventory inventory, int slot) {
        ItemStack itemStack = inventory.getStack(slot);
        if (!(inventory instanceof StickyHopperBlockEntity)
         || (((StickyHopperBlockEntity)inventory).needsNonStackableInsert && slot == Main.config.nsif_observed_slot)
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
        // Also, the sticky hopper must not to be blocked, otherwise it would normally accept an inserted item (from another hopper) and so we would be forced to accept the transfer
        if (to instanceof StickyHopperBlockEntity && Main.config.nsif_enabled && to.isEmpty() && ((StickyHopperBlockEntity) to).getCachedState().get(HopperBlock.ENABLED)) {
            int filterSlot = Main.config.nsif_observed_slot;
            for (int index = 0; index < to.size(); ++index) {
                ItemStack itemStack = to.getStack(index);
                if ((index != filterSlot && !itemStack.isOf(Registry.ITEM.get(new Identifier(Main.config.nsif_recipe_slots[index]))))
                 || (index == filterSlot && itemStack.getMaxCount() > 1)) {
                    return; // Wrong recipe, continue normal execution
                }
            }

            ItemStack itemStack = to.getStack(filterSlot);

            if (stack.getMaxCount() > 1 || !stack.isOf(itemStack.getItem())
             || (!Main.config.nsif_allow_nbts_filters && !ItemStack.areNbtEqual(stack, itemStack))
             || (!Main.config.nsif_ignore_durability && stack.getDamage() != itemStack.getDamage())
             || (!Main.config.nsif_ignore_enchantments && !stack.getEnchantments().equals(itemStack.getEnchantments()))
             || (!Main.config.nsif_ignore_name && !stack.getName().equals(itemStack.getName()))
             || (!Main.config.nsif_ignore_potions && !stack.getNbt().getString("Potion").equals(itemStack.getNbt().getString("Potion")))) {
                info.setReturnValue(stack); // Incoming item rejected, transfer canceled
                return;
            }

            ((StickyHopperBlockEntity)to).needsNonStackableInsert = true;
            boolean nonStackableInserted = insert(((StickyHopperBlockEntity) to).getWorld(), ((StickyHopperBlockEntity) to).getPos(), ((StickyHopperBlockEntity) to).getCachedState(), to);
            ((StickyHopperBlockEntity)to).needsNonStackableInsert = false;

            // Last but not least, if the non-stackable item can't be inserted to another inventory we cancel the transfer, otherwise the item would get lost
            if (!nonStackableInserted) {
                info.setReturnValue(stack); // Transfer canceled
                return;
            }
            to.setStack(filterSlot, stack);
            info.setReturnValue(ItemStack.EMPTY); // Incoming item accepted, transfer done
        }
    }
}

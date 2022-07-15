package ovh.cuicui.stickyhopper.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.cuicui.stickyhopper.StickyHopperBlockEntity;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity {
    public HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) { super(blockEntityType, blockPos, blockState); }

    // A Sticky Hopper can insert an item to the next inventory only if there is more than one item in a slot
    // ≥ v3.0: We allow to insert a non-stackable item from the slot defined by Main.config.nsif_observed_slot if needed (this method is then called a second time from sh_transfer_head below)
    @Redirect(method = "insert", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;getStack(I)Lnet/minecraft/item/ItemStack;", ordinal = 0))
    private static ItemStack sh_insert_getStack(Inventory inventory, int slot) {
        ItemStack itemStack = inventory.getStack(slot);
        if (!(inventory instanceof StickyHopperBlockEntity) || itemStack.getCount() > 1) {
            return (itemStack); // Inserts this item
        }
        return (ItemStack.EMPTY); // Ignores it
    }

    // A Hopper (Sticky or not) can extract an item from a slot of Sticky Hopper above only if there is more than one item
    // "hopper" is the extracting one, "inventory" is the inventory above it
    @Inject(method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private static void sh_extract_head(Hopper hopper, Inventory inventory, int slot, Direction side, CallbackInfoReturnable<Boolean> info) {
        if (inventory instanceof StickyHopperBlockEntity && inventory.getStack(slot).getCount() <= 1) {
            info.setReturnValue(false);
        }
    }
}

package ovh.cuicui.stickyhopper.mixin;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.cuicui.stickyhopper.Main;
import ovh.cuicui.stickyhopper.StickyHopperBlockEntity;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity {
    public HopperBlockEntityMixin(BlockEntityType<?> type) { super(type); }

    // A Hopper (Sticky or not) can extract an item from a slot of Sticky Hopper above only if there is more than one items
    // "hopper" is the extracting one, "inventory" is the inventory above it
    @Inject(method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private static void sh_extract_head(Hopper hopper, Inventory inventory, int slot, Direction side, CallbackInfoReturnable<Boolean> info) {
        if (inventory instanceof StickyHopperBlockEntity && inventory.getStack(slot).getCount() <= 1) {
            info.setReturnValue(false);
            info.cancel();
        }
    }

    // A Sticky Hopper can insert an item to the next inventory only if there is more than one items in a slot
    @Redirect(method = "insert", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
    private boolean sh_insert_isEmpty(ItemStack itemStack) {
        return (itemStack.isEmpty() || (this.getType() == Main.STICKY_HOPPER_BLOCK_ENTITY && itemStack.getCount() == 1));
    }

    // Destination Sticky Hopper must be considered as empty even if there is still one item in each slots, so its cooldown is properly reset
    @Redirect(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;ILnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;isEmpty()Z", ordinal = 0))
    private static boolean sh_transfer_isEmpty(Inventory inventory) {
        if (inventory instanceof StickyHopperBlockEntity) {
            for (int i = 0; i < inventory.size(); ++i) {
                ItemStack itemStack = inventory.getStack(i);
                if (itemStack.getCount() > 1) {
                    return (false);
                }
            }
            return (true);
        }
        return (inventory.isEmpty());
    }
}

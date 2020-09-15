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

    @Inject(method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private static void sh_keepsLastItem(Hopper hopper, Inventory inventory, int slot, Direction side, CallbackInfoReturnable<Boolean> info) {
        ItemStack itemStack = inventory.getStack(slot);
        if (itemStack.getCount() == 1 && inventory instanceof StickyHopperBlockEntity) {
            info.setReturnValue(false);
            info.cancel();
        }
    }

    // if (!this.getStack(i).isEmpty()) {...} (the test is inverted!)
    @Redirect(method = "insert", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z", ordinal = 0))
    private boolean sh_insertIfNot(ItemStack stack) {
        return (stack.isEmpty() || (stack.getCount() == 1 && this.getType() == Main.STICKY_HOPPER_BLOCK_ENTITY));
    }
}

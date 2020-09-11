package ovh.cuicui.stickyhopper.mixin;

import jdk.internal.jline.internal.Nullable;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.*;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.cuicui.stickyhopper.Main;
import ovh.cuicui.stickyhopper.StickyHopperBlockEntity;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity {
    public HopperBlockEntityMixin(BlockEntityType<?> type) { super(type); }

    @Inject(at = @At("HEAD"), method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z", cancellable = true)
    private static void stickyhopperExtract(Hopper hopper, Inventory inventory, int slot, Direction side, CallbackInfoReturnable<Boolean> info) {
        ItemStack itemStack = inventory.getStack(slot);
        if (hopper instanceof StickyHopperBlockEntity && itemStack.getCount() <= 1) {
            info.setReturnValue(false);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "insert", cancellable = true)
    private void stickyhopperInsert(CallbackInfoReturnable<Boolean> info) {
        Inventory inventory = this.getOutputInventory();
        if (inventory == null) {
            info.setReturnValue(false);
            info.cancel();
            return;
        }

        Direction direction = ((Direction)this.getCachedState().get(HopperBlock.FACING)).getOpposite();
        if (this.isInventoryFull(inventory, direction)) {
            info.setReturnValue(false);
            info.cancel();
            return;
        }

        for(int i = 0; i < this.size(); ++i) {
            ItemStack stack = this.getStack(i);
            if (stack.getCount() > 1 || (!stack.isEmpty() && this.getType() != Main.STICKY_HOPPER_BLOCK_ENTITY)) {
                ItemStack itemStack = stack.copy();
                ItemStack itemStack2 = HopperBlockEntity.transfer(this, inventory, this.removeStack(i, 1), direction);
                if (itemStack2.isEmpty()) {
                    inventory.markDirty();
                    info.setReturnValue(true);
                    info.cancel();
                    return;
                }

                this.setStack(i, itemStack);
            }
        }

        info.setReturnValue(false);
        info.cancel();
    }

    @Shadow @Nullable protected abstract Inventory getOutputInventory();

    @Shadow protected abstract boolean isInventoryFull(Inventory inv, Direction direction);
}

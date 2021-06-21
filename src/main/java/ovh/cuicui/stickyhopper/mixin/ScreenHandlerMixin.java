package ovh.cuicui.stickyhopper.mixin;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import ovh.cuicui.stickyhopper.StickyHopperBlockEntity;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    public ScreenHandlerMixin() { super(); }

    // Comparators must consider a Sticky Hopper empty if it only contains zero or a single stackable item per slots (and zero not stackable items)
    @Redirect(method = "calculateComparatorOutput(Lnet/minecraft/inventory/Inventory;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;getStack(I)Lnet/minecraft/item/ItemStack;", ordinal = 0))
    private static ItemStack sh_calculateComparatorOutput_getStack(Inventory inventory, int slot) {
        ItemStack itemStack = inventory.getStack(slot);
        return ((inventory instanceof StickyHopperBlockEntity && itemStack.getCount() <= 1) ? ItemStack.EMPTY : itemStack);
    }
}

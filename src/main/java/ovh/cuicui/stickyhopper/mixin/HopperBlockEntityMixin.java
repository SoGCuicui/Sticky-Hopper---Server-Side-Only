package ovh.cuicui.stickyhopper.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.cuicui.stickyhopper.HopperBlockEntityMixinAccessor;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity implements HopperBlockEntityMixinAccessor {
    public HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) { super(blockEntityType, blockPos, blockState); }

    private boolean isSticky;

    // For Comparators and Items
    public boolean isSticky() { return this.isSticky; }

    // A Sticky Hopper is considered empty even if there is still one item in each slot
    @Override
    public boolean isEmpty() {
        if (this.isSticky) {
            this.checkLootInteraction(null); // Needs to be done first
            for (int i = 0; i < this.size(); ++i) {
                if (this.getStack(i).getCount() > 1) {
                    return (false);
                }
            }
            return (true);
        }
        return (super.isEmpty());
    }

    // If the NBT does not exist, it will be set to false by default
    @Inject(method = "readNbt", at = @At("TAIL"))
    public void sh_readNbt_tail(NbtCompound nbt, CallbackInfo info) { this.isSticky = nbt.getBoolean("sticky"); }

    // If the Hopper is not sticky, we don't write the NBT, so it is like any new, "virgin" Hoppers
    @Inject(method = "writeNbt", at = @At("TAIL"))
    protected void sh_writeNbt_tail(NbtCompound nbt, CallbackInfo info) {
        if (this.isSticky) {
            nbt.putBoolean("sticky", true);
        }
    }

    // A Sticky Hopper can insert an item to the next inventory only if there is more than one item in a slot
    // ≥ v3.0: We allow to insert a non-stackable item from the slot defined by Main.config.nsif_observed_slot if needed (this method is then called a second time from sh_transfer_head below)
    @Redirect(method = "insert", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;getStack(I)Lnet/minecraft/item/ItemStack;", ordinal = 0))
    private static ItemStack sh_insert_getStack(Inventory inventory, int slot) {
        ItemStack itemStack = inventory.getStack(slot);
        if (!((HopperBlockEntityMixin) inventory).isSticky || itemStack.getCount() > 1) {
            return (itemStack); // Inserts this item
        }
        return (ItemStack.EMPTY); // Ignores it
    }

    // A Hopper (Sticky or not) can extract an item from a slot of Sticky Hopper above only if there is more than one item
    // "hopper" is the extracting one, "inventory" is the inventory above it
    @Inject(method = "extract(Lnet/minecraft/block/entity/Hopper;Lnet/minecraft/inventory/Inventory;ILnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"), cancellable = true)
    private static void sh_extract_head(Hopper hopper, Inventory inventory, int slot, Direction side, CallbackInfoReturnable<Boolean> info) {
        if (inventory instanceof HopperBlockEntity && ((HopperBlockEntityMixin) inventory).isSticky && inventory.getStack(slot).getCount() <= 1) {
            info.setReturnValue(false);
        }
    }

    // We can change a Hopper by dropping a single Honey Bottle or a single Snow Ball
    @Inject(method = "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z", at = @At("HEAD"), cancellable = true)
    private static void sh_extract_head(Inventory inventory, ItemEntity itemEntity, CallbackInfoReturnable<Boolean> info) {
        if (itemEntity.getStack().getCount() == 1) {
            if (((HopperBlockEntityMixin) inventory).isSticky) {
                if (itemEntity.getStack().isOf(Items.SNOWBALL)) {
                    ((HopperBlockEntityMixin) inventory).isSticky = false;
                    ((HopperBlockEntityMixin) inventory).setCustomName(null);

                    itemEntity.discard();
                    info.setReturnValue(true);
                }
            } else {
                if (itemEntity.getStack().isOf(Items.HONEY_BOTTLE)) {
                    ((HopperBlockEntityMixin) inventory).isSticky = true;
                    MutableText name = ((MutableText) Text.of("Sticky Hopper"));
                    name.fillStyle(name.getStyle().withItalic(false));
                    ((HopperBlockEntityMixin) inventory).setCustomName(name);

                    itemEntity.setStack(new ItemStack(Items.GLASS_BOTTLE));
                    info.setReturnValue(true);
                }
            }
        }
    }
}

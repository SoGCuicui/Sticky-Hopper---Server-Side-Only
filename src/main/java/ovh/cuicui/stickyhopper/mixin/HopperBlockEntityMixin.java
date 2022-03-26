package ovh.cuicui.stickyhopper.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Hopper;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.cuicui.stickyhopper.HopperBlockEntityMixinAccessor;
import ovh.cuicui.stickyhopper.Main;

@Mixin(HopperBlockEntity.class)
public abstract class HopperBlockEntityMixin extends LootableContainerBlockEntity implements HopperBlockEntityMixinAccessor {
    public HopperBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) { super(blockEntityType, blockPos, blockState); }

    private boolean isSticky;
    private boolean needsNonStackableInsert = false;

    // For Comparators and Items
    public boolean isSticky() {
        return this.isSticky;
    }

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

    @Override
    public Text getContainerName() { return this.isSticky ? Text.of("Sticky Hopper") : new TranslatableText("container.hopper"); }

    @Shadow
    private static native boolean insert(World world, BlockPos pos, BlockState state, Inventory inventory);

    // If the NBT does not exist, it will be set to false by default
    @Inject(method = "readNbt", at = @At("TAIL"))
    public void sh_readNbt_tail(NbtCompound nbt, CallbackInfo info) {
        this.isSticky = nbt.getBoolean("IsSticky");
    }

    // If the Hopper is not sticky, we don't write the NBT, so it is like any new, "virgin" Hoppers
    @Inject(method = "writeNbt", at = @At("TAIL"))
    protected void sh_writeNbt_tail(NbtCompound nbt, CallbackInfo info) {
        if (this.isSticky) {
            nbt.putBoolean("IsSticky", true);
        }
    }

    // A Sticky Hopper can insert an item to the next inventory only if there is more than one item in a slot
    // ≥ v3.0: We allow to insert a non-stackable item from the slot defined by Main.config.nsif_observed_slot if needed (this method is then called a second time from sh_transfer_head below)
    @Redirect(method = "insert", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/Inventory;getStack(I)Lnet/minecraft/item/ItemStack;", ordinal = 0))
    private static ItemStack sh_insert_getStack(Inventory inventory, int slot) {
        ItemStack itemStack = inventory.getStack(slot);
        if (!((HopperBlockEntityMixin) inventory).isSticky
         || (((HopperBlockEntityMixin) inventory).needsNonStackableInsert && slot == ArrayUtils.indexOf(Main.config.getRecipe(), "item"))
         || itemStack.getCount() > 1) {
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
                    itemEntity.discard();
                    info.setReturnValue(true);
                }
            } else {
                if (itemEntity.getStack().isOf(Items.HONEY_BOTTLE)) {
                    ((HopperBlockEntityMixin) inventory).isSticky = true;
                    itemEntity.setStack(new ItemStack(Items.GLASS_BOTTLE));
                    info.setReturnValue(true);
                }
            }
        }
    }

    // ≥ v3.0: If the "recipe" is set, we try to transfer filtered non-stackable item
    @Inject(method = "transfer(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/inventory/Inventory;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Direction;)Lnet/minecraft/item/ItemStack;", at = @At("HEAD"), cancellable = true)
    private static void sh_transfer_head(@Nullable Inventory from, Inventory to, ItemStack stack, @Nullable Direction direction, CallbackInfoReturnable<ItemStack> info) {
        // This "recipe" requires only one item on each slot, so we can use the overridden to.isEmpty() method in this test
        // Also, the sticky hopper must not to be blocked, otherwise it would normally accept an inserted item (from another hopper) and so we would be forced to accept the transfer
        if (stack.getMaxCount() > 1 || to instanceof HopperBlockEntity && ((HopperBlockEntityMixin) to).isSticky
         && Main.config.general.nsif_enabled && to.isEmpty() && ((HopperBlockEntity) to).getCachedState().get(HopperBlock.ENABLED)) {
            String[] recipe = Main.config.getRecipe();
            int filteredSlot = ArrayUtils.indexOf(recipe, "item");
            int bookSlot = ArrayUtils.indexOf(recipe, "book");

            for (int index = 0; index < 5; ++index) {
                ItemStack invStack = to.getStack(index);
                if (index == filteredSlot) {
                    if (invStack.getMaxCount() > 1) {
                        return; // Wrong recipe, continue normal execution
                    }
                } else if (index == bookSlot) {
                    if (!invStack.isOf(Items.WRITABLE_BOOK) && !invStack.isOf(Items.WRITTEN_BOOK)) {
                        return; // Wrong recipe, continue normal execution
                    }
                } else {
                    try {
                        if (!invStack.isOf(Registry.ITEM.get(new Identifier(recipe[index])))) {
                            return; // Wrong recipe, continue normal execution
                        }
                    } catch (InvalidIdentifierException exception) {
                        return; // Wrong config, continue normal execution
                    }
                }
            }

            if (!stack.isOf(to.getStack(filteredSlot).getItem())) {
                info.setReturnValue(stack); // Incoming item rejected, transfer cancelled
                return;
            }

            boolean durability = Main.config.permissions.nsif_allows_diff_durability;
            boolean enchantments = Main.config.permissions.nsif_allows_diff_enchantment;
            boolean potionEffect = Main.config.permissions.nsif_allows_diff_potion_effect;
            boolean name = Main.config.permissions.nsif_allows_diff_name;
            if (bookSlot >= 0) {
                ItemStack book = to.getStack(bookSlot);
                if (WrittenBookItem.getPageCount(book) > 0) { // We check only the first page
                    String text = book.getNbt().getList("pages", 8).getString(0);
                    durability = durability && text.contains(Main.config.book.nsif_durability_keyword.length() > 0 ? Main.config.book.nsif_durability_keyword : "Durability");
                    enchantments = enchantments && text.contains(Main.config.book.nsif_enchantment_keyword.length() > 0 ? Main.config.book.nsif_enchantment_keyword : "Enchantment");
                    potionEffect = potionEffect && text.contains(Main.config.book.nsif_potion_effect_keyword.length() > 0 ? Main.config.book.nsif_potion_effect_keyword : "Potion");
                    name = name && text.contains(Main.config.book.nsif_name_keyword.length() > 0 ? Main.config.book.nsif_name_keyword : "Name");
                } else {
                    durability = enchantments = potionEffect = name = false;
                }
            }

            ItemStack filteredStack = to.getStack(filteredSlot);
            if ((!durability && stack.getDamage() != filteredStack.getDamage())
             || (!enchantments && !stack.getEnchantments().equals(filteredStack.getEnchantments()))
             || (!potionEffect && !stack.getNbt().getString("Potion").equals(filteredStack.getNbt().getString("Potion")))
             || (!name && !stack.getName().equals(filteredStack.getName()))) {
                info.setReturnValue(stack); // Incoming item rejected, transfer cancelled
                return;
            }

            ((HopperBlockEntityMixin) to).needsNonStackableInsert = true;
            boolean nonStackableInserted = insert(((HopperBlockEntity) to).getWorld(), ((HopperBlockEntity) to).getPos(), ((HopperBlockEntity) to).getCachedState(), to);
            ((HopperBlockEntityMixin) to).needsNonStackableInsert = false;

            // Last but not least, if the non-stackable item can't be inserted to another inventory we cancel the transfer, otherwise the item would get lost
            if (!nonStackableInserted) {
                info.setReturnValue(stack); // Transfer cancelled
                return;
            }
            to.setStack(filteredSlot, stack);
            info.setReturnValue(ItemStack.EMPTY); // Incoming item accepted, transfer done
        }
    }
}

package ovh.cuicui.stickyhopper.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import ovh.cuicui.stickyhopper.HopperBlockEntityMixinAccessor;

@Mixin(HopperBlock.class)
public abstract class HopperBlockMixin extends BlockWithEntity {
    public HopperBlockMixin(Settings settings) { super(settings); }

    // Replaces normal drop of the item to include "IsSticky" NBT if necessary
    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (world instanceof ServerWorld && blockEntity instanceof HopperBlockEntity && ((HopperBlockEntityMixinAccessor) blockEntity).isSticky()) {
            player.incrementStat(Stats.MINED.getOrCreateStat(this));
            player.addExhaustion(0.005F);

            Block.getDroppedStacks(state, (ServerWorld) world, pos, blockEntity).forEach((itemStack) -> {
                NbtCompound nbt = new NbtCompound();
                nbt.putBoolean("IsSticky", true);
                BlockItem.setBlockEntityNbt(itemStack, blockEntity.getType(), nbt);
                Block.dropStack(world, pos, itemStack);
            });

            state.onStacksDropped((ServerWorld)world, pos, stack);
        } else {
            super.afterBreak(world, player, pos, state, blockEntity, stack);
        }
    }
}

package ovh.cuicui.stickyhopper.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ovh.cuicui.stickyhopper.HopperBlockEntityMixinAccessor;


@Mixin(Block.class)
public abstract class BlockMixin extends AbstractBlock implements ItemConvertible {
    public BlockMixin(Settings settings) { super(settings); }

    @Inject(method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getDroppedStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;"), cancellable = true)
    private static void sh_dropStacks_before_getDroppedStacks(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack stack, CallbackInfo info) {
        if (blockEntity instanceof HopperBlockEntity && ((HopperBlockEntityMixinAccessor) blockEntity).isSticky()) {
            Block.getDroppedStacks(state, (ServerWorld)world, pos, blockEntity).forEach((itemStack) -> {
                NbtCompound nbt = new NbtCompound();
                nbt.putBoolean("sticky", true);
                itemStack.setSubNbt("BlockEntityTag", nbt);

                itemStack.setRepairCost(0); // Renamed items have that...

                Block.dropStack(world, pos, itemStack);
            });

            state.onStacksDropped((ServerWorld)world, pos, stack);
            info.cancel();
        }
    }
}

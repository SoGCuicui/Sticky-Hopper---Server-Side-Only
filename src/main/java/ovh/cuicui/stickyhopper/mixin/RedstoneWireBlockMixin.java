package ovh.cuicui.stickyhopper.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ovh.cuicui.stickyhopper.Main;

@Mixin(RedstoneWireBlock.class)
public abstract class RedstoneWireBlockMixin extends Block {
    public RedstoneWireBlockMixin(AbstractBlock.Settings settings) { super(settings); }

    @Inject(method = "canRunOnTop", at = @At("RETURN"), cancellable = true)
    private void sh_canRunOnStickyHoppers(BlockView world, BlockPos pos, BlockState floor, CallbackInfoReturnable<Boolean> info) {
        if (floor.isOf(Main.STICKY_HOPPER_BLOCK)) {
            info.setReturnValue(true);
            info.cancel();
        }
    }
}

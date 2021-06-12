package ovh.cuicui.stickyhopper;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class StickyHopperBlock extends HopperBlock implements BlockEntityProvider {
    public StickyHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new StickyHopperBlockEntity(pos, state);
    }
}

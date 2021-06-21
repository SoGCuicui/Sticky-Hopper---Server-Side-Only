package ovh.cuicui.stickyhopper;

import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class StickyHopperBlock extends HopperBlock {
    public StickyHopperBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new StickyHopperBlockEntity();
    }
}

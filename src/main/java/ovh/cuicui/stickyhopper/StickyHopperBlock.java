package ovh.cuicui.stickyhopper;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Blocks;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;

public class StickyHopperBlock extends HopperBlock {
    public StickyHopperBlock() {
        super(FabricBlockSettings.copy(Blocks.HOPPER));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new StickyHopperBlockEntity();
    }
}

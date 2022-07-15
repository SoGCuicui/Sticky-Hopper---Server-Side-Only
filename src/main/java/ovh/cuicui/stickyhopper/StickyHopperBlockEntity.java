package ovh.cuicui.stickyhopper;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class StickyHopperBlockEntity extends HopperBlockEntity {
    public StickyHopperBlockEntity(BlockPos pos, BlockState state) { super(pos, state); }

    @Override
    protected Text getContainerName() { return Text.translatable(Main.STICKY_HOPPER_CONTAINER_ID); }

    @Override
    public BlockEntityType<?> getType() { return Main.STICKY_HOPPER_BLOCK_ENTITY; }

    // A Sticky Hopper is considered empty even if there is still one item in some slots
    @Override
    public boolean isEmpty() {
        this.checkLootInteraction(null); // Needs to be done first
        for (int i = 0; i < this.size(); ++i) {
            if (this.getStack(i).getCount() > 1) {
                return (false);
            }
        }
        return (true);
    }
}

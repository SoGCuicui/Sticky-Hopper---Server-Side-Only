package ovh.cuicui.stickyhopper;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class StickyHopperBlockEntity extends HopperBlockEntity {
    public static String CONTAINER = "container.sticky_hopper";

    public StickyHopperBlockEntity() {
        super();
    }

    @Override
    protected Text getContainerName() {
        return new TranslatableText(CONTAINER);
    }

    @Override
    public BlockEntityType<?> getType() {
        return Main.STICKY_HOPPER_BLOCK_ENTITY;
    }
}

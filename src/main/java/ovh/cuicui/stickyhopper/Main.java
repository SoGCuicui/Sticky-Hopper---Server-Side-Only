package ovh.cuicui.stickyhopper;

import net.fabricmc.api.ModInitializer;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {
	private static String ID = "stickyhopper:sticky_hopper";
	private static String ENTITY_ID = "stickyhopper:sticky_hopper_entity";

	private static final StickyHopperBlock STICKY_HOPPER_BLOCK = new StickyHopperBlock();
	public static final BlockEntityType<StickyHopperBlockEntity> STICKY_HOPPER_BLOCK_ENTITY = BlockEntityType.Builder
			.create(StickyHopperBlockEntity::new, STICKY_HOPPER_BLOCK).build(null);

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, ID, STICKY_HOPPER_BLOCK);
		Registry.register(Registry.ITEM, ID, new BlockItem(STICKY_HOPPER_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));

		Registry.register(Registry.BLOCK_ENTITY_TYPE, ENTITY_ID, STICKY_HOPPER_BLOCK_ENTITY);

		System.out.println("Sticky Hopper mod loaded!");
	}
}

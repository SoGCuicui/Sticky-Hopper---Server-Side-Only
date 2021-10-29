package ovh.cuicui.stickyhopper;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class Main implements ModInitializer {
	private static final String STICKY_HOPPER_ID = "stickyhopper:sticky_hopper";
	private static final String STICKY_HOPPER_ENTITY_ID = "stickyhopper:sticky_hopper_entity";
	public static final String STICKY_HOPPER_CONTAINER_ID = "container.sticky_hopper";
	public static final StickyHopperBlock STICKY_HOPPER_BLOCK = new StickyHopperBlock(FabricBlockSettings.copy(Blocks.HOPPER));
	public static BlockEntityType<StickyHopperBlockEntity> STICKY_HOPPER_BLOCK_ENTITY;

	public static Configuration config;

	@Override
	public void onInitialize() {
		Registry.register(Registry.BLOCK, STICKY_HOPPER_ID, STICKY_HOPPER_BLOCK);
		Registry.register(Registry.ITEM, STICKY_HOPPER_ID, new BlockItem(STICKY_HOPPER_BLOCK, new Item.Settings().group(ItemGroup.REDSTONE)));
		STICKY_HOPPER_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, STICKY_HOPPER_ENTITY_ID, FabricBlockEntityTypeBuilder.create(StickyHopperBlockEntity::new, STICKY_HOPPER_BLOCK).build(null));

		AutoConfig.register(Configuration.class, Toml4jConfigSerializer::new);
		config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

		System.out.println("Sticky Hopper mod loaded!");
	}
}

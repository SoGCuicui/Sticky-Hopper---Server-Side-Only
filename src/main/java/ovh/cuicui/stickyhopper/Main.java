package ovh.cuicui.stickyhopper;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	public static Configuration config;

	@Override
	public void onInitialize() {
		AutoConfig.register(Configuration.class, Toml4jConfigSerializer::new);
		config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

		System.out.println("Sticky Hopper mod loaded!");
	}
}

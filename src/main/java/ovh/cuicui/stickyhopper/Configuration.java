package ovh.cuicui.stickyhopper;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

import java.util.Optional;

@Config(name = "sticky_hopper")
@Config.Gui.Background("minecraft:textures/block/hopper_outside.png")
public class Configuration implements ConfigData {
    @ConfigEntry.Gui.PrefixText()
    public boolean nsif_enabled = false;

    public boolean nsif_ignores_durability = false;

    @ConfigEntry.Gui.Excluded
    public int nsif_observed_slot = 2;

    @ConfigEntry.Gui.Excluded
    public String[] nsif_recipe_slots = {"minecraft:tripwire_hook", "minecraft:string", "", "minecraft:string", "minecraft:tripwire_hook"};

    public static Optional<Text[]> splitTooltipKey(String key) {
        String[] lines = Language.getInstance().get(key).split("\n");
        Text[] tooltip = new Text[lines.length];
        for (int i = 0, l = lines.length; i < l; ++i)
            tooltip[i] = new LiteralText(lines[i]);
        return Optional.of(tooltip);
    }
}

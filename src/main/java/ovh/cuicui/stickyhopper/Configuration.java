package ovh.cuicui.stickyhopper;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;

@Config(name = "sticky_hopper")
@Config.Gui.Background("minecraft:textures/block/hopper_outside.png")
public class Configuration implements ConfigData {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface NonStackableFilterTooltip {}

    @NonStackableFilterTooltip
    public boolean nonstackable_filter;

    public Configuration() {
        boolean nonstackable_filter = false;
    }

    public static Optional<Text[]> splitTooltipKey(String key) {
        String[] lines = Language.getInstance().get(key).split("\n");
        Text[] tooltip = new Text[lines.length];
        for (int i = 0, l = lines.length; i < l; ++i)
            tooltip[i] = new LiteralText(lines[i]);
        return Optional.of(tooltip);
    }
}

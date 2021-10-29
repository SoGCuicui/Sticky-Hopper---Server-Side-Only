package ovh.cuicui.stickyhopper;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "sticky_hopper")
@Config.Gui.Background("minecraft:textures/block/hopper_outside.png")
public class Configuration implements ConfigData {
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    public GeneralCategory general;

    @ConfigEntry.Category("recipe")
    @ConfigEntry.Gui.TransitiveObject
    public RecipeCategory recipe;

    @ConfigEntry.Category("permissions")
    @ConfigEntry.Gui.TransitiveObject
    public PermissionsCategory permissions;

    @ConfigEntry.Category("book")
    @ConfigEntry.Gui.TransitiveObject
    public BookCategory book;

    public Configuration() {
        this.general = new GeneralCategory();
        this.recipe = new RecipeCategory();
        this.permissions = new PermissionsCategory();
        this.book = new BookCategory();
    }

    public static class GeneralCategory implements Cloneable {
        @ConfigEntry.Gui.PrefixText()
        public boolean nsif_enabled = false;
    }

    public static class RecipeCategory implements Cloneable {
        @ConfigEntry.Gui.PrefixText()
        public String nsif_slot1 = "item";
        public String nsif_slot2 = "minecraft:item_frame";
        public String nsif_slot3 = "minecraft:comparator";
        public String nsif_slot4 = "minecraft:lectern";
        public String nsif_slot5 = "book";
    }

    public String[] getRecipe() {
        return (new String[]{this.recipe.nsif_slot1, this.recipe.nsif_slot2, this.recipe.nsif_slot3, this.recipe.nsif_slot4, this.recipe.nsif_slot5});
    }

    public static class PermissionsCategory implements Cloneable {
        @ConfigEntry.Gui.PrefixText()
        public boolean nsif_allows_diff_durability = true;
        public boolean nsif_allows_diff_enchantment = true;
        public boolean nsif_allows_diff_potion_effect = true;
        public boolean nsif_allows_diff_name = true;
    }

    public static class BookCategory implements Cloneable {
        @ConfigEntry.Gui.PrefixText()
        public String nsif_durability_keyword = "Durability";
        public String nsif_enchantment_keyword = "Enchantment";
        public String nsif_potion_effect_keyword = "Potion";
        public String nsif_name_keyword = "Name";
    }
}

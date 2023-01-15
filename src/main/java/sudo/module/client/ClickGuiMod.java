package sudo.module.client;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.NumberSetting;

public class ClickGuiMod extends Mod {

    public static ClickGuiMod INSTANCE;
    public BooleanSetting customColor = new BooleanSetting("CustomColor", false);
    public BooleanSetting blur = new BooleanSetting("Blur", false);
    public NumberSetting blurIntensity = new NumberSetting("Blur intensity", 2, 50, 8, 1);
//    public ColorSetting color = new ColorSetting("Color", new Color(157,115,230));
    
    
    public ClickGuiMod() {
        super("ClickGui", "description", Category.CLIENT, 0);
        INSTANCE = this;
        addSettings(customColor, blur, blurIntensity);
    }
}

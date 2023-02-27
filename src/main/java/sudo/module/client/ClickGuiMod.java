package sudo.module.client;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.NumberSetting;

public class ClickGuiMod extends Mod {

    public static ClickGuiMod INSTANCE;
    public BooleanSetting description = new BooleanSetting("Module desc", false);
    public BooleanSetting pause = new BooleanSetting("Pause", false);
    public BooleanSetting background = new BooleanSetting("Background", false);
    public BooleanSetting blur = new BooleanSetting("Blur", false);
    public NumberSetting blurIntensity = new NumberSetting("Blur intensity", 1, 50, 8, 1);
    
    
    public ClickGuiMod() {
        super("ClickGui", "Customize the ClickGUI", Category.CLIENT, 0);
        INSTANCE = this;
        addSettings(description, background, pause, blur, blurIntensity);
    }
}

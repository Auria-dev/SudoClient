package sudo.module.render;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;

import java.awt.*;

public class ClickGuiMod extends Mod {

    public static ClickGuiMod INSTANCE;
    public BooleanSetting customColor = new BooleanSetting("CustomColor", false);
    public ColorSetting color = new ColorSetting("Color", new Color(0xffA962E8));

    public ClickGuiMod() {
        super("ClickGui", "description", Category.RENDER, 0);
        INSTANCE = this;
        addSettings(customColor,color);
    }
}

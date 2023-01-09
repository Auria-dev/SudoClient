package sudo.module.render;

import java.awt.Color;

import sudo.module.Mod;
import sudo.module.settings.ColorSetting;

public class HudModule extends Mod{

    public ColorSetting Arraycolor = new ColorSetting("Arraylist", new Color(164, 2, 179));
    public ColorSetting Arraycolor2 = new ColorSetting("Arraylist", new Color(164, 2, 179));

	public HudModule() {
		super("Hud", "", Category.RENDER, 0);
		addSettings(Arraycolor, Arraycolor2);
	}
}

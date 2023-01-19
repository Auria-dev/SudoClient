package sudo.module.client;

import java.awt.Color;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;

public class HudModule extends Mod{

	public ModeSetting mode = new ModeSetting("Mode", "Original", "Original");
//	public ModeSetting mode = new ModeSetting("Mode", "Original", "Original", "Glow", "Classic");
    public BooleanSetting cute = new BooleanSetting("Cute Colors", false);
    public ColorSetting Arraycolor = new ColorSetting("ModList", new Color(0xff13294B));
    public ColorSetting Arraycolor2 = new ColorSetting("SideCL", new Color(0xffff294B));

	public HudModule() {
		super("Hud", "", Category.CLIENT, 0);
		addSettings(mode, cute, Arraycolor);
	}
}

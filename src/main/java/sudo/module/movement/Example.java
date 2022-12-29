package sudo.module.movement;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;

import java.awt.*;

public class Example extends Mod {

	public NumberSetting slider = new NumberSetting("Slider", 0, 10, 3, 0.1);
	public BooleanSetting bool = new BooleanSetting("Boolean", true);
	public ModeSetting mode = new ModeSetting("Mode", "Mode 1", "Mode 1", "Mode 2", "Mode 3");
	public ColorSetting coolor = new ColorSetting("Color", new Color(255,25,25));
	
	public Example() {
		super("Test module", "Just a test module", Category.MOVEMENT, 0);
		addSettings(slider, bool, mode,coolor);
	}
	
	
}

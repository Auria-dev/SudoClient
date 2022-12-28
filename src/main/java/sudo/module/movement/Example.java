package sudo.module.movement;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;

public class Example extends Mod {

	public NumberSetting slider = new NumberSetting("Slider", 0, 10, 3, 0.1);
	public BooleanSetting bool = new BooleanSetting("Boolean", true);
	public ModeSetting mode = new ModeSetting("Mode", "Mode 1", "Mode 1", "Mode 2", "Mode 3");
	
	public Example() {
		super("Test module", "Just a test module", Category.MOVEMENT, 0);
		addSettings(slider, bool, mode);
	}
	
	
}

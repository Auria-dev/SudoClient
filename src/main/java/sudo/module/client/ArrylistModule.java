package sudo.module.client;

import java.awt.Color;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;

public class ArrylistModule extends Mod{
	
	public BooleanSetting show = new BooleanSetting("Show", true);
	public ModeSetting mode = new ModeSetting("Color", "Pulse", "Pulse", "Orig", "Cute");
    public ColorSetting textColor = new ColorSetting("Text color", new Color(255,0,0));
    public ColorSetting pulseColor = new ColorSetting("Pulse color", new Color(120,0,0));
	public ModeSetting SortY = new ModeSetting("Sorting", "Normal", "Normal", "Reversed");
	public ModeSetting SortX = new ModeSetting("Alignment", "Right", "Right", "Left");
	public BooleanSetting background = new BooleanSetting("Background", true);
	public BooleanSetting outline = new BooleanSetting("Outline", false);
	public BooleanSetting glow = new BooleanSetting("Glow", true);
    public ColorSetting glowcolor = new ColorSetting("Glow color", new Color(0xffec03fb));
	
	public ArrylistModule() {
		super("Arraylist", "", Category.CLIENT, 0);
		addSettings(show, mode, textColor, pulseColor, SortX, SortY, background, outline, glow, glowcolor);
	}
	
	@Override
	public void onTick() {
		if (!mode.is("Pulse")) textColor.setVisible(false);
		else textColor.setVisible(true);
		super.onTick();
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	@Override
	public void onDisable() {
		super.onDisable();
	}
}

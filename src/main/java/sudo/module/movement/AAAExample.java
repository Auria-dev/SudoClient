package sudo.module.movement;

import sudo.Client;
import sudo.events.EventMotionUpdate;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;

import java.awt.*;

import com.google.common.eventbus.Subscribe;

import net.minecraft.client.util.math.MatrixStack;

public class AAAExample extends Mod {

	public NumberSetting slider = new NumberSetting("Slider", 0, 10, 3, 0.1);
	public BooleanSetting bool = new BooleanSetting("Boolean", true);
	public ModeSetting mode = new ModeSetting("Mode", "Mode 1", "Mode 1", "Mode 2", "Mode 3");
	public ColorSetting coolor = new ColorSetting("Color", new Color(255,25,25));
	
	public AAAExample() {
		super("AAA-Example", "Just a test module", Category.MOVEMENT, 0);
		addSettings(slider, bool, mode, coolor, mode);
	}
	
	@Override
	public void onEnable() {
		Client.logger.info("Enabled");
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		Client.logger.info("Disabled");
		super.onDisable();
	}
	@Override
	public void onTick() {
		if (mc.options.sneakKey.isPressed() && mc.player.isSprinting() && mc.player.isOnGround()) mc.player.addVelocity(0, 1, 0);
		super.onTick();
	}
	
	@Override
	public void onWorldRender(MatrixStack matrices) {
		
		super.onWorldRender(matrices);
	}

	@Subscribe
	public void onMotionUpdate(EventMotionUpdate event) {
		Client.logger.info("MotionUpdate");
	}
}

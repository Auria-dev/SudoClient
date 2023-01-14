package sudo.module.movement;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;

import java.awt.*;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class Example extends Mod {

	public NumberSetting slider = new NumberSetting("Slider", 0, 10, 3, 0.1);
	public BooleanSetting bool = new BooleanSetting("Boolean", true);
	public ModeSetting mode = new ModeSetting("Mode", "Mode 1", "Mode 1", "Mode 2", "Mode 3");
	public ColorSetting coolor = new ColorSetting("Color", new Color(255,25,25));
	
	public Example() {
		super("AAA-Example", "Just a test module", Category.MOVEMENT, 0);
		addSettings(slider, bool, mode,coolor);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	int cooldown = 50;
	static PlayerEntity target = null;
	@Override
	public void onTick() {
		HitResult hit = mc.crosshairTarget;

		if (mc.player != null) {
			if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
			    if (((EntityHitResult) hit).getEntity() instanceof PlayerEntity player) {
			        target = player;
			        if (target.isDead()) {
			        	if (cooldown<0) mc.player.sendChatMessage(target.getName().getString() + " died EZ");
			        	cooldown = 50;
			        	target=null;
			        }
			    }
			} else if (target == null) return;
		}
		cooldown--;
		super.onTick();
	}
	
	@Override
	public void onWorldRender(MatrixStack matrices) {
		super.onWorldRender(matrices);
	}
}

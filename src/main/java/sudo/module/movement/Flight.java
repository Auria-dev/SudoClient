package sudo.module.movement;

import org.lwjgl.glfw.GLFW;

import sudo.module.Mod;

public class Flight extends Mod {

	public Flight() {
		super("Flight", "Enables flight", Category.MOVEMENT, GLFW.GLFW_KEY_G);
	}
	
	@Override
	public void onTick() {
		this.mc.player.getAbilities().flying = true;
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		this.mc.player.getAbilities().flying = false;
		super.onDisable();
	}
}

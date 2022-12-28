package sudo.module.movement;

import org.lwjgl.glfw.GLFW;

import sudo.module.Mod;

public class Sprint extends Mod {

	public Sprint() {
		super("Sprint", "Automaticly sprints for you", Category.MOVEMENT, GLFW.GLFW_KEY_V);
	}
	
	@Override
	public void onTick() {
		this.mc.player.setSprinting(true);
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		this.mc.player.setSprinting(false);
		super.onDisable();
	}
}

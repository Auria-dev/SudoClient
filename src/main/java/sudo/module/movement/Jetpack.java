package sudo.module.movement;

import sudo.module.Mod;

public class Jetpack extends Mod {
	public Jetpack() {
		super("Jetpack", "Allows flight if in a boat", Category.MOVEMENT, 0);
	}
	
	
	@Override
	public void onTick() {
		if(mc.options.jumpKey.isPressed()){
            mc.player.jump();
        }
		super.onTick();
	}
}

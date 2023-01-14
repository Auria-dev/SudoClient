package sudo.module.movement;

import sudo.module.Mod;

public class AirJump extends Mod {

	public AirJump() {
		super("AirJump", "Auto sprint", Category.MOVEMENT, 0);
	}
	
	@Override
	public void onTick() {
		
		if (mc.player == null) {
			return;
		}
		if (mc.options.jumpKey.isPressed()) {
			mc.player.setOnGround(true);
			mc.player.fallDistance = 0f;
		}
		super.onTick();
	}
}

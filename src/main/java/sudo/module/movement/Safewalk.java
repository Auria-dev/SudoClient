package sudo.module.movement;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Box;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;

public class Safewalk extends Mod {

	private final BooleanSetting sneak = new BooleanSetting("Visual", false);
	private boolean sneaking;
	
	public Safewalk() {
		super("Safewalk", "Prevents the player from falling off the side of blocks", Category.MOVEMENT, 0);
		addSetting(sneak);
	}
	
	@Override
	public void onEnable() {
		sneaking = false;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if (sneaking) setSneaking(false);
		super.onDisable();
	}
	

	public void onClipAtLedge(boolean clipping) {
		if(!isEnabled() || !sneak.isEnabled() || !mc.player.isOnGround()) {
			if(sneaking)
				setSneaking(false);
			
			return;
		}
		
		ClientPlayerEntity player = mc.player;
		Box bb = player.getBoundingBox();
		float stepHeight = player.stepHeight;
		
		for(double x = -0.05; x <= 0.05; x += 0.05)
			for(double z = -0.05; z <= 0.05; z += 0.05)
				if(mc.world.isSpaceEmpty(player, bb.offset(x, -stepHeight, z)))
					clipping = true;
				
		setSneaking(clipping);
	}
	
	private void setSneaking(boolean sneaking) {
		
		if(sneaking)
			mc.options.sneakKey.setPressed(true);
		else {
			if( mc.options.sneakKey.isPressed()) {
				mc.options.sneakKey.setPressed(true);
				} 
			else {mc.options.sneakKey.setPressed(false);
			}
		}
		this.sneaking = sneaking;
	}
	
}

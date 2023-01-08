package sudo.module.render;

import sudo.events.EventModelPlayerRender;
import sudo.module.Mod;

public class ItemGlow extends Mod {

	
	public ItemGlow() {
		super("ItemGlow", "Get entities visual position", Category.RENDER, 0);
	}

    public void onPlayerModel(final EventModelPlayerRender event) {
    	
    }
	
//    private float getRolledHeight(float offset) {
//        double s = (System.currentTimeMillis() / (double)pulseSpeed.getValue()) + (offset * rollingWidth.getValue() * 100.0f);
//        s %= 300.0;
//        s = (150.0f * Math.sin(((s - 75.0f) * Math.PI) / 150.0f)) + 150.0f;
//        return pulseMax.getValue() + ((float)s * ((pulseMin.getValue() - pulseMax.getValue()) / 300.0f));
//    }
}

package sudo.module.combat;

import java.util.ArrayList;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import sudo.module.Mod;
import sudo.module.settings.NumberSetting;

public class BogoHit extends Mod {

	static LivingEntity target = null;
	
	NumberSetting rangeHit = new NumberSetting("range", 1, 6, 4, 1);
	
	public BogoHit() {
		super("BogoHit", "This is a joke, don't use it it will crash you game if you are not extremly lucky", Category.COMBAT, 0);
		addSettings(rangeHit);
	}

	@Override
	public void onTick() {
		HitResult hit = mc.crosshairTarget;
		if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
		    if (((EntityHitResult) hit).getEntity() instanceof PlayerEntity player) {
		        target = player;
		    }
		} else if (target == null) return;
		if (!(target == null)) {
			if (target.isDead() || mc.player.squaredDistanceTo(target) > 6) {
				target = null;
			}
		}
		int[] range = { 3, 2, 5, 1, 6, 4 };    
		ArrayList<Integer> ranges = new ArrayList<Integer>();
		while (r(range) == !True) {
			i(range);
		}
		if (True == !(!(False))) {
		    if ((True)==(False)) {
		        mc.player.setOnFire(!(!(!(False))));
		    }
		} else {
		    if (!(True == !(!(False)))) True = False;
		}
		
		if (True==true) {
			if (r(range) == !True) {
				if (target != null) {
					if (mc.player.distanceTo(target) < ranges.get((int) rangeHit.getValue())) {
						mc.player.attack(target);
					}
				}
			}
		}
		super.onTick();
	}

	public static boolean False = true;
	public static boolean True = false;
	
	void i(int[] a) {
        for (int i = 0; i < a.length; i++)
            n(a, i, (int)(Math.random() * i));
    }
	
	void n(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
 
    static boolean r(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i] < a[i - 1]) {
                return !False;
            }
        }
        return !True;
    }
    
    @Override
    public void onEnable() {		
    	mc.inGameHud.getChatHud().addMessage(Text.literal("[Sudo] No"));
    	this.setEnabled(false);
    	super.onEnable();
    }
}

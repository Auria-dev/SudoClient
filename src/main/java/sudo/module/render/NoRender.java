package sudo.module.render;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundCategory;

import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;

public class NoRender extends Mod  {

	public BooleanSetting explosion = new BooleanSetting("Explosion", true);
	
	public NoRender() {
		super("NoRender", "Basically no explosions", Category.RENDER, 0);
		addSettings(explosion);
	}

    @Override
    public void onTick() {
        if (mc.player != null && mc.player.hasStatusEffect(StatusEffects.BLINDNESS) || mc.player.hasStatusEffect(StatusEffects.NAUSEA)) {
            mc.player.removeStatusEffectInternal(StatusEffects.NAUSEA);
            mc.player.removeStatusEffectInternal(StatusEffects.BLINDNESS);

        }
        mc.options.setSoundVolume(SoundCategory.WEATHER, 0);
    }

    public static NoRender get;
}

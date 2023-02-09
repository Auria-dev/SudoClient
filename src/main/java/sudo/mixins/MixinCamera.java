package sudo.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.Camera;
import sudo.module.ModuleManager;
import sudo.module.render.NoCameraClip;

@Mixin(Camera.class)
public abstract class MixinCamera {
	
	@Inject(at = @At("HEAD"), method = "clipToSpace(D)D", cancellable = true)
	private void onClipToSpace(double desiredCameraDistance, CallbackInfoReturnable<Double> cir) {
		if(ModuleManager.INSTANCE.getModule(NoCameraClip.class).isEnabled()) cir.setReturnValue(desiredCameraDistance);
	}
}

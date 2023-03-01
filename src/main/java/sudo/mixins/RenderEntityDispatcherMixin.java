package sudo.mixins;


import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import sudo.module.ModuleManager;
import sudo.module.combat.Hitbox;
import sudo.module.render.Chams;
import sudo.utils.mixins.IBox;

@Mixin(EntityRenderDispatcher.class)
public class RenderEntityDispatcherMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public <E extends Entity> void onRenderPre(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Chams.get.shouldRender(entity)) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0f, -1100000.0f);
        }
    }

    @Inject(method = "render", at = @At("RETURN"))
    public <E extends Entity> void onRenderPost(E entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (Chams.get.shouldRender(entity)) {
            GL11.glPolygonOffset(1.0f, 1100000.0f);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        }
    }
    
    @Inject(method = "renderHitbox", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/math/Box;FFFF)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT)
    private static void onRenderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo info, Box box) {
        double v = ModuleManager.INSTANCE.getModule(Hitbox.class).size.getValue();
        if (v != 0 && ModuleManager.INSTANCE.getModule(Hitbox.class).isEnabled()) ((IBox) box).expand(v);
    }

}
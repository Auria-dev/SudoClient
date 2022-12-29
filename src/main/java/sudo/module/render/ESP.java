package sudo.module.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import sudo.events.EventRender3D;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;
import sudo.utils.render.RenderUtils;

public class ESP extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "2D", "2D", "Box", "Glow");
	
	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting monsters = new BooleanSetting("Monsters", true);
	public BooleanSetting items = new BooleanSetting("Items", true);
	public BooleanSetting animals = new BooleanSetting("Animals", true);
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
	
	public ColorSetting color = new ColorSetting("Color", new Color(255, 0, 0));
	
	public ESP() {
		super("ESP", "esp.", Category.RENDER, 0);
		addSettings(mode, players,monsters,items,animals,invisibles, color);
	}
	
	@Override
	public void onWorldRender(MatrixStack matrices) {
		if (this.isEnabled()) {
			for (Entity e  : mc.world.getEntities()) {
				if (!(e instanceof ClientPlayerEntity)) {
					if (shouldRenderEntity(e)) {
						if (mode.is("Rect")) {
							renderOutline(e, color.getColor(), matrices);
						}
						Vec3d renderPos = RenderUtils.getEntityRenderPosition(e, EventRender3D.getTickDelta());
						if (mode.is("Box")) {
							RenderUtils.drawEntityBox(matrices, e, renderPos.x, renderPos.y, renderPos.z, color.getColor());
						}
						if (mode.is("Glow")) {
						}
					}
				}
			}
		}
		super.onWorldRender(matrices);
	}
	
	void renderOutline(Entity e, Color color, MatrixStack stack) {
        float red = color.getRed() / 255f;
        float green = color.getGreen() / 255f;
        float blue = color.getBlue() / 255f;
        float alpha = color.getAlpha() / 255f;
        Camera c = mc.gameRenderer.getCamera();
        Vec3d camPos = c.getPos();
        Vec3d start = e.getPos().subtract(camPos);
        float x = (float) start.x;
        float y = (float) start.y;
        float z = (float) start.z;

        double r = Math.toRadians(-c.getYaw() + 90);
        float sin = (float) (Math.sin(r) * (e.getWidth() / 1.7));
        float cos = (float) (Math.cos(r) * (e.getWidth() / 1.7));
        stack.push();

        Matrix4f matrix = stack.peek().getPositionMatrix();
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        GL11.glDepthFunc(GL11.GL_ALWAYS);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableBlend();
        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y, z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x - sin, y + e.getHeight(), z - cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y + e.getHeight(), z + cos).color(red, green, blue, alpha).next();
        buffer.vertex(matrix, x + sin, y, z + cos).color(red, green, blue, alpha).next();

        BufferRenderer.drawWithShader(buffer.end());
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        RenderSystem.disableBlend();
        stack.pop();
    }
	
	public boolean shouldRenderEntity(Entity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity) return true;
		if (monsters.isEnabled() && entity instanceof Monster) return true;
		if (items.isEnabled() && entity instanceof ItemEntity)
		if (animals.isEnabled() && (entity instanceof PassiveEntity || entity instanceof Entity)) return true;
		if (invisibles.isEnabled() && entity.isInvisible()) return true;
		return false;
	}
}

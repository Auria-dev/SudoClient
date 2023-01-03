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
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import sudo.events.EventRender3D;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;
import sudo.utils.render.RenderUtils;

public class ESP extends Mod {

	public static ModeSetting mode = new ModeSetting("Mode", "Rect", "Rect", "Box", "Glow");

	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting monsters = new BooleanSetting("Monsters", true);
	public BooleanSetting passives = new BooleanSetting("Passives", true);
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
	public BooleanSetting items = new BooleanSetting("Items", true);
	
	public ColorSetting color = new ColorSetting("Color", new Color(255, 0, 0));

	
	public ESP() {
		super("ESP", "Get entities visual position", Category.RENDER, 0);
		addSettings(mode, players,monsters,passives,invisibles,items, color);
	}
	
	private static final Formatting Gray = Formatting.GRAY;
	
	@Override
	public void onTick() {
		this.setDisplayName("ESP" + Gray + " ["+mode.getMode()+"]");
	}
	
	@Override
	public void onWorldRender(MatrixStack matrices) {
		if (this.isEnabled()) {
			for (Entity e  : mc.world.getEntities()) {
				if (!(e instanceof ClientPlayerEntity)) {
					if (shouldRenderEntity(e)) {
						if (mode.is("Rect")) {
							RenderUtils.renderOutlineRect(e, color.getColor(), matrices);
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
	
	@Override
	public void onEnable() {
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		super.onDisable();
	}
	
	public boolean shouldRenderEntity(Entity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity) return true;
		if (monsters.isEnabled() && entity instanceof Monster) return true;
		if (passives.isEnabled() && (entity instanceof PassiveEntity))return true;
		if (invisibles.isEnabled() && entity.isInvisible()) return true;
		if (items.isEnabled() && entity instanceof ItemEntity) return true;
		return false;
	}
}

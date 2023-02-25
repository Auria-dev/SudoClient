package sudo.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.google.common.collect.Lists;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import sudo.events.EventRender3D;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;
import sudo.utils.player.RotationUtils;
import sudo.utils.render.RenderUtils;

public class Killaura extends Mod {

	public static ArrayList<String> modes;
	public static ModeSetting mode;
	public static ModeSetting rotationmode;
	public static NumberSetting range;
	public static BooleanSetting cooldown;
	public static ModeSetting priority;
	public static BooleanSetting render;
	public static ModeSetting critMode;
	public static BooleanSetting switchItem;

	//ToDo: Make it render the target

	public static Killaura instance;

	public Killaura() {
		super("Killaura", "Automatically attacks entities for you", Category.COMBAT, 0);
		this.addSettings(Killaura.mode, Killaura.rotationmode, Killaura.critMode, Killaura.range, Killaura.cooldown, Killaura.priority,Killaura.render,Killaura.switchItem);
		instance = this;
	}

	private static final Formatting Gray = Formatting.GRAY;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final List<LivingEntity> targets = (List)Lists.newArrayList();

	@Override
	public void onTick() {
		this.setDisplayName("Killaura" + Gray + "["+mode.getMode()+"]");
		if (this.isEnabled() && this.mc.world != null) {
			if (this.mc.world.getEntities() != null) {
				for (final Entity e : this.mc.world.getEntities()) {
					if (e instanceof LivingEntity && e != this.mc.player && this.mc.player.distanceTo(e) <= Killaura.range.getValue()) {
						targets.add((LivingEntity)e);
					}
					else if (targets.contains(e)) {
						targets.remove(e);
					}
					if (Killaura.priority.is("Distance")) {
						targets.sort(Comparator.comparingDouble(entity -> this.mc.player.distanceTo(e)).reversed());
					}
					else {
						if (!Killaura.priority.is("Health")) {
							continue;
						}
						targets.sort(Comparator.comparingDouble(entity -> ((LivingEntity) entity).getHealth()).reversed());
					}
				}
				if (targets.size() - 1 >= 0) {
					final float pitch = RotationUtils.getRotations(targets.get(0))[1];
					final float yaw = RotationUtils.getRotations(targets.get(0))[0];
					if (Killaura.mode.getMode() == "Camera" && targets.get(0).isAlive()) {
						if (Killaura.rotationmode.is("Legit")) {

							this.mc.player.setYaw(yaw);
							this.mc.player.setPitch(pitch);
						}
						else if (Killaura.rotationmode.is("Silent")) {
							RotationUtils.setSilentPitch(pitch);
							RotationUtils.setSilentYaw(yaw);
						}
						if (!Killaura.cooldown.isEnabled() || this.mc.player.getAttackCooldownProgress(0.5f) == 1.0f ) {
							if(mc.player.isOnGround() && critMode.is("Jump")) {
								mc.player.setVelocity(0, 0.15, 0);
							}

							if (switchItem.isEnabled()) {
								for (int i = 0; i < 9; i++) {
									if (mc.player.getInventory().getStack(i).getItem() instanceof SwordItem)
										mc.player.getInventory().selectedSlot = i;
								}
							}

							this.mc.interactionManager.attackEntity((PlayerEntity)this.mc.player, (Entity)targets.get(0));
							this.mc.player.swingHand(Hand.MAIN_HAND);
							this.resetRotation();
						}
					}
					if (Killaura.mode.getMode() == "Packet") {

					}
				}
			}
		}
	}

	@Override
	public void onDisable() {
		this.resetRotation();
		super.onDisable();
	}

	public void resetRotation() {
		RotationUtils.resetPitch();
		RotationUtils.resetYaw();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onWorldRender(MatrixStack matrices) {
		final List<LivingEntity> targets = (List)Lists.newArrayList();

		if (this.mc.world.getEntities() != null && render.isEnabled()) {
			for (final Entity e : this.mc.world.getEntities()) {
				if (e instanceof LivingEntity && e != this.mc.player && this.mc.player.distanceTo(e) <= Killaura.range.getValue()) {
					targets.add((LivingEntity)e);
				} else if (targets.contains(e)) {
					targets.remove(e);
				}
				Vec3d renderPos = RenderUtils.getEntityRenderPosition(e, EventRender3D.getTickDelta());
				if(render.isEnabled()) {
					RenderUtils.drawEntityBox(matrices, e, renderPos.x, renderPos.y, renderPos.z, new Color(230, 30, 30));
				}
			}
		}
		super.onWorldRender(matrices);
	}

	static {
		Killaura.modes = new ArrayList<String>();
		Killaura.mode = new ModeSetting("Mode", "Camera", new String[] { "Camera", "Packet" });
		Killaura.rotationmode = new ModeSetting("Rotation", "Silent", new String[] { "Silent", "Legit" });
		Killaura.range = new NumberSetting("Range", 3.0, 6.0, 4.0, 0.1);
		Killaura.cooldown = new BooleanSetting("Cooldown", true);
		Killaura.priority = new ModeSetting("Priority", "Random", new String[] { "Random", "Random" });
		Killaura.render = new BooleanSetting("Render", false);
		Killaura.critMode = new ModeSetting("Crit Mode", "Jump", "Jump", "Soon");
		Killaura.switchItem = new BooleanSetting("Switch", true);
	}
}
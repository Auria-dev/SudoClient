package sudo.module.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import sudo.events.EventSendPacket;
import sudo.mixins.accessors.PlayerMoveC2SPacketAccessor;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;
import sudo.module.world.Scaffold;
import sudo.utils.player.RotationUtils;
import sudo.utils.render.ColorUtils;
import sudo.utils.render.RenderUtils;


public class Killaura extends Mod {
    public static ArrayList<String> modes = new ArrayList<>();

    public static ModeSetting rotationmode = new ModeSetting("Rotation", "Silent", "Silent", "Legit");
    public static NumberSetting range = new NumberSetting("Range", 3, 6, 4, 0.1);
    public static BooleanSetting cooldown = new BooleanSetting("Cooldown", true);
    public static BooleanSetting swing = new BooleanSetting("Swing", true);
    public static ModeSetting priority = new ModeSetting("Priority", "Distance", "Distance", "Health");

	public BooleanSetting trigger = new BooleanSetting("Trigger", false);
	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting animals = new BooleanSetting("Animals", false);
	public BooleanSetting monsters = new BooleanSetting("Monsters", false);
	public BooleanSetting passives = new BooleanSetting("Passives", false);
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
	
	public static LivingEntity target;
	private float smoothYaw, smoothPitch;

    public Killaura() {
        super("Killaura", "Automatically attacks living entities for you", Category.COMBAT, 0);
        addSettings(rotationmode, range, priority, cooldown, swing, trigger, players, animals, monsters, passives, invisibles);
    }
    
    public void onTick() {
        if (this.isEnabled()) {
            if (mc.world != null && mc.world.getEntities() != null) {
				List<LivingEntity> targets = Lists.<LivingEntity>newArrayList();
                for (Entity e : mc.world.getEntities()) {
                    if (e instanceof LivingEntity && e != mc.player && mc.player.distanceTo(e) <= range.getValue())
                        targets.add((LivingEntity) e);
                    else {
                        if (targets.contains(e)) targets.remove(e);
                    }
                }
                
                //remove target
                if (target != null && mc.player.distanceTo(target) > range.getValue()) {
					targets.remove(target);
					target = null;
					if(!ModuleManager.INSTANCE.getModule(Scaffold.class).isEnabled()){
						RotationUtils.resetPitch();
						RotationUtils.resetYaw();
					}
				}
                
                //sort target
                switch(priority.getSelected()) {
					case "Distance": 
						targets.sort(Comparator.comparingDouble(entity -> mc.player.distanceTo(entity)));
						break;
					case "Health": 
						targets.sort(Comparator.comparingDouble(entity -> ((LivingEntity)entity).getHealth()));
						break;
				}
                
                if(!targets.isEmpty()) {
                	target = (LivingEntity)targets.get(0);
                    if (target != null) {
						
						this.setDisplayName("Killaura " + ColorUtils.gray + (target instanceof PlayerEntity ? target.getName().getString().replaceAll(ColorUtils.colorChar, "&") : target.getDisplayName().getString())+ "  ");
						if (canAttack(target) && !target.isInvulnerable()) {
							float yaw = RotationUtils.getRotations(target)[0];
							float pitch = RotationUtils.getRotations(target)[1];
							if (smoothYaw != yaw) smoothYaw += RenderUtils.slowDownTo(smoothYaw, yaw, 10);
							if (smoothPitch != pitch) smoothYaw += RenderUtils.slowDownTo(smoothPitch, pitch, 10);
							RotationUtils.setSilentPitch(smoothPitch);
							RotationUtils.setSilentYaw(smoothYaw);

							if (rotationmode.is("Legit")) {
							    mc.player.setYaw(yaw);
							    mc.player.setPitch(pitch); 
							}
							
							if (cooldown.isEnabled() ? mc.player.getAttackCooldownProgress(0.5F) == 1 : true) {
								double posX = mc.player.getX();
								double posY = mc.player.getY();
								double posZ = mc.player.getZ();
							    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(posX, posY + 0.0633, posZ, false));
							    mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(posX, posY, posZ, false));
							    
								mc.interactionManager.attackEntity(mc.player, target);
								if (swing.isEnabled()) mc.player.swingHand(Hand.MAIN_HAND);
								else mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
								if (swing.isEnabled() && !(mc.player.getMainHandStack().getItem() instanceof SwordItem)) mc.player.swingHand(Hand.MAIN_HAND); 
								resetRotation();
							}
	                	}
                    }
                    if (mc.player.squaredDistanceTo(target)>range.getValue()) resetRotation();
                }
            }
        }
    
    }
    
    @Override
    public void onWorldRender(MatrixStack matrices) {
    	if (target!=null) RenderUtils.renderOutlineRect(target, new Color(ColorUtils.rainbow(400f, 1f, 1f)), matrices);
    	super.onWorldRender(matrices);
    }
    
    @Override
    public void onEnable() {
    	super.onEnable();
    }
    
	@Override
	public void onDisable() {
		resetRotation();
		target = null;
		super.onDisable();
	}
	
    public void resetRotation() {
    	RotationUtils.resetPitch();
    	RotationUtils.resetYaw();
    }

    @Subscribe
    public void sendPacket(EventSendPacket event) {
    	if (event.getPacket() instanceof PlayerMoveC2SPacket) {
			if (target != null && rotationmode.is("Silent")) {
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw(RotationUtils.getRotations(target)[0]);
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(RotationUtils.getRotations(target)[1]);
			} else {
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw(mc.player.getYaw());
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(mc.player.getPitch());
			}
		}
    }
    
	private boolean canAttack(LivingEntity entity) {
		if (entity != mc.player && mc.player.distanceTo(entity) <= range.getValue() && entity.isAlive() && mc.player.isAlive()) {
			if (!trigger.isEnabled()) {
				return isKillauraEntity(entity);
			} else {
				HitResult hitResult = mc.crosshairTarget;
				if (hitResult != null && hitResult.getType() == Type.ENTITY) {
					Entity entity1 = ((EntityHitResult) hitResult).getEntity();
					if (entity1 != null && entity1 == entity) return isKillauraEntity(entity);
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean isKillauraEntity(LivingEntity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity) return true;
		if (animals.isEnabled() && entity instanceof AnimalEntity) return true;
		if (monsters.isEnabled() && entity instanceof Monster) return true;
		if (passives.isEnabled() && entity instanceof PassiveEntity && !(entity instanceof ArmorStandEntity)) return true;
		if (passives.isEnabled() && entity.isInvisible()) return true;
		return false;
	}
}
package sudo.module.combat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import sudo.Client;
import sudo.core.event.EventTarget;
import sudo.events.EventRender3D;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.movement.Flight;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.NumberSetting;
import sudo.utils.player.PlayerUtils;
import sudo.utils.player.RotationUtils;
import sudo.utils.render.ColorUtils;
import sudo.utils.render.RenderUtils;

public class TargetStrafe extends Mod {
	private static MinecraftClient mc = MinecraftClient.getInstance();

    public static NumberSetting radius = new NumberSetting("Radius", 0.5, 5, 3, 0.1);
    public static BooleanSetting spacebar = new BooleanSetting("Spacebar", false);
    public static BooleanSetting thirdPerson = new BooleanSetting("Third Person", false);
    public static BooleanSetting rainbow = new BooleanSetting("Rainbow", false);

    public static NumberSetting speed = new NumberSetting("Speed", 0.1, 2, 0.8, 0.1);
    
	public TargetStrafe() {
		super("TargetStrafe", "Strafe arround the current Killaura target", Category.COMBAT, 0);
        addSettings(radius, spacebar, rainbow, thirdPerson, speed);
	}
	
    @Override
    public void onTick() {
    	if (thirdPerson.isEnabled() && ModuleManager.INSTANCE.getModule(Killaura.class).isEnabled()) {
    		if (Killaura.target != null && canStrafe())
    			mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    		else
    			mc.options.setPerspective(Perspective.FIRST_PERSON);
    		
    	}
    	if (Killaura.target != null) {
    		TargetStrafe.strafe(speed.getValueFloat(), Killaura.target, true, false);
    	}
    	super.onTick();
    }

    
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
	
	@Override
	public void onWorldRender(MatrixStack matrices) {
		if (Killaura.target != null && (Killaura.target != mc.player && mc.player.distanceTo(Killaura.target) <= Killaura.range.getValue() && Killaura.target.isAlive() && mc.player.isAlive()))
			drawCircle(matrices, Killaura.target, radius.getValue(), 100);
		super.onWorldRender(matrices);
	}
	
	@EventTarget
    public final void onRender3D(EventRender3D event) {
    	Client.logger.info("EventRender3D");
    	if (Killaura.target != null)
    		this.setDisplayName("TargetStrafe " + ColorUtils.gray + Killaura.target.getName().getString());
    	else
    		this.setDisplayName("TargetStrafe " + ColorUtils.gray + "None");
        if (ModuleManager.INSTANCE.getModule(Killaura.class).isEnabled() && Killaura.target != null) {
        	LivingEntity target = Killaura.target;
            drawCircle(event.getMatrices(), target, radius.getValue(), 0.1);
        }
    }
    
    public static void strafe(double moveSpeed, LivingEntity target,  boolean direction, boolean flight) {
		if (Killaura.target != null && (Killaura.target != mc.player && mc.player.distanceTo(Killaura.target) <= Killaura.range.getValue() && Killaura.target.isAlive() && mc.player.isAlive())) {
	    	try {
		        double direction1 = direction ? 1 : -1;
		        float[] rotations = RotationUtils.getRotations(target);
		        
		        if ((double) mc.player.distanceTo(target) <= radius.getValue()) {
		            PlayerUtils.setSpeed(moveSpeed, mc.player.getVelocity().y, rotations[0], direction1, 0.0D);
		        } else {
		            PlayerUtils.setSpeed(moveSpeed, mc.player.getVelocity().y, rotations[0], direction1, 1.0D);
		        }
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	}
    	}
    }

	
    public static boolean canStrafe() {
    	if (Killaura.target == null) return false;
        return Killaura.target != null 
        		&& !Killaura.target.isDead() 
        		&& (spacebar.isEnabled() ? ModuleManager.INSTANCE.getModule(Killaura.class).isEnabled() 
        				&& Killaura.target != null 
        				&& PlayerUtils.isMoving() 
        				&& ModuleManager.INSTANCE.getModule(TargetStrafe.class).isEnabled() 
        				&& (ModuleManager.INSTANCE.getModule(Flight.class).isEnabled() ? true : mc.options.jumpKey.isPressed()) : ModuleManager.INSTANCE.getModule(Killaura.class).isEnabled() 
        				&& Killaura.target != null 
        				&& PlayerUtils.isMoving() 
        				&& ModuleManager.INSTANCE.getModule(TargetStrafe.class).isEnabled()
        			);
    }
    
    private void drawCircle(MatrixStack matrices, Entity entity, double rad, double height) {
    	boolean canSee = true;
    	if (!canSee) {
    		return;
    	}
    	RenderUtils.drawCircle(matrices, entity.getPos(), rad, height, rainbow.isEnabled() ? ColorUtils.rainbow(2f, 1f, 1f) : -1);
    }
}

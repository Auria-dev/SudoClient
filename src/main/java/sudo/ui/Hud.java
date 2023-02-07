package sudo.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.client.ArrylistModule;
import sudo.module.client.Notifications;
import sudo.module.combat.TargetHud;
import sudo.utils.TimerUtil;
import sudo.utils.misc.Notification;
import sudo.utils.misc.NotificationUtil;
import sudo.utils.render.ColorUtils;
import sudo.utils.render.RenderUtils;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;

public class Hud {
	private static MinecraftClient mc = MinecraftClient.getInstance();
	public static GlyphPageFontRenderer textRend = IFont.CONSOLAS;
	
	public static void render(MatrixStack matrices, float tickDelta) {
		textRend.drawString(matrices, "Sudo client", 5, 5, -1, 1);
		
		if (ModuleManager.INSTANCE.getModule(ArrylistModule.class).show.isEnabled())
			renderArrayList(matrices);
		if (ModuleManager.INSTANCE.getModule(TargetHud.class).isEnabled())
			renderTargetHud(matrices);
		if (ModuleManager.INSTANCE.getModule(Notifications.class).enabled.isEnabled()) 
			Notifs(matrices);
	}
	
	
	@SuppressWarnings("unused")
	public static void renderArrayList(MatrixStack matrices) {
		int x, y = 0;
		int xOffset = -3;
		int yOffset = 5;
		
		int index = 0;
		int sWidth = mc.getWindow().getScaledWidth();
		int sHeight = mc.getWindow().getScaledHeight();
		
		List<Mod> enabled = ModuleManager.INSTANCE.getEnabledModules();
		if (ModuleManager.INSTANCE.getModule(ArrylistModule.class).SortY.is("Normal")) enabled.sort(Comparator.comparingInt(m -> (int)textRend.getStringWidth(((Mod)m).getDisplayName())).reversed());
		else if (ModuleManager.INSTANCE.getModule(ArrylistModule.class).SortY.is("Reversed")) enabled.sort(Comparator.comparingInt(m -> (int)textRend.getStringWidth(((Mod)m).getDisplayName())));
		
		for (Mod mod : enabled) {
			int fWidth = (int) textRend.getStringWidth(mod.getDisplayName());
			int fHeight = (int) textRend.getFontHeight();

			int fromX = xOffset+(sWidth-4) - fWidth-2   +1;
			int fromY = yOffset+(fHeight*index)-1;
			int toX = xOffset+(sWidth-2)+1;
			int toY = yOffset+(fHeight*index)+fHeight;
			
			if (mod.isEnabled()) {
				
				RenderUtils.renderRoundedShadow(matrices, ModuleManager.INSTANCE.getModule(ArrylistModule.class).Arraycolor.getColor(), fromX, fromY+1, toX, toY, 1, 500, 4);
				RenderUtils.renderRoundedQuad(matrices, ModuleManager.INSTANCE.getModule(ArrylistModule.class).Arraycolor.getColor(), fromX, fromY+1, toX, toY, 1, 500);
				
//				RenderUtils.blur(matrices, fromX, fromY+1, toX-fromX, toY-fromY, 8f);
				index++;
			}
		}
		index=0;
		for (Mod mod : enabled) {
			int fWidth = (int) textRend.getStringWidth(mod.getDisplayName());
			int fHeight = (int) textRend.getFontHeight();

			int fromX = xOffset+(sWidth-4) - fWidth-2   +1;
			int fromY = yOffset+(fHeight*index)-1;
			int toX = xOffset+(sWidth-2)+1   +1;
			int toY = yOffset+(fHeight*index)+fHeight;
			
			if (mod.isEnabled()) {
//				DrawableHelper.fill(matrices, fromX, fromY+1, toX-1, toY, 0x90000000);
				if (ModuleManager.INSTANCE.getModule(ArrylistModule.class).cute.isEnabled()) {
					textRend.drawStringWithShadow(matrices, mod.getDisplayName(), fromX, fromY, ColorUtils.getCuteColor(index), 1);
				} else {
					textRend.drawStringWithShadow(matrices, mod.getDisplayName(), fromX, fromY, -1, 1);
				}

//				DrawableHelper.fill(matrices, fromX, fromY+1, toX-1, toY, 8f);
//				RenderUtils.renderRoundedQuad(matrices, new Color(10, 10, 10, 100), fromX, fromY, toX, toY, 1, 500);
//				DrawableHelper.fill(matrices, toX, fromY+1, toX-1, toY,ModuleManager.INSTANCE.getModule(HudModule.class).Arraycolor2.getColor().getRGB());
//				DrawableHelper.fill(matrices, toX, fromY+1, toX-2, toY, ColorUtils.rainbow(10, 0.8f, 1, 200*index));
				index++;
			}
		}
	}
	

	static PlayerEntity target = null;
	
	@SuppressWarnings("static-access")
	public static void renderTargetHud(MatrixStack matrices) {
		HitResult hit = mc.crosshairTarget;
		int sWidth = mc.getWindow().getScaledWidth();
		int sHeight = mc.getWindow().getScaledHeight();
		
		if (mc.player != null) {
			
			if (hit != null && hit.getType() == HitResult.Type.ENTITY) {
			    if (((EntityHitResult) hit).getEntity() instanceof PlayerEntity player) {
			        target = player;
			    }
			} else if (target == null) return;
			
			int maxDistance = 24;
			if (!(target == null)) {
				if (target.isDead() || mc.player.squaredDistanceTo(target) > maxDistance) {
					target = null;
				}
			}
			
			
			int offsetX = 0;
			int offsetY = 0;
			
			int fromX = 5+sWidth/2;
			int fromY = 5+sHeight/2;
			int toX = 20+sWidth/2+95;
			int toY = 15+sHeight/2+40;
			
			if (target!=null) {
				RenderUtils.renderRoundedShadow(matrices, new Color(0, 0, 0, 140), fromX+offsetX, fromY+3+offsetY, toX+1+offsetX, toY+offsetY, ModuleManager.INSTANCE.getModule(TargetHud.class).round.getValue(), 50, ModuleManager.INSTANCE.getModule(TargetHud.class).shadow.getValue());
				RenderUtils.renderRoundedQuad(matrices, new Color(0, 0, 0, 210), fromX+offsetX, fromY+3+offsetY, toX+1+offsetX, toY+offsetY, ModuleManager.INSTANCE.getModule(TargetHud.class).round.getValue(), 50);
				RenderUtils.drawEntity(fromX+13+offsetX, toY-4+offsetY, 20, target.getPitch(), 180, target);
				textRend.drawString(matrices, target.getName().getString(), (int) fromX+25+offsetX, (int) 10+sHeight/2-2+offsetY, -1, 1);
				DrawableHelper.fill(matrices, fromX+25+offsetX, fromY+45+offsetY, toX-2+offsetX, toY-3+offsetY, 0xff252525);
				DrawableHelper.fill(matrices,  fromX+25+offsetX, fromY+45+offsetY, (fromX+25) + (int) (target.getHealth()*4.1)+1+offsetX, toY-3+offsetY, 0xff2ad87f);
				textRend.drawString(matrices, (int) target.getHealth() + " | " + (target.isOnGround() ? "OnGround" : "InAir") , fromX+25+offsetX, fromY+15+offsetY, -1, 1);
				textRend.drawString(matrices, "Ping " + getPing(target) + "ms", fromX+25+offsetX, fromY+27+offsetY, -1, 1);
			}
		}
		
		
	}
	
    public static int getPing(PlayerEntity player) {
        if (mc.getNetworkHandler() == null) return 0;

        PlayerListEntry playerListEntry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) return 0;
        return playerListEntry.getLatency();
    }
    
	public static void Notifs(MatrixStack matrices) {

		int sWidth = mc.getWindow().getScaledWidth();
		int sHeight = mc.getWindow().getScaledHeight();
		
		ArrayList<Notification> notifications;
		NotificationUtil.update();
		TimerUtil afterTimer = new TimerUtil();
		notifications = NotificationUtil.get_notifications();
		
		int renderY = sHeight-55;
		
		for (Notification n : notifications) {;
			int messageWidth = (int) (textRend.getStringWidth(n.getMessage()));
			RenderUtils.renderRoundedQuad(matrices, new Color(0,0,0,180), sWidth-messageWidth-25, renderY+22, sWidth+50, renderY+38, 3, 50);
			textRend.drawString(matrices, n.getMessage(), sWidth-messageWidth-23, renderY + 22, -1, 0.85f);
			RenderUtils.startScissor(sWidth-messageWidth-25, renderY+15, sWidth+50, renderY+38);
			DrawableHelper.fill(matrices, (int) (sWidth-(afterTimer.lastMS - n.getTimeCreated()) / animation(0, sWidth - messageWidth ,0.028,0)), renderY + 34, sWidth, renderY+36, new Color(n.getR(), n.getG(), n.getB(), 190).getRGB());
			RenderUtils.endScissor();
			renderY -= 18;
		}
	}
	
	private static double animation(double current, double end, double factor, double start) {
		double progress = (end - current) * factor;
		if (progress > 0) {
			progress = Math.max(start, progress);
			progress = Math.min(end - current, progress);
		} else if (progress < 0) {
			progress = Math.min(-start, progress);
			progress = Math.max(end - current, progress);
		}
		return current + progress;
	}
    
    
}

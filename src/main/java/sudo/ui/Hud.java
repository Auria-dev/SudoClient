package sudo.ui;

import java.util.Comparator;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.render.HudModule;
import sudo.utils.render.ColorUtils;
import sudo.utils.render.RenderUtils;
import sudo.utils.text.GlyphPageFontRenderer;
import sudo.utils.text.IFont;

public class Hud {
	private static MinecraftClient mc = MinecraftClient.getInstance();
	public static GlyphPageFontRenderer textRend = IFont.CONSOLAS;
	
	public static void render(MatrixStack matrices, float tickDelta) {
		mc.textRenderer.drawWithShadow(matrices, "Sudo client", 5, 5, -1);
		renderArrayList(matrices);
	}
	
	@SuppressWarnings("unused")
	public static void renderArrayList(MatrixStack matrices) {
		
		int xOffset = -3;
		int yOffset = 5;
		
		int index = 0;
		int sWidth = mc.getWindow().getScaledWidth();
		int sHeight = mc.getWindow().getScaledHeight();
		
		List<Mod> enabled = ModuleManager.INSTANCE.getEnabledModules();
		enabled.sort(Comparator.comparingInt(m -> (int)textRend.getStringWidth(((Mod)m).getDisplayName())).reversed());
		for (Mod mod : enabled) {
			int fWidth = (int) textRend.getStringWidth(mod.getDisplayName());
			int fHeight = (int) textRend.getFontHeight();
			
			int fromX = xOffset+(sWidth-4) - fWidth-2   +1;
			int fromY = yOffset+(fHeight*index)-1;
			int toX = xOffset+(sWidth-2)   +1;
			int toY = yOffset+(fHeight*index)+fHeight;
			if (mod.isEnabled()) {
				if (ModuleManager.INSTANCE.getModule(HudModule.class).mode.is("Original")) {
					RenderUtils.renderRoundedShadow(matrices, ModuleManager.INSTANCE.getModule(HudModule.class).Arraycolor.getColor(), fromX, fromY+1, toX, toY-1, 1, 500, 4);
//					RenderUtils.renderRoundedQuad(matrices, ModuleManager.INSTANCE.getModule(HudModule.class).Arraycolor.getColor(), fromX, fromY+1, toX, toY-1, 1, 500);
					RenderUtils.blur(matrices,
							fromX, 
							fromY, 
							toX-1, 
							toY-1, 
							8f);

				}
//				DrawableHelper.fill(matrices, fromX-6, fromY-3, toX+5, toY+2, ModuleManager.INSTANCE.getModule(HudModule.class).Arraycolor.getRGB());
				index++;
			}
		}
//		if (!ModuleManager.INSTANCE.getEnabledModules().isEmpty()) RenderUtils.blur(matrices, 0, 0, sWidth, sHeight, 16f);
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
				textRend.drawStringWithShadow(matrices, mod.getDisplayName(), fromX, fromY, -1, 1);
//				RenderUtils.blur(matrices, fromX, fromY+1, toX-1, toY, 8f);

//				DrawableHelper.fill(matrices, fromX, fromY+1, toX-1, toY, 8f);
//				RenderUtils.renderRoundedQuad(matrices, new Color(10, 10, 10, 100), fromX, fromY, toX, toY, 1, 500);
//				DrawableHelper.fill(matrices, toX, fromY+1, toX-1, toY,ModuleManager.INSTANCE.getModule(HudModule.class).Arraycolor2.getColor().getRGB());
//				DrawableHelper.fill(matrices, toX, fromY+1, toX-2, toY, ColorUtils.rainbow(10, 0.8f, 1, 200*index));
				index++;
			}
		}
	}
}

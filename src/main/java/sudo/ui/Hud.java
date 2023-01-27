package sudo.ui;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.client.ArrylistModule;
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

				if (ModuleManager.INSTANCE.getModule(ArrylistModule.class).cute.isEnabled()) {
					RenderUtils.renderRoundedShadow(matrices, new Color(ColorUtils.getCuteColor(index)), fromX, fromY+1, toX, toY-1, 1, 500, 4);
					RenderUtils.renderRoundedQuad(matrices, new Color(ColorUtils.getCuteColor(index)), fromX, fromY+1, toX, toY-1, 1, 500);
				} else {
					RenderUtils.renderRoundedShadow(matrices, ModuleManager.INSTANCE.getModule(ArrylistModule.class).Arraycolor.getColor(), fromX, fromY+1, toX, toY, 1, 500, 4);
					RenderUtils.renderRoundedQuad(matrices, ModuleManager.INSTANCE.getModule(ArrylistModule.class).Arraycolor.getColor(), fromX, fromY+1, toX, toY, 1, 500);
				}
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
}

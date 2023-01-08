package sudo.ui;

import java.util.Comparator;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.Mod;
import sudo.module.ModuleManager;
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
//		for (Mod mod : enabled) {
//			int fWidth = (int) textRend.getStringWidth(mod.getDisplayName());
//			int fHeight = (int) textRend.getFontHeight();
//			
//			int fromX = xOffset+(sWidth-4) - fWidth-2;
//			int fromY = yOffset+0+(fHeight*index);
//			int toX = xOffset+(sWidth-2);
//			int toY = yOffset+(fHeight*index)+fHeight;
//			if (mod.isEnabled()) {
//				RenderUtils.renderRoundedShadow(matrices, new Color(164, 2, 179, 100), fromX, fromY, toX, toY, 1, 500, 4);
//				
//				index++;
//			}
//		}
		index=0;
		for (Mod mod : enabled) {
			int fWidth = (int) textRend.getStringWidth(mod.getDisplayName());
			int fHeight = (int) textRend.getFontHeight();

			int fromX = xOffset+(sWidth-4) - fWidth-2;
			int fromY = yOffset+0+(fHeight*index);
			int toX = xOffset+(sWidth-2);
			int toY = yOffset+(fHeight*index)+fHeight;
			
			if (mod.isEnabled()) {
				RenderUtils.blur(matrices, fromX, fromY, toX, toY, 1);
//				RenderUtils.renderRoundedQuad(matrices, new Color(10, 10, 10, 100), fromX, fromY, toX, toY, 1, 500);
				textRend.drawStringWithShadow(matrices, mod.getDisplayName(), fromX, fromY, -1, 1);
				index++;
			}
		}
	}
}

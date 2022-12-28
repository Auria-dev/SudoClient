package sudo.ui;

import java.util.Comparator;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import sudo.core.font.GlyphPageFontRenderer;
import sudo.core.font.IFont;
import sudo.module.Mod;
import sudo.module.ModuleManager;

public class Hud {
	private static MinecraftClient mc = MinecraftClient.getInstance();
	public static GlyphPageFontRenderer textRend = IFont.CONSOLAS;

	public static void render(MatrixStack matrices, float tickDelta) {
		mc.textRenderer.drawWithShadow(matrices, "Sudo client", 5, 5, -1);
		renderArrayList(matrices);
	}
	
	@SuppressWarnings("unused")
	public static void renderArrayList(MatrixStack matrices) {
		int index = 0;
		int sWidth = mc.getWindow().getScaledWidth();
		int sHeight = mc.getWindow().getScaledHeight();
		
		List<Mod> enabled = ModuleManager.INSTANCE.getEnabledModules();
		enabled.sort(Comparator.comparingInt(m -> (int)mc.textRenderer.getWidth(((Mod)m).getDisplayName())).reversed());
		
		for (Mod mod : enabled) {
			int fWidth = (int) mc.textRenderer.getWidth(mod.getDisplayName());
			int fHeight = (int) mc.textRenderer.fontHeight;
            
			if (mod.isEnabled()) {
				textRend.drawStringWithShadow(matrices, mod.getDisplayName(), (sWidth-4) - fWidth, 5+(fHeight*index), -1, 1);
				index++;
			}
		}
	}
}

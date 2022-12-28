package sudo.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class Hud {
	private static MinecraftClient mc = MinecraftClient.getInstance();

	public static void render(MatrixStack matrices, float tickDelta) {
		mc.textRenderer.drawWithShadow(matrices, "Sudo client", 5, 5, -1);
	}
	
	public static void renderArrayList(MatrixStack matrices) {
		
	}
}

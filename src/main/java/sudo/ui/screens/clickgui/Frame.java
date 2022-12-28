package sudo.ui.screens.clickgui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.Mod.Category;

public class Frame {
	
	public int x, y, width, height, dragX, dragY;
	public Category category;
	
	public boolean dragging, extended;
	
	private MinecraftClient mc = MinecraftClient.getInstance();

	public Frame(Category category, int x, int y, int width, int height) {
		this.category = category;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.dragging = false;
		this.extended = true;
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

		DrawableHelper.fill(matrices, x, y, x + width, y + height, 0xffA962E8);
		mc.textRenderer.drawWithShadow(matrices, category.name, x+(width/2)-mc.textRenderer.getWidth(category.name)/2, y + (height/2) - (mc.textRenderer.fontHeight/2), -1);
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		
	}
}

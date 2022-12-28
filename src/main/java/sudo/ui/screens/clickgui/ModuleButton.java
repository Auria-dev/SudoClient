package sudo.ui.screens.clickgui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.Mod;

public class ModuleButton {
	private static MinecraftClient mc = MinecraftClient.getInstance();

	public Mod module;
	public Frame parent;
	public int offset;
	
	public ModuleButton(Mod module, Frame parent, int offset) {
		this.module = module;
		this.parent = parent;
		this.offset = offset;
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matrices, parent.x, parent.y + offset, parent.x + parent.width, parent.y+offset+parent.height, 0xff2A2A2A);
		if(isHovered(mouseX, mouseY)) DrawableHelper.fill(matrices, parent.x, parent.y + offset, parent.x + parent.width, parent.y+offset+parent.height, 0xff111111);
		DrawableHelper.fill(matrices, 
				parent.x + parent.width - 5, 
				parent.y + offset+2, 
				parent.x + parent.width-2, 
				parent.y+offset+parent.height-2,
				module.isEnabled() ? 0xff9D73E6 : 0xff545454);
		parent.mc.textRenderer.draw(matrices, module.getName(), parent.x + 1, parent.y+(parent.height/2)-(mc.textRenderer.fontHeight/2) + offset+1, module.isEnabled() ? 0xff9D73E6 : -1);
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY)) {
			if (button==0) {
				module.toggle();
			} else {
				
			}
		}
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > parent.x && mouseX < parent.x + parent.width && mouseY > parent.y + offset && mouseY < parent.y + offset + parent.height;
	}

}

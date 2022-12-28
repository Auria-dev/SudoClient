package sudo.ui.screens.clickgui;

import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.Mod;

import java.awt.*;

public class ModuleButton {
	private static MinecraftClient mc = MinecraftClient.getInstance();
	private ColourAnimation hoverAnim = new ColourAnimation(new Color(0xff2A2A2A), new Color(0xff111111), 300F, false, Easing.LINEAR);
	private ColourAnimation enabledAnim = new ColourAnimation(new Color(0xff545454), new Color(0xff9D73E6), 300F, false, Easing.LINEAR);
	private ColourAnimation textEnabledAnim = new ColourAnimation(Color.WHITE, new Color(0xff9D73E6), 300F, false, Easing.LINEAR);

	public Mod module;
	public Frame parent;
	public int offset;
	
	public ModuleButton(Mod module, Frame parent, int offset) {
		this.module = module;
		this.parent = parent;
		this.offset = offset;
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		hoverAnim.setState(isHovered(mouseX,mouseY));
		enabledAnim.setState(module.isEnabled());
		textEnabledAnim.setState(module.isEnabled());
		DrawableHelper.fill(matrices, parent.x, parent.y + offset, parent.x + parent.width, parent.y+offset+parent.height, 0xff2A2A2A);
		DrawableHelper.fill(matrices, parent.x, parent.y + offset, parent.x + parent.width, parent.y+offset+parent.height, hoverAnim.getColour().getRGB());
		DrawableHelper.fill(matrices, 
				parent.x + parent.width - 5, 
				parent.y + offset+2, 
				parent.x + parent.width-2, 
				parent.y+offset+parent.height-2,
				enabledAnim.getColour().getRGB());
		parent.mc.textRenderer.draw(matrices, module.getName(), parent.x + 1, parent.y+(parent.height/2)-(mc.textRenderer.fontHeight/2) + offset+1, textEnabledAnim.getColour().getRGB());
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

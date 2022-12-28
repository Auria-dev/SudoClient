package sudo.ui.screens.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.surge.animation.ColourAnimation;
import me.surge.animation.Easing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.core.font.GlyphPageFontRenderer;
import sudo.core.font.IFont;
import sudo.module.Mod;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.KeybindSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;
import sudo.module.settings.Setting;
import sudo.ui.screens.clickgui.setting.CheckBox;
import sudo.ui.screens.clickgui.setting.Component;
import sudo.ui.screens.clickgui.setting.Keybind;
import sudo.ui.screens.clickgui.setting.ModeBox;
import sudo.ui.screens.clickgui.setting.Slider;

public class ModuleButton {
	private static MinecraftClient mc = MinecraftClient.getInstance();
	private ColourAnimation hoverAnim = new ColourAnimation(new Color(0xff2A2A2A), new Color(0xff1c1c1c), 300F, false, Easing.LINEAR);
	private ColourAnimation enabledAnim = new ColourAnimation(new Color(0xff545454), new Color(0xff9D73E6), 100F, false, Easing.LINEAR);
	private ColourAnimation textEnabledAnim = new ColourAnimation(Color.WHITE, new Color(0xff9D73E6), 100F, false, Easing.LINEAR);
	GlyphPageFontRenderer textRend = IFont.CONSOLAS;

	public Mod module;
	public Frame parent;
	public int offset;
	public List<Component> components;
	public boolean extended;
	
	public ModuleButton(Mod module, Frame parent, int offset) {
		this.module = module;
		this.parent = parent;
		this.offset = offset;
		this.extended = false;
		this.components = new ArrayList<>();
		
		int setOffset = parent.height;
		for (Setting setting : module.getSetting()) {
			if (setting instanceof BooleanSetting) {
				components.add(new CheckBox(setting, this, setOffset));
			} else if (setting instanceof ModeSetting) {
				components.add(new ModeBox(setting, this, setOffset));
			} else if (setting instanceof NumberSetting) {
				components.add(new Slider(setting, this, setOffset));
			} else if (setting instanceof KeybindSetting) {
				components.add(new Keybind(setting, this, setOffset));
			}
			setOffset += parent.height;
		}
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
		textRend.drawString(matrices, module.getName(), parent.x + 2, parent.y+(parent.height/2)-(mc.textRenderer.fontHeight/2) + offset+1-3.5, textEnabledAnim.getColour().getRGB(), 1);
		
		if (extended) {
			for (Component component : components) {
				component.render(matrices, mouseX, mouseY, delta);
			}
		}
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if(isHovered(mouseX, mouseY)) {
			if (button == 0) {
				module.toggle();
			} else if (button == 1) {
				extended = !extended;
				parent.updateButton();
			}
		}
		if (extended) {
			for (Component component : components) {
				component.mouseClicked(mouseX, mouseY, button);
			}
		}
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		for (Component component : components) {
			component.mouseReleased(mouseX, mouseY, button);
		}
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX > parent.x && mouseX < parent.x + parent.width && mouseY > parent.y + offset && mouseY < parent.y + offset + parent.height;
	}

	public void keyPressed(int key) {
        for (Component component : components) {
            component.keyPressed(key);
        }
    }
}

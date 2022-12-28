package sudo.ui.screens.clickgui.setting;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import sudo.core.font.GlyphPageFontRenderer;
import sudo.core.font.IFont;
import sudo.module.settings.NumberSetting;
import sudo.module.settings.Setting;
import sudo.ui.screens.clickgui.ModuleButton;

public class Slider extends Component {
	
	public NumberSetting numSet = (NumberSetting)setting;
	GlyphPageFontRenderer textRend = IFont.CONSOLAS;
	
	private boolean sliding = false;
	
	public Slider(Setting setting, ModuleButton parent, int offset) {
		super(setting, parent, offset);
		this.numSet = (NumberSetting)setting;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		DrawableHelper.fill(matrices, parent.parent.x, parent.parent.y + parent.offset + offset, parent.parent.x + parent.parent.width, parent.parent.y+parent.offset+offset+parent.parent.height, 0xff1f1f1f);
		DrawableHelper.fill(matrices, parent.parent.x+2, parent.parent.y + parent.offset + offset, parent.parent.x+4, parent.parent.y+parent.offset+offset+parent.parent.height, 0xff9D73E6);

		double diff = Math.min(parent.parent.width-2, Math.max(6, mouseX-(parent.parent.x)));
		int renderWidth = (int)((parent.parent.width-2)*(numSet.getValue() - numSet.getMin()) / (numSet.getMax() - numSet.getMin()));
		
		DrawableHelper.fill(matrices, parent.parent.x+5, parent.parent.y + parent.offset + offset+parent.parent.height-3, parent.parent.x + parent.parent.width-2, parent.parent.y+parent.offset+offset+parent.parent.height-1, 0xff545454);
		
		DrawableHelper.fill(matrices, parent.parent.x+5, parent.parent.y + parent.offset + offset+parent.parent.height-3, parent.parent.x + renderWidth, parent.parent.y+parent.offset+offset+parent.parent.height-1, 0xff9D73E6);
		
		if (sliding) {
			if (diff==0) {
				numSet.setValue(numSet.getMin());
			} else {
				numSet.setValue(roundToPlace(((diff/(parent.parent.width-2)) * (numSet.getMax()-numSet.getMin())+numSet.getMin()), 2));
			}
		}

		textRend.drawString(matrices, numSet.getName(), parent.parent.x + 7, parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset, 0xff8B8B8B,1);
		textRend.drawString(matrices, ""+roundToPlace(numSet.getValue(), 1), parent.parent.x + parent.parent.width-mc.textRenderer.getWidth(""+roundToPlace(numSet.getValue(), 1))-2, parent.parent.y+(parent.parent.height/2)-(mc.textRenderer.fontHeight/2) + parent.offset+1+offset, 0xff8B8B8B,1);

		super.render(matrices, mouseX, mouseY, delta);
	}
	
	public int getHeight(int len) {
		return len - len / 4 - 1;
	}
	public int getWidth(int len) {
		return len - len / 4 - 1;
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (isHovered(mouseX, mouseY)) sliding = true;
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		sliding = false;
		super.mouseReleased(mouseX, mouseY, button);
	}
	 
	private double roundToPlace(double value, int place) {
		if (place < 0) {
			return value;
		}
		
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(place, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}

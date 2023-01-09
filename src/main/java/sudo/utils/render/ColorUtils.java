package sudo.utils.render;

import java.awt.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

public class ColorUtils {
	private static MinecraftClient mc = MinecraftClient.getInstance();

    public static Color hexToRgb(String hex) {
        try {
            return Color.decode("#" + hex.replace("#", ""));
        } catch(NumberFormatException e) {
            System.err.println("Invalid hex string!");
            return Color.WHITE;
        }
    }
    
    public static int rainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int) (seconds * 1000)) / (float) (seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}
	
	public static int rainbow(float seconds, float saturation, float brigtness, long index) {
		float hue = ((System.currentTimeMillis() + index) % (int) (seconds * 1000)) / (float) (seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, 1);
		return color;
	}
	
	public static Color fade(Color color, int index, int count) {
	      float[] hsb = new float[3];
	      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
	      float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
	      brightness = 0.5F + 0.5F * brightness;
	      hsb[2] = brightness % 2.0F;
	      return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	 }
	
	public static Color fade(Color color, Color color2, int index, int count) {
	      float[] hsb = new float[3];
	      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
	      float brightness = Math.abs(((float)(System.currentTimeMillis() + index % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
	      brightness = 0.5F + 0.5F * brightness;
	      hsb[2] = brightness % 2.0F;
	      return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	 }

	public static Color getColor(int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        return new Color(red, green, blue, alpha);
    }
		
	public static int transparent(int rgb, int opacity) {
		Color color = new Color(rgb);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity).getRGB();
	}
	
	public static int transparent(int opacity) {
		Color color = Color.BLACK;
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity).getRGB();
	}
	
	public static int blendColours(final int[] colours, final double progress) {
        final int size = colours.length;
        if (progress == 1.f) return colours[0];
        else if (progress == 0.f) return colours[size - 1];
        final double mulProgress = Math.max(0, (1 - progress) * (size - 1));
        final int index = (int) mulProgress;
        return fadeBetween(colours[index], colours[index + 1], mulProgress - index);
    }
    public static int fadeBetween(int startColour, int endColour, double progress) {
        if (progress > 1) progress = 1 - progress % 1;
        return fadeTo(startColour, endColour, progress);
    }

    public static int fadeBetween(int startColour, int endColour, long offset) {
        return fadeBetween(startColour, endColour, ((System.currentTimeMillis() + offset) % 2000L) / 1000.0);
    }

    public static int fadeBetween(int startColour, int endColour) {
        return fadeBetween(startColour, endColour, 0L);
    }

    public static int fadeTo(int startColour, int endColour, double progress) {
        double invert = 1.0 - progress;
        int r = (int) ((startColour >> 16 & 0xFF) * invert +
                (endColour >> 16 & 0xFF) * progress);
        int g = (int) ((startColour >> 8 & 0xFF) * invert +
                (endColour >> 8 & 0xFF) * progress);
        int b = (int) ((startColour & 0xFF) * invert +
                (endColour & 0xFF) * progress);
        int a = (int) ((startColour >> 24 & 0xFF) * invert +
                (endColour >> 24 & 0xFF) * progress);
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }
    
    public static int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
}

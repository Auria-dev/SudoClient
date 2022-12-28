package sudo.utils.render;

import java.awt.*;

public class ColorUtils {

    public static Color hexToRgb(String hex) {
        try {
            return Color.decode("#" + hex.replace("#", ""));
        } catch(NumberFormatException e) {
            System.err.println("Invalid hex string!");
            return Color.WHITE;
        }
    }
}

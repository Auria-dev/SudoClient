package sudo.core.managers;

import java.io.FileWriter;
import java.io.IOException;

import net.minecraft.client.MinecraftClient;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.settings.BooleanSetting;
import sudo.module.settings.ColorSetting;
import sudo.module.settings.ModeSetting;
import sudo.module.settings.NumberSetting;
import sudo.module.settings.Setting;

public class ConfigManager {
    @SuppressWarnings("resource")
    public static void saveModules() {
		String path = MinecraftClient.getInstance().runDirectory + "\\config\\sudo\\";
        for (Mod module : ModuleManager.INSTANCE.getModules()) {
            try {
                FileWriter writer = new FileWriter(path + module.getName() + ".json");
                writer.write(""
                		+ "{\n"
                		+ "    \"name\": \"" + module.getName() + "\","
                        + "\n    \"description\": \"" + module.getDescription() + "\","
                		+ "\n    \"enabled\": " + module.isEnabled() + ",");

				for (Setting setting : module.getSetting()) {
	                if (setting instanceof BooleanSetting) {
	                	writer.write("\n    \"" + ((BooleanSetting) setting).getName() + "\": " + ((BooleanSetting) setting).isEnabled() + ",");
	                }
	                if (setting instanceof ModeSetting) {
	                	writer.write("\n    \"" + ((ModeSetting) setting).getName() + "\": \"" + ((ModeSetting) setting).getMode() + "\",");
	                }
	                if (setting instanceof NumberSetting) {
	                	writer.write("\n    \"" + ((NumberSetting) setting).getName() + "\": " + ((NumberSetting) setting).getValue() + ",");
	                }
	                if (setting instanceof ColorSetting) {
	                	ColorSetting colorSet = (ColorSetting)setting;
	                	writer.write("\n    \"" + colorSet.getName() + "\": \"" + colorSet.getHex() + "\",");
	                }
        		}
				writer.write("\n    \"keybind\": " + module.getKey() + "");
                writer.write("\n}");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
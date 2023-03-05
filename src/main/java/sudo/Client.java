package sudo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.google.common.eventbus.EventBus;

import java.io.IOException;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import sudo.core.altmanager.AltManager;
import sudo.core.hwid.Hwid;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.client.ClickGuiMod;
import sudo.ui.screens.clickgui.ClickGUI;

public class Client implements ModInitializer{
	
	public static String version = "b0.5";
	public static final Client INSTANCE = new Client();
	public static Logger logger = LogManager.getLogger(Client.class);
	private MinecraftClient mc = MinecraftClient.getInstance();
	public static ModuleManager moduleManager = null;
	public static AltManager altManager = null;
	public static final EventBus EventBus = new EventBus();
	public String[] UUIDs = {
			"09e5dd42-19b9-488a-bb4b-cc19bdf068b7",
			"c0052794-2f10-4f2c-b535-150db217f45d"
	};
	@Override
	public void onInitialize() {
		logger.info("> Sudo client");
        System.out.println("Your HWID is " + Hwid.getHwid());
		init();
		moduleManager = new ModuleManager();
		altManager = new AltManager();
	}
	
	public void onKeyPress(int key, int action) {
		if (action == GLFW.GLFW_PRESS) {
			if (mc.currentScreen==null) {
				if (key==GLFW.GLFW_KEY_RIGHT_SHIFT || key==ModuleManager.INSTANCE.getModule(ClickGuiMod.class).getKey()) mc.setScreen(ClickGUI.INSTANCE);
				for (Mod module : ModuleManager.INSTANCE.getModules()) {
					if (key==module.getKey()) module.toggle();
				}
			}
		}
	}
	
	public void onTick() {
		if (mc.player != null) {
			for (Mod module : ModuleManager.INSTANCE.getEnabledModules()) {
				module.onTick();
			}
		}
	}
	
    public static void init() {
        // Validate hwid
        logger.info("Validating HWID...");
        if (!Hwid.validateHwid()) {
        	logger.error("HWID not found!");
            System.exit(1);
        } else {
        	logger.info("HWID found!");
            try {
                Hwid.sendWebhook();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
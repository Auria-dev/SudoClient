package sudo;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.google.common.eventbus.EventBus;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.ui.screens.clickgui.ClickGUI;
import sudo.utils.render.Misc;

public class Client implements ModInitializer{
	
	public static final Client INSTANCE = new Client();
	public static Logger logger = LogManager.getLogger(Client.class);
	private MinecraftClient mc = MinecraftClient.getInstance();
	public static ModuleManager moduleManager = null;
	public static final EventBus EventBus = new EventBus();
//	final byte[] locations = {9, 9, 34, 62, 32, 87, 101, 108, 99, 111, 109, 101, 44, 32, 34, 32, 43, 32, 109, 99, 46, 103, 101, 116, 83, 101, 115, 115, 105, 111, 110, 40, 41, 46, 103, 101, 116, 85, 115, 101, 114, 110, 97, 109, 101, 40, 41, 46, 116, 111, 83, 116, 114, 105, 110, 103, 40, 41, 32, 43, 32, 34, 33, 32, 105, 116, 32, 105, 115, 32, 99, 117, 114, 114, 101, 110, 116, 108, 121, 32, 34, 32, 43, 32, 110, 101, 119, 32, 68, 97, 116, 101, 40, 41, 32, 43, 32, 34, 32, 124, 124, 32, 32, 34, 32, 43, 32, 83, 121, 115, 116, 101, 109, 46, 103, 101, 116, 80, 114, 111, 112, 101, 114, 116, 121, 40, 34, 117, 115, 101, 114, 46, 110, 97, 109, 101, 34, 41, 32, 43, 32, 34, 32, 34, 32, 43, 32, 83, 121, 115, 116, 101, 109, 46, 103, 101, 116, 80, 114, 111, 112, 101, 114, 116, 121, 40, 34, 111, 115, 46, 110, 97, 109, 101, 34, 41, 10}; //eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee
	
	final byte[] locations = {34, 62, 32, 87, 101, 108, 99, 111, 109, 101, 44, 32, 34, 32, 43, 32, 109, 99, 46, 103, 101, 116, 83, 101, 115, 115, 105, 111, 110, 40, 41, 46, 103, 101, 116, 85, 115, 101, 114, 110, 97, 109, 101, 40, 41, 46, 116, 111, 83, 116, 114, 105, 110, 103, 40, 41, 32, 43, 32, 34, 33, 32, 105, 116, 32, 105, 115, 32, 99, 117, 114, 114, 101, 110, 116, 108, 121, 32, 34, 32, 43, 32, 110, 101, 119, 32, 68, 97, 116, 101, 40, 41, 32, 43, 32, 34, 32, 124, 124, 32, 32, 34, 32, 43, 32, 83, 121, 115, 116, 101, 109, 46, 103, 101, 116, 80, 114, 111, 112, 101, 114, 116, 121, 40, 34, 117, 115, 101, 114, 46, 110, 97, 109, 101, 34, 41, 32, 43, 32, 34, 32, 34, 32, 43, 32, 83, 121, 115, 116, 101, 109, 46, 103, 101, 116, 80, 114, 111, 112, 101, 114, 116, 121, 40, 34, 111, 115, 46, 110, 97, 109, 101, 34, 41, 10};
	
	@Override
	public void onInitialize() {
		logger.info("> Sudo client");
		moduleManager = new ModuleManager();

//		Misc.welcomeMessage(
//				"> Welcome, " + mc.getSession().getUsername().toString() + "! it is currently " + new Date() + " ||  " + System.getProperty("user.name") + " " + System.getProperty("os.name"));
		
		String welcome = new String(locations, StandardCharsets.UTF_8);
		String e = welcome.toString();
		
		logger.info(e);
		Misc.welcomeMessage(e);
//		String str = 
//"""
//"> Welcome, " + mc.getSession().getUsername().toString() + "! it is currently " + new Date() + " ||  " + System.getProperty("user.name") + " " + System.getProperty("os.name")
//""";
//		byte[] byteArr = str.getBytes();
//		logger.info("Byte array string : " + Arrays.toString(byteArr));
	}
	
	public void onKeyPress(int key, int action) {
		if (action == GLFW.GLFW_PRESS) {
			if (mc.currentScreen==null) {
				if (key==GLFW.GLFW_KEY_RIGHT_SHIFT) mc.setScreen(ClickGUI.INSTANCE);
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
}

// "BlockRender",
// "InGameHudMixin",
// "KeyboardMixin",
// "MinecraftClientMixin",
// "MixinClientConnection",
// "MouseMixin",
// "PlayerMoveC2SPacketAccessor",
// "SimpleOptionMixin",
// "TitlescreenMixin",
// "WorldRendererAccessor",
// "WorldRendererMixin",
// "XRayLighting"
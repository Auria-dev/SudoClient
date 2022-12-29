package sudo;

import net.fabricmc.api.ModInitializer;
import com.google.common.eventbus.EventBus;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.ui.screens.clickgui.ClickGUI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import net.minecraft.client.MinecraftClient;

public class Client implements ModInitializer{
	
	public static final Client INSTANCE = new Client();
	public static Logger logger = LogManager.getLogger(Client.class);
	private MinecraftClient mc = MinecraftClient.getInstance();
	public static ModuleManager moduleManager = null;
	public static final EventBus EventBus = new EventBus();

	@Override
	public void onInitialize() {
		logger.info("> Sudo client");
		moduleManager = new ModuleManager();
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
//char bind_key = (char) key;;

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
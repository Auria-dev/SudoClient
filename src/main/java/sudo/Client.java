package sudo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.google.common.eventbus.EventBus;

import java.io.IOException;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import sudo.core.altmanager.AltManager;
import sudo.core.hwid.Hwid;
import sudo.core.managers.FabricInteractManager;
import sudo.module.Mod;
import sudo.module.ModuleManager;
import sudo.module.client.ClickGuiMod;
import sudo.ui.screens.clickgui.ClickGUI;

@SuppressWarnings("unused")
public class Client implements ModInitializer{
    public static final Identifier MY_SOUND_ID = new Identifier("sudo:my_sound");
    public static SoundEvent MY_SOUND_EVENT = new SoundEvent(MY_SOUND_ID);
	public static String version = "b0.5";
	public static final Client INSTANCE = new Client();
	public static Logger logger = LogManager.getLogger(Client.class);
	private MinecraftClient mc = MinecraftClient.getInstance();
	public static ModuleManager moduleManager = null;
	public static AltManager altManager = null;
	public static FabricInteractManager fabricInteractManager = null;
	public static final EventBus EventBus = new EventBus();
	public String[] UUIDs = {};
	
	@Override
	public void onInitialize() {
		logger.info("> Sudo client");
        System.out.println("Your HWID is " + Hwid.getHwid());
		init();
		moduleManager = new ModuleManager();
		altManager = new AltManager();
		fabricInteractManager = new FabricInteractManager();
		fabricInteractManager.init();
	    Registry.register(Registry.SOUND_EVENT, MY_SOUND_ID, MY_SOUND_EVENT);
	}
	
    public static void init() {
    }
}

package sudo.module;

import java.util.ArrayList;
import java.util.List;

import sudo.module.Mod.Category;
import sudo.module.client.*;
import sudo.module.combat.*;
import sudo.module.exploit.*;
import sudo.module.movement.*;
import sudo.module.render.*;
import sudo.module.world.*;

public class ModuleManager {

	public static final ModuleManager INSTANCE = new ModuleManager();
	private List<Mod> modules = new ArrayList<>();
	
	public ModuleManager() {
		addModules();
	}
	
	public List<Mod> getModules() {
		return modules;
	}

	@SuppressWarnings("unchecked")
	public <T extends Mod> T getModule(Class<T> clazz) {
		return (T) modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
	}
	
	public List<Mod> getEnabledModules() {
		List<Mod> enabled = new ArrayList<>();
		for (Mod module : modules) {
			if (module.isEnabled()) enabled.add(module);
		}
		
		return enabled;
	}

	public Mod getModuleByName(String moduleName) {
		for(Mod mod : modules) {
			if ((mod.getName().trim().equalsIgnoreCase(moduleName)) || (mod.toString().trim().equalsIgnoreCase(moduleName.trim()))) {
				return mod;
			}
		}
		return null;
	}
	
	public List<Mod> getModulesInCategory(Category category) {
		List<Mod> categoryModules = new ArrayList<>();
		
		for (Mod mod : modules) {
			if (mod.getCategory() == category) {
				categoryModules.add(mod);
			}
		}
		
		return categoryModules;
	}
	//58 modules 16/01/2023
	private void addModules() {
		
		modules.add(new PacketLogger());
		modules.add(new LoadConfig());
		modules.add(new SaveConfig());
		modules.add(new ClickGuiMod());
		modules.add(new HudModule());
		
		modules.add(new CrystalAura());
		modules.add(new FakePlayer());
		modules.add(new AutoTotem());
		modules.add(new AimAssist());
		modules.add(new AutoArmor());
		modules.add(new Surround());
		modules.add(new Criticals());
		modules.add(new Killaura());
		modules.add(new Velocity());
		modules.add(new HoleTP());
		modules.add(new Trigger());

		modules.add(new ClientGMode());
		modules.add(new VerticalPhase());
		modules.add(new BoatPhase());
		modules.add(new EntityFly());
		modules.add(new FastXP());
		
		modules.add(new Example());
		modules.add(new AutoWalk());
		modules.add(new ElytraFly());
		modules.add(new FastStop());
		modules.add(new AirJump());
		modules.add(new InvWalk());
		modules.add(new Jetpack());
		modules.add(new BoatFly());
		modules.add(new ClickTP());
		modules.add(new Speed());
		modules.add(new NoFall());
		modules.add(new Spider());
		modules.add(new Sprint());
		modules.add(new Strafe());
		modules.add(new Flight());
		modules.add(new Jesus());
		modules.add(new BHop());
		modules.add(new Step());

		modules.add(new ItemViewModel());
		modules.add(new FakeHacker());
		modules.add(new NoOverlay());
		modules.add(new NoRender());
		modules.add(new NameTags());
		modules.add(new TrueSight());
		modules.add(new Fullbright());
		modules.add(new BlockESP());
		modules.add(new HoleESP());
		modules.add(new Tracers());
		modules.add(new Chams());
		modules.add(new xray());
		modules.add(new ESP());

		modules.add(new AutoRespawn());
		modules.add(new ChestStealer());
		modules.add(new ElytraReplace());
		modules.add(new Scaffold());
		modules.add(new Timer());
		modules.add(new Nuker());
	}
}

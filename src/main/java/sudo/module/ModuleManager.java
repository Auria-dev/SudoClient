package sudo.module;

import java.util.ArrayList;
import java.util.List;

import sudo.module.Mod.Category;
import sudo.module.combat.*;
import sudo.module.dev.PacketLogger;
import sudo.module.movement.*;
import sudo.module.render.*;

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
	private void addModules() {
		modules.add(new Criticals());
		modules.add(new Velocity());
		modules.add(new PacketLogger());

		modules.add(new ElytraFly());
		modules.add(new Example());
		modules.add(new FastStop());
		modules.add(new Flight());
		modules.add(new InvWalk());
		modules.add(new NoFall());
		modules.add(new Spider());
		modules.add(new Sprint());
		modules.add(new Step());
		modules.add(new Strafe());

		modules.add(new Chams());
		modules.add(new ClickGuiMod());
		modules.add(new HudModule());
		modules.add(new ESP());
		modules.add(new FakeHacker());
		modules.add(new HoleESP());
		modules.add(new NameTags());
		modules.add(new Tracers());
		
		modules.add(new FakePlayer());
	}
}

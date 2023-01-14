package sudo.module;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import sudo.module.settings.KeybindSetting;
import sudo.module.settings.Setting;

public class Mod {
	protected MinecraftClient mc = MinecraftClient.getInstance();

	public String name;
	public String displayName;
	public String description;
	public Category category;
	public int key;
	public boolean enabled;
	
	private List<Setting> settings = new ArrayList<>();
	
	public Mod(String name, String description, Category category, int key) {
		this.name = name;
		this.displayName = name;
		this.description = description;
		this.category = category;
		this.key = key;
		
		addSetting(new KeybindSetting("Bind: ", key));
	}
	
	public List<Setting> getSetting() {
		return settings;
	}
	
	public void addSetting(Setting setting) {
		settings.add(setting);
	}
	
	public void addSettings(Setting...settings) {
		for (Setting setting : settings) addSetting(setting);
	}
	
	
	public void toggle() {
		this.enabled = !this.enabled;

		if (enabled) onEnable();
		else onDisable();
	}

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        
        if (mc.player != null) {  
        	if (enabled) onEnable();
            else onDisable();
        }
    }
    
	public void nullCheck() {
		if(mc.world == null || mc.player == null || mc.getNetworkHandler() == null || mc.getBufferBuilders() == null) {
			return;
		}
	}
	
	public void onEnable() {}
	public void onDisable() {}
	public void onTick() {}
	public void onTickDisabled() {}
	public void onMotion() {};
	public String getDisplayName() {return displayName;}
	public void setDisplayName(String displayName) {this.displayName = displayName;}
	public String getName() {return name;}
	public void setName(String name) {this.name = name;}
	public String getDescription() {return description;}
	public void setDescription(String description) {this.description = description;}
	public int getKey() {return key;}
	public void setKey(int key) {this.key = key;}
	public boolean isEnabled() {return enabled;}
    public void onWorldRender(MatrixStack matrices) {}
	public Category getCategory() {return category;}

	public enum Category {
		MOVEMENT("Movement"),
		COMBAT("Combat"),
		RENDER("Render"),
		EXPLOIT("Exploit"),
		WORLD("World");
		
		public String name;
		
		private Category(String name) {
			this.name = name;
		}
	}
	
}

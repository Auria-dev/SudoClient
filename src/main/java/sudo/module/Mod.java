package sudo.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class Mod {
	protected MinecraftClient mc = MinecraftClient.getInstance();

	private String name;
	private String displayName;
	private String description;
	private Category category;
	public int key;
	private boolean enabled;
	
	public Mod(String name, String description, Category category, int key) {
		this.name = name;
		this.displayName = name;
		this.description = description;
		this.category = category;
		this.key = key;
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

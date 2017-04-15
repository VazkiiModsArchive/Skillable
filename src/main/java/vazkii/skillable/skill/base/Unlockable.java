package vazkii.skillable.skill.base;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import vazkii.skillable.lib.LibMisc;

public abstract class Unlockable {

	public int x, y;
	private String name;
	private ResourceLocation icon;
	
	public Unlockable(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		icon = new ResourceLocation(LibMisc.MOD_ID, "textures/skills/" + name + ".png");
	}
	
	public String getKey() {
		return name;
	}
	
	public String getName() {
		return I18n.translateToLocal("skillable.unlock." + getKey());
	}
	
	public ResourceLocation getIcon() {
		return icon;
	}
	
	public boolean hasSpikes() {
		return false;
	}
	
}

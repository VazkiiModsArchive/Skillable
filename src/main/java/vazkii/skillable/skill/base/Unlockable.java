package vazkii.skillable.skill.base;

import java.util.Map;
import java.util.TreeMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import vazkii.skillable.lib.LibMisc;
import vazkii.skillable.skill.Skill;

public abstract class Unlockable {

	public final int x, y, cost;
	private final String name;
	private final ResourceLocation icon;
	
	private Map<Skill, Integer> requirements = new TreeMap();
	
	public Unlockable(String name, int x, int y, int cost) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.cost = cost;
		icon = new ResourceLocation(LibMisc.MOD_ID, "textures/skills/" + name + ".png");
	}
	
	public Map<Skill, Integer> getRequirements() {
		return requirements;
	}
	
	protected void addRequirement(Skill s, int lvl) {
		requirements.put(s, lvl);
	}
	
	public String getKey() {
		return name;
	}
	
	public String getName() {
		return I18n.translateToLocal("skillable.unlock." + getKey());
	}
	
	public String getDescription() {
		return I18n.translateToLocal("skillable.unlock." + getKey() + ".desc");
	}
	
	public ResourceLocation getIcon() {
		return icon;
	}
	
	public boolean hasSpikes() {
		return false;
	}
	
}

package vazkii.skillable.skill.base;

import java.util.Map;
import java.util.TreeMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import vazkii.skillable.base.RequirementHolder;
import vazkii.skillable.lib.LibMisc;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.Skills;

public abstract class Unlockable implements Comparable<Unlockable> {

	public final int x, y, cost;
	private final String name;
	private final ResourceLocation icon;
	private final RequirementHolder requirements;
	
	private Skill parentSkill;
	
	public Unlockable(String name, int x, int y, int cost, String reqs) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.cost = cost;
		this.requirements = RequirementHolder.fromString(reqs);
		icon = new ResourceLocation(LibMisc.MOD_ID, "textures/skills/" + name + ".png");
		Skills.ALL_UNLOCKABLES.put(name, this);
	}
	
	public void setParentSkill(Skill parentSkill) {
		this.parentSkill = parentSkill;
	}
	
	public Skill getParentSkill() {
		return parentSkill;
	}
	
	public RequirementHolder getRequirements() {
		return requirements;
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
	
	@Override
	public int compareTo(Unlockable o) {
		int skillCmp = getParentSkill().compareTo(o.getParentSkill());
		if(skillCmp == 0)
			return getName().compareTo(o.getName());
		
		return skillCmp;
	}
	
}

package vazkii.skillable.skill;

import net.minecraft.util.text.translation.I18n;

public class Skill {

	private final String name;
	private final int index;
	
	public Skill(String name, int index) {
		this.name = name;
		this.index = index;
	}
	
	public String getKey() {
		return name;
	}
	
	public String getName() {
		return I18n.translateToLocal("skillable.skill." + getKey());
	}
	
	public int getIndex() {
		return index;
	}
	
}

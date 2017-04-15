package vazkii.skillable.skill;

import net.minecraft.block.Block;
import net.minecraft.util.text.translation.I18n;

public class Skill {

	private final String name;
	private final int index;
	private final Block background;
	
	public Skill(String name, int index, Block background) {
		this.name = name;
		this.index = index;
		this.background = background;
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
	
	public Block getBackground() {
		return background;
	}
	
}

package vazkii.skillable.skill.base;

public class Ability extends Unlockable {

	public Ability(String name, int x, int y) {
		super(name, x, y);
	}
	
	@Override
	public boolean hasSpikes() {
		return true;
	}

}

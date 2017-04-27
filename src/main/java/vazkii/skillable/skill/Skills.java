package vazkii.skillable.skill;

import java.util.HashMap;

import vazkii.skillable.skill.base.Unlockable;

public final class Skills {
	
	public static final HashMap<String, Skill> ALL_SKILLS = new HashMap();
	public static final HashMap<String, Unlockable> ALL_UNLOCKABLES = new HashMap();
	
	public static Skill mining;
	public static Skill gathering;
	public static Skill attack;
	public static Skill defense;
	public static Skill building;
	public static Skill farming;
	public static Skill agility;
	public static Skill magic;
	
	public static void init() {
		mining = addSkill(new SkillMining());
		gathering = addSkill(new SkillGathering());
		attack = addSkill(new SkillAttack());
		defense = addSkill(new SkillDefense());
		building = addSkill(new SkillBuilding());
		farming = addSkill(new SkillFarming());
		agility = addSkill(new SkillAgility());
		magic = addSkill(new SkillMagic());
	}
	
	private static Skill addSkill(Skill s) {
		ALL_SKILLS.put(s.getKey(), s);
		return s;
	}
	
}

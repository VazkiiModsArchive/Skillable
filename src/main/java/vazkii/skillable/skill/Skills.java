package vazkii.skillable.skill;

import vazkii.skillable.skill.base.Unlockable;

import java.util.HashMap;

public final class Skills {

    public static final HashMap<String, Skill> SKILLS = new HashMap();
    public static final HashMap<String, Unlockable> ALL_UNLOCKABLES = new HashMap();

    public static Skill mining;
    public static Skill gathering;
    public static Skill attack;
    public static Skill defense;
    public static Skill building;
    public static Skill farming;
    public static Skill agility;
    public static Skill magic;

    public static Skill registerSkill(String name, Skill skill) {
        SKILLS.put(name, skill);
        return skill;
    }

    public static void init() {
        mining = registerSkill("mining", new SkillMining());
        gathering = registerSkill("gathering", new SkillGathering());
        attack = registerSkill("attack", new SkillAttack());
        defense = registerSkill("defense", new SkillDefense());
        building = registerSkill("building", new SkillBuilding());
        farming = registerSkill("farming", new SkillFarming());
        agility = registerSkill("agility", new SkillAgility());
        magic = registerSkill("magic", new SkillMagic());
    }
}

package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.magic.TraitGoldenOsmosis;
import codersafterdark.reskillable.skill.magic.TraitSafePort;
import net.minecraft.init.Blocks;

public class SkillMagic extends Skill {

    public SkillMagic() {
        super("magic", 7, Blocks.END_STONE);
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitGoldenOsmosis());
        addUnlockable(new TraitSafePort());
    }

}

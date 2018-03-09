package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.skill.magic.TraitGoldenOsmosis;
import codersafterdark.reskillable.skill.magic.TraitSafePort;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;

public class SkillMagic extends Skill {

    public SkillMagic() {
        super("magic", 7, new ResourceLocation("textures/blocks/end_stone.png"));
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitGoldenOsmosis());
        addUnlockable(new TraitSafePort());
    }

}

package vazkii.skillable.skill;

import net.minecraft.init.Blocks;
import vazkii.skillable.skill.farming.TraitGreenThumb;
import vazkii.skillable.skill.farming.TraitMoreWheat;

public class SkillFarming extends Skill {

    public SkillFarming() {
        super("farming", 5, Blocks.DIRT);
    }

    @Override
    public void initUnlockables() {
        addUnlockable(new TraitMoreWheat());
        addUnlockable(new TraitGreenThumb());
    }

}

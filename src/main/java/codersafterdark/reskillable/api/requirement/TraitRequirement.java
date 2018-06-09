package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class TraitRequirement extends Requirement {
    private Unlockable unlockable;

    public TraitRequirement(Unlockable unlockable) {
        this.unlockable = unlockable;
        this.tooltip =  TextFormatting.GRAY + " - " + TextFormatting.LIGHT_PURPLE + new TextComponentTranslation("skillable.misc.traitFormat", "%s",
                this.unlockable.getName()).getUnformattedComponentText();
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        return PlayerDataHandler.get(entityPlayer).getSkillInfo(unlockable.getParentSkill()).isUnlocked(unlockable);
    }

    public Skill getSkill() {
        return unlockable.getParentSkill();
    }

    public Unlockable getUnlockable() {
        return unlockable;
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        return other instanceof TraitRequirement ? getUnlockable().getKey().equals(((TraitRequirement) other).getUnlockable().getKey()) ?
                RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL : RequirementComparision.NOT_EQUAL;
    }

    @Override
    public boolean isEnabled() {
        return getUnlockable() != null && getUnlockable().isEnabled();
    }
}
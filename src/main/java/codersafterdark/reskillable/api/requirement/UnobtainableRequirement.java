package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class UnobtainableRequirement extends Requirement {

    public UnobtainableRequirement (){}

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayerMP) {
        return false;
    }

    @Override
    public String getToolTip(PlayerData data) {
        return TextFormatting.RED + new TextComponentTranslation("skillable.misc.unobtainableFormat").getUnformattedComponentText();
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        if (other instanceof UnobtainableRequirement) {
            return RequirementComparision.EQUAL_TO;
        }
        return RequirementComparision.NOT_EQUAL;
    }
}
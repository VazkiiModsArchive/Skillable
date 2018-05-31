package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

//This has the same body as a TrueRequirement, except that it should not simplify out
public final class NoneRequirement extends Requirement {
    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayerMP) {
        return true;
    }

    @Override
    public String getToolTip(PlayerData data) {
        return TextFormatting.GREEN + new TextComponentTranslation("skillable.misc.unobtainableFormat").getUnformattedComponentText();
    }
}
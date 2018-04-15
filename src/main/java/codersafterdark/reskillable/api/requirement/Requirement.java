package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.data.PlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Requirement {
    public abstract boolean achievedByPlayer(EntityPlayer entityPlayerMP);

    @SideOnly(Side.CLIENT)
    public abstract String getToolTip(PlayerData data);

    public RequirementComparision matches(Requirement other) {
        return equals(other) ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
    }
}

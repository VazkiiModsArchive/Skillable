package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.base.PlayerData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Requirement {
    public abstract boolean achievedByPlayer(EntityPlayerMP entityPlayerMP);

    @SideOnly(Side.CLIENT)
    public abstract String getToolTip(PlayerData data);
}

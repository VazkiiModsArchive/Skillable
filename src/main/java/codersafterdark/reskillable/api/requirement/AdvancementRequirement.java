package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.base.PlayerData;
import codersafterdark.reskillable.base.RequirementHolder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AdvancementRequirement extends Requirement {
    private ResourceLocation advancementName;

    public AdvancementRequirement(ResourceLocation advancementName) {
        this.advancementName = advancementName;
    }

    @Override
    public boolean achievedByPlayer(EntityPlayerMP entityPlayerMP) {
        AdvancementManager manager = ((WorldServer) entityPlayerMP.world).getAdvancementManager();

        Advancement adv = manager.getAdvancement(advancementName);
        if (adv != null) {
            AdvancementProgress progress = entityPlayerMP.getAdvancements().getProgress(adv);
            if (!progress.isDone()) {
                return false;
            }
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getToolTip(PlayerData data) {
        Advancement adv = getAdvancement();
        String toolTip = "";
        if (adv != null) {
            toolTip = TextFormatting.GRAY + " - " +
                    I18n.format("skillable.misc.achievementFormat",
                            adv.getDisplayText().getUnformattedText().replaceAll("\\[|\\]", ""));
        }
        return toolTip;
    }

    public Advancement getAdvancement() {
        return RequirementHolder.getAdvancementList().getAdvancement(advancementName);
    }
}

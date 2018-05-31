package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.RequirementHolder;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.Optional;

public class AdvancementRequirement extends Requirement {
    private ResourceLocation advancementName;

    public AdvancementRequirement(ResourceLocation advancementName) {
        this.advancementName = advancementName;
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        return Optional.ofNullable(this.getAdvancement())
                .map(advancement -> ReskillableAPI.getInstance().getAdvancementProgress(entityPlayer, advancement))
                .map(AdvancementProgress::isDone)
                .orElse(false);
    }

    @Override
    public String getToolTip(PlayerData data) {
        Advancement adv = getAdvancement();
        if (adv == null) {
            return "";
        }
        return TextFormatting.GRAY + " - " + TextFormatting.GOLD + new TextComponentTranslation("skillable.misc.achievementFormat",
                data == null || !achievedByPlayer(data.playerWR.get()) ? TextFormatting.RED : TextFormatting.GREEN,
                adv.getDisplayText().getUnformattedText().replaceAll("[\\[\\]]", "")).getUnformattedComponentText();
    }

    public Advancement getAdvancement() {
        return RequirementHolder.getAdvancementList().getAdvancement(advancementName);
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        return other instanceof AdvancementRequirement && getAdvancement().equals(((AdvancementRequirement) other).getAdvancement())
                ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
    }
}
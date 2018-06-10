package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.ReskillableAPI;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public class AdvancementRequirement extends Requirement {
    private Advancement advancement;

    //TODO: Double check that this does not need to actually check the advancement later
    public AdvancementRequirement(Advancement advancement) {
        this.advancement = advancement;
        this.tooltip = TextFormatting.GRAY + " - " + TextFormatting.GOLD + new TextComponentTranslation("skillable.misc.achievementFormat",
                "%S", advancement.getDisplayText().getUnformattedText().replaceAll("[\\[\\]]", "")).getUnformattedComponentText();
    }

    @Override
    public boolean achievedByPlayer(EntityPlayer entityPlayer) {
        return ReskillableAPI.getInstance().getAdvancementProgress(entityPlayer, advancement).isDone();
    }

    public Advancement getAdvancement() {
        return advancement;
    }

    @Override
    public RequirementComparision matches(Requirement other) {
        return other instanceof AdvancementRequirement && advancement.equals(((AdvancementRequirement) other).advancement)
                ? RequirementComparision.EQUAL_TO : RequirementComparision.NOT_EQUAL;
    }

    @Override
    public boolean equals(Object o) {
        return o == this || o instanceof AdvancementRequirement && advancement.equals(((AdvancementRequirement) o).advancement);
    }

    @Override
    public int hashCode() {
        return advancement.hashCode();
    }
}
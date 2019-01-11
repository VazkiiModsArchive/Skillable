package codersafterdark.reskillable.advancement.skilllevel;

import codersafterdark.reskillable.Reskillable;
import codersafterdark.reskillable.advancement.CriterionTrigger;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.lib.LibMisc;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SkillLevelTrigger extends CriterionTrigger<SkillLevelListeners, SkillLevelCriterionInstance> {
    public SkillLevelTrigger() {
        super(new ResourceLocation(LibMisc.MOD_ID, "skill_level"), SkillLevelListeners::new);
    }

    public void trigger(EntityPlayerMP player, Skill skill, int level) {
        SkillLevelListeners listeners = this.getListeners(player.getAdvancements());
        if (listeners != null) {
            listeners.trigger(skill, level);
        }
    }

    @Override
    public SkillLevelCriterionInstance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        int level = JsonUtils.getInt(json, "level");
        if (json.has("skill")) {
            ResourceLocation skillName = new ResourceLocation(JsonUtils.getString(json, "skill"));
            Skill skill = ReskillableRegistries.SKILLS.getValue(skillName);
            if (skill != null) {
                return new SkillLevelCriterionInstance(skill, level);
            }
            throw new JsonParseException("Failed to find Matching Skill for Name: " + skillName);
        }
        return new SkillLevelCriterionInstance(null, level);
    }
}

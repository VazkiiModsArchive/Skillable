package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.Reskillable;
import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.skill.Skill;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.util.Map;
import java.util.function.Function;

public class RequirementRegistry {
    private Map<String, Function<String, Requirement>> requirementHandlers = Maps.newHashMap();

    public Requirement getRequirement(String requirementString) {
        String[] requirements = requirementString.split("\\|");
        Requirement requirement = null;
        if (requirements.length == 2) {
            String requirementType = requirements[0];
            String requirementInputs = requirements[1];

            if (requirementHandlers.containsKey(requirementType)) {
                requirement = requirementHandlers.get(requirementType).apply(requirementInputs);
            } else {
                try {
                    int level = Integer.parseInt(requirementInputs);
                    Skill skill = ReskillableRegistries.SKILLS.getValue(new ResourceLocation(requirementType));
                    if (skill != null && level > 1) {
                        requirement = new SkillRequirement(skill, level);
                    } else {
                        ReskillableAPI.getInstance().log(Level.WARN, "Invalid Level Lock: " + requirementString);
                    }
                } catch (NumberFormatException e) {
                    ReskillableAPI.getInstance().log(Level.WARN, "Invalid Level Lock: " + requirementString);
                }
            }

        } else if (requirements.length > 2) {
            String requirementType = requirements[0];
            if (requirementHandlers.containsKey(requirementType)) {
                //Pass them the whole extended requirement Inputs (Note: they will have to split by | themselves)
                requirement = requirementHandlers.get(requirementType).apply(requirementString.substring(requirementType.length() + 1));
            }
        }
        if (requirement == null) {
            Reskillable.logger.log(Level.ERROR, "Invalid Lock for Input: " + requirementString);
        } else if (!requirement.isEnabled()) {
            //TODO: potentially let requirements set their own disabled message
            Reskillable.logger.log(Level.ERROR, "Disabled Requirement for Input: " + requirementString);
            requirement = null;
        }
        return requirement;
    }

    public void addRequirementHandler(String identity, Function<String, Requirement> creator) {
        requirementHandlers.put(identity, creator);
    }
}

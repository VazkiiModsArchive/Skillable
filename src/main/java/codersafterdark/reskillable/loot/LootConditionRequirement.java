package codersafterdark.reskillable.loot;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.lib.LibMisc;
import com.google.gson.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.LootCondition;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LootConditionRequirement implements LootCondition {
    private final RequirementHolder requirementHolder;
    private final boolean requiresPlayer;
    private final String[] requirements;

    public LootConditionRequirement(boolean requiresPlayer, String[] requirements) {
        this.requiresPlayer = requiresPlayer;
        this.requirementHolder = requirements.length > 0 ? RequirementHolder.fromStringList(requirements) :
                RequirementHolder.realEmpty();
        this.requirements = requirements;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean testCondition(Random rand, LootContext context) {
        return context.getKillerPlayer() instanceof EntityPlayer ?
                PlayerDataHandler.get((EntityPlayer) context.getKillerPlayer()).matchStats(requirementHolder) :
                !requiresPlayer;
    }

    public static class Serializer extends LootCondition.Serializer<LootConditionRequirement> {
        public Serializer() {
            super(new ResourceLocation(LibMisc.MOD_ID, "requirement"), LootConditionRequirement.class);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void serialize(JsonObject json, LootConditionRequirement value, JsonSerializationContext context) {
            json.addProperty("requiresPlayer", value.requiresPlayer);
            if (value.requirements.length > 1) {
                JsonArray requirementsJson = new JsonArray();
                for (String requirement : value.requirements) {
                    requirementsJson.add(requirement);
                }
                json.add("requirements", requirementsJson);
            } else {
                json.addProperty("requirements", value.requirements[0]);
            }
        }

        @Override
        @Nonnull
        @ParametersAreNonnullByDefault
        public LootConditionRequirement deserialize(JsonObject json, JsonDeserializationContext context) {
            boolean requiresPlayer = JsonUtils.getBoolean(json, "requiresPlayer", false);
            String[] requirements = new String[0];
            JsonElement requirementsJson = json.get("requirements");
            if (requirementsJson != null) {
                if (requirementsJson.isJsonArray()) {
                    List<String> reqs = new ArrayList<>();
                    requirementsJson.getAsJsonArray().forEach(req -> reqs.add(req.getAsString()));
                    requirements = reqs.toArray(new String[0]);
                } else if (requirementsJson.isJsonPrimitive() && requirementsJson.getAsJsonPrimitive().isString()) {
                    requirements = new String[] {requirementsJson.getAsJsonPrimitive().getAsString()};
                } else {
                    throw new JsonParseException("Failed to find Requirements");
                }
            }
            return new LootConditionRequirement(requiresPlayer, requirements);
        }
    }
}

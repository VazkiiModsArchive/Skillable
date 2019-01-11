package codersafterdark.reskillable.advancement.trait;

import codersafterdark.reskillable.advancement.CriterionTrigger;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.lib.LibMisc;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class UnlockUnlockableTrigger extends CriterionTrigger<UnlockUnlockableListeners, UnlockUnlockableCriterionInstance> {
    public UnlockUnlockableTrigger() {
        super(new ResourceLocation(LibMisc.MOD_ID, "unlockable"), UnlockUnlockableListeners::new);
    }

    public void trigger(EntityPlayerMP entityPlayer, Unlockable unlockable) {
        UnlockUnlockableListeners listeners = this.getListeners(entityPlayer.getAdvancements());
        if (listeners != null) {
            listeners.trigger(unlockable);
        }
    }

    @Override
    public UnlockUnlockableCriterionInstance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        if (json.has("unlockable_name")) {
            String unlockableName = json.get("unlockable_name").getAsString();
            Unlockable unlockable = ReskillableRegistries.UNLOCKABLES.getValue(new ResourceLocation(unlockableName));
            if (unlockable != null) {
                return new UnlockUnlockableCriterionInstance(unlockable);
            }
            throw new JsonParseException("No Unlockable found for name " + unlockableName);
        }
        throw new JsonParseException("Field 'unlockable_name' not found");
    }
}

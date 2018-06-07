package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.event.LockUnlockableEvent;
import codersafterdark.reskillable.api.event.UnlockUnlockableEvent;
import codersafterdark.reskillable.api.requirement.logic.DoubleRequirement;
import codersafterdark.reskillable.api.requirement.logic.impl.NOTRequirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.annotation.Nonnull;
import java.util.*;

public class RequirementCache {
    private static List<Class<? extends Requirement>> dirtyCacheTypes = new ArrayList<>();
    private static Map<UUID, RequirementCache> cacheMap = new HashMap<>();

    //TODO: Should it be requirement -> player/boolean so that it does not need to keep track of multiple requirements? They really are pointers so it should not matter
    private Map<Requirement, Boolean> requirementCache = new HashMap<>();
    private EntityPlayer player;
    private boolean dirtyCache;

    public RequirementCache(@Nonnull EntityPlayer player) {
        this.player = player;
        cacheMap.put(player.getUniqueID(), this);
    }

    //TODO: Improved caching for tooltips. Would specifically help logic requirements and item requirements because their tooltip logic can be rather complex
    public boolean requirementAchieved(Requirement requirement) {
        if (requirement == null) {
            return false;
        }
        if (requirementCache.containsKey(requirement)) {
            return requirementCache.get(requirement);
        }
        boolean achieved = requirement.achievedByPlayer(player);
        requirementCache.put(requirement, achieved);
        if (dirtyCacheTypes.stream().anyMatch(dirtyType -> dirtyType.isInstance(requirement))) {
            dirtyCache = true;
        }
        return achieved;
    }

    public void invalidateCache(Class<? extends Requirement>... cacheType) {
        List<Class<? extends Requirement>> dirtyTypes = dirtyCache ? new ArrayList<>(dirtyCacheTypes) : new ArrayList<>();
        //Clear all types that are supposed to be invalidated each time if dirtyCache is true

        if (cacheType != null) {
            dirtyTypes.addAll(Arrays.asList(cacheType));
        }

        if (dirtyTypes.isEmpty()) {
            return;
        }
        Set<Requirement> requirements = requirementCache.keySet();
        List<Requirement> toRemove = new ArrayList<>();

        for (Requirement requirement : requirements) {
            for (Class<? extends Requirement> dirtyType : dirtyTypes) {
                if (dirtyType.isInstance(requirement)) {
                    toRemove.add(requirement);
                }
            }
        }
        toRemove.forEach(requirement -> requirementCache.remove(requirement));
    }

    public static void registerDirtyTypes() {
        //Register logic requirements and any other implementations of DoubleRequirement to be invalidated
        registerRequirementType(NOTRequirement.class, DoubleRequirement.class);
    }

    //A method to allow adding of requirements that should always be invalidated if other requirements get invalidated
    public static void registerRequirementType(Class<? extends Requirement>... requirementClasses) {
        dirtyCacheTypes.addAll(Arrays.asList(requirementClasses));
    }

    public static void invalidateCache(EntityPlayer player, Class<? extends Requirement>... cacheTypes) {
        if (player != null) {
            invalidateCache(player.getUniqueID(), cacheTypes);
        }
    }

    public static void invalidateCache(UUID uuid, Class<? extends Requirement>... cacheTypes) {
        RequirementCache requirementCache = cacheMap.get(uuid);
        if (requirementCache != null) {
            requirementCache.invalidateCache(cacheTypes);
        }
    }

    public static boolean requirementAchieved(EntityPlayer player, Requirement requirement) {
        if (player != null) {
            return requirementAchieved(player.getUniqueID(), requirement);
        }
        return false;
    }

    public static boolean requirementAchieved(UUID uuid, Requirement requirement) {
        RequirementCache requirementCache = cacheMap.get(uuid);
        if (requirementCache != null) {
            return requirementCache.requirementAchieved(requirement);
        }
        return false;
    }

    @SubscribeEvent
    public static void onLevelChange(LevelUpEvent.Post event) {
        //Just invalidate all skills because it is easier than checking each requirement they have to see if the skill matches
        invalidateCache(event.getEntityPlayer().getUniqueID(), SkillRequirement.class);
    }

    @SubscribeEvent
    public static void onUnlockableLocked(LockUnlockableEvent.Post event) {
        invalidateCache(event.getEntityPlayer().getUniqueID(), TraitRequirement.class);
    }

    @SubscribeEvent
    public static void onUnlockableUnlocked(UnlockUnlockableEvent.Post event) {
        invalidateCache(event.getEntityPlayer().getUniqueID(), TraitRequirement.class);
    }

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        invalidateCache(event.getEntityPlayer().getUniqueID(), AdvancementRequirement.class);
    }

    @SubscribeEvent
    public static void onDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        cacheMap.remove(event.player.getUniqueID());
    }
}
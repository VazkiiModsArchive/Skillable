package codersafterdark.reskillable.api.requirement;

import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.event.LockUnlockableEvent;
import codersafterdark.reskillable.api.event.UnlockUnlockableEvent;
import codersafterdark.reskillable.api.requirement.logic.impl.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.*;

public class RequirementCache {
    private static List<Class<? extends Requirement>> dirtyCacheTypes = new ArrayList<>();
    private static Map<UUID, RequirementCache> cacheMap = new HashMap<>();

    //TODO should it be requirement -> player/boolean so that it does not need to keep track of multiple requirements? They really are pointers so it should not matter
    private Map<Requirement, Boolean> requirementCache = new HashMap<>();
    private EntityPlayer player;
    private boolean dirtyCache;

    public RequirementCache(@Nonnull EntityPlayer player) {
        this.player = player;
        cacheMap.put(player.getUniqueID(), this);
    }

    public boolean requirementAchieved(Requirement requirement) {
        if (requirementCache.containsKey(requirement)) {
            return requirementCache.get(requirement);
        }
        boolean achieved = requirement.achievedByPlayer(player);
        requirementCache.put(requirement, achieved);
        dirtyCache = true; //TODO: Decide if this should only add as dirty if the type matches one of the dirtyCacheTypes. Would potentially improve performance
        return achieved;
    }

    public void invalidateCache(Class<? extends Requirement> cacheType) {
        List<Class<? extends Requirement>> dirtyTypes = null;
        if (dirtyCache) {
            //Clear all types that are supposed to be invalidated each time
            dirtyTypes = new ArrayList<>(dirtyCacheTypes);
            if (cacheType != null) {
                dirtyTypes.add(cacheType);
            }
        } else if (cacheType != null) {
            dirtyTypes = Collections.singletonList(cacheType);
        }
        if (dirtyTypes == null || dirtyTypes.isEmpty()) {
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
        //Register all Logic requirements to Requirement.class so that they always get invalidated when things become invalid
        registerRequirementType(NOTRequirement.class, ANDRequirement.class, NANDRequirement.class, ORRequirement.class, NORRequirement.class,
                XORRequirement.class, XNORRequirement.class);
    }

    //A method to allow adding of requirements that should always be invalidated if other requirements get invalidated
    public static void registerRequirementType(Class<? extends Requirement>... requirementClasses) {
        dirtyCacheTypes.addAll(Arrays.asList(requirementClasses));
    }

    public static void invalidateCache(EntityPlayer player, Class<? extends Requirement> cacheType) {
        if (player != null) {
            invalidateCache(player.getUniqueID(), cacheType);
        }
    }

    public static void invalidateCache(UUID uuid, Class<? extends Requirement> cacheType) {
        RequirementCache requirementCache = cacheMap.get(uuid);
        if (requirementCache != null) {
            requirementCache.invalidateCache(cacheType);
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

    //TODO add the listeners that invalidate the caches for the types we have
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
}
package codersafterdark.reskillable.api.unlockable;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.event.CacheInvalidatedEvent;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.toast.ToastHelper;
import codersafterdark.reskillable.base.LevelLockHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AutoUnlocker {
    private static Set<Unlockable> unlockables = new HashSet<>();
    private static boolean hasUncacheable;
    private static boolean hasBeenSet;

    public static void setUnlockables() {
        hasBeenSet = true;
        if (unlockables.isEmpty()) {
            Collection<Unlockable> entries = ReskillableRegistries.UNLOCKABLES.getValuesCollection();
            for (Unlockable u : entries) {
                if (u.isEnabled() && u.getCost() == 0) {
                    addUnlockable(u);
                }
            }
        } else {
            recheckUnlockables();
        }
    }

    private static void addUnlockable(Unlockable u) {
        unlockables.add(u);
        if (!hasUncacheable) {
            for (Requirement requirement : u.getRequirements().getRequirements()) {
                if (!requirement.isCacheable()) {
                    hasUncacheable = true;
                    break;
                }
            }
        }
    }

    //Generic method that just rechecks all unlockables because it is a lot simpler than trying to gather the unlockable from the config
    public static void recheckUnlockables() {
        if (!hasBeenSet) {
            return;
        }
        Collection<Unlockable> entries = ReskillableRegistries.UNLOCKABLES.getValuesCollection();
        for (Unlockable u : entries) {
            if (u.isEnabled() && u.getCost() == 0) {
                addUnlockable(u);
            } else {
                unlockables.remove(u);
            }
        }
        //TODO: This should really recheck all online players. Though I do not believe there is any real way to currently change cost while players are connected
    }

    public static void recheck(EntityPlayer player) {
        PlayerData data = PlayerDataHandler.get(player);
        if (data == null) {
            return;
        }

        boolean anyUnlocked = false;
        for (Unlockable u : unlockables) {
            PlayerSkillInfo skillInfo = data.getSkillInfo(u.getParentSkill());
            if (!skillInfo.isUnlocked(u)) {
                RequirementHolder holder = u.getRequirements();
                if (holder.equals(LevelLockHandler.EMPTY_LOCK) || data.matchStats(holder)) {
                    skillInfo.unlock(u, player);
                    if (player instanceof EntityPlayerMP) {
                        ToastHelper.sendUnlockableToast((EntityPlayerMP) player, u);
                    }
                    anyUnlocked = true;
                }
            }
        }
        if (anyUnlocked) {
            data.saveAndSync();
        }
    }

    @SubscribeEvent
    public static void onCacheInvalidated(CacheInvalidatedEvent event) {
        recheck(event.getPlayer());
    }

    @SubscribeEvent
    public static void onEntityLiving(LivingEvent.LivingUpdateEvent event) {
        if (hasUncacheable) {
            EntityLivingBase entityLiving = event.getEntityLiving();
            //Recheck every 5 seconds if any auto unlockables have uncacheable requirements.
            if (entityLiving instanceof EntityPlayer && !entityLiving.world.isRemote && entityLiving.ticksExisted % 100 == 0) {
                recheck((EntityPlayer) entityLiving);
            }
        }
    }
}
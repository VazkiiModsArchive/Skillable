package codersafterdark.reskillable.api.unlockable;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.toast.ToastHelper;
import codersafterdark.reskillable.base.LevelLockHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AutoUnlocker {
    private static Set<Unlockable> unlockables = new HashSet<>();

    public static void setUnlockables() {
        if (unlockables.isEmpty()) {
            Collection<Unlockable> entries = ReskillableRegistries.UNLOCKABLES.getValuesCollection();
            for (Unlockable u : entries) {
                if (u.isEnabled() && u.getCost() == 0) {
                    unlockables.add(u);
                }
            }
        }
    }

    //Generic method that just rechecks all unlockables because it is a lot simpler than trying to gather the unlockable from the config
    public static void recheckUnlockables() {
        Collection<Unlockable> entries = ReskillableRegistries.UNLOCKABLES.getValuesCollection();
        for (Unlockable u : entries) {
            if (u.isEnabled() && u.getCost() == 0) {
                unlockables.add(u);
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
}
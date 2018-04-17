package codersafterdark.reskillable.api.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class PlayerDataHandler {

    private static final String DATA_TAG = "SkillableData";
    private static HashMap<Integer, PlayerData> playerData = new HashMap<>();

    public static PlayerData get(EntityPlayer player) {
        if (player == null) {
            return null;
        }

        int key = getKey(player);
        if (!playerData.containsKey(key)) {
            playerData.put(key, new PlayerData(player));
        }

        PlayerData data = playerData.get(key);
        if (data.playerWR.get() != player) {
            NBTTagCompound cmp = new NBTTagCompound();
            data.saveToNBT(cmp);
            playerData.remove(key);
            data = get(player);
            data.loadFromNBT(cmp);
        }

        return data;
    }

    public static void cleanup() {
        List<Integer> removals = new ArrayList<>();
        for (Entry<Integer, PlayerData> item : playerData.entrySet()) {
            PlayerData d = item.getValue();
            if (d != null && d.playerWR.get() == null) {
                removals.add(item.getKey());
            }
        }

        for (int i : removals) {
            playerData.remove(i);
        }
    }

    private static int getKey(EntityPlayer player) {
        return player == null ? 0 : player.hashCode() << 1 + (player.getEntityWorld().isRemote ? 1 : 0);
    }

    public static NBTTagCompound getDataCompoundForPlayer(EntityPlayer player) {
        NBTTagCompound forgeData = player.getEntityData();
        if (!forgeData.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
            forgeData.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
        }

        NBTTagCompound persistentData = forgeData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
        if (!persistentData.hasKey(DATA_TAG)) {
            persistentData.setTag(DATA_TAG, new NBTTagCompound());
        }

        return persistentData.getCompoundTag(DATA_TAG);
    }

    public static class EventHandler {

        @SubscribeEvent
        public static void onServerTick(ServerTickEvent event) {
            if (event.phase == Phase.END) {
                PlayerDataHandler.cleanup();
            }
        }

        @SubscribeEvent
        public static void onPlayerLogin(PlayerLoggedInEvent event) {
            PlayerData data = PlayerDataHandler.get(event.player);
            if (data != null) {
                data.sync();
            }
        }

        @SubscribeEvent
        public static void onPlayerTick(PlayerTickEvent event) {
            if (event.phase == Phase.END) {
                PlayerData data = PlayerDataHandler.get(event.player);
                if (data != null) {
                    data.tickPlayer(event);
                }
            }
        }

        @SubscribeEvent
        public static void onBlockDrops(HarvestDropsEvent event) {
            PlayerData data = PlayerDataHandler.get(event.getHarvester());
            if (data != null) {
                data.blockDrops(event);
            }
        }

        @SubscribeEvent
        public static void onGetBreakSpeed(BreakSpeed event) {
            PlayerData data = PlayerDataHandler.get(event.getEntityPlayer());
            if (data != null) {
                data.breakSpeed(event);
            }
        }

        @SubscribeEvent
        public static void onMobDrops(LivingDropsEvent event) {
            if (event.getSource().getTrueSource() instanceof EntityPlayer) {
                PlayerData data = PlayerDataHandler.get((EntityPlayer) event.getSource().getTrueSource());
                if (data != null) {
                    data.mobDrops(event);
                }
            }
        }

        @SubscribeEvent
        public static void onHurt(LivingHurtEvent event) {
            if (event.getEntity() instanceof EntityPlayer) {
                PlayerData data = PlayerDataHandler.get((EntityPlayer) event.getEntity());
                if (data != null) {
                    data.hurt(event);
                }
            }
            if (event.getSource().getTrueSource() instanceof EntityPlayer) {
                PlayerData data = PlayerDataHandler.get((EntityPlayer) event.getSource().getTrueSource());
                if (data != null) {
                    data.attackMob(event);
                }
            }
        }

        @SubscribeEvent
        public static void onRightClickBlock(RightClickBlock event) {
            PlayerData data = PlayerDataHandler.get(event.getEntityPlayer());
            if (data != null) {
                data.rightClickBlock(event);
            }
        }

        @SubscribeEvent
        public static void onEnderTeleport(EnderTeleportEvent event) {
            if (event.getEntity() instanceof EntityPlayer) {
                PlayerData data = PlayerDataHandler.get((EntityPlayer) event.getEntity());
                if (data != null) {
                    data.enderTeleport(event);
                }
            }
        }

        @SubscribeEvent
        public static void onMobDeath(LivingDeathEvent event) {
            if (event.getSource().getTrueSource() instanceof EntityPlayer) {
                PlayerData data = PlayerDataHandler.get((EntityPlayer) event.getSource().getTrueSource());
                if (data != null) {
                    data.killMob(event);
                }
            }
        }

    }

}

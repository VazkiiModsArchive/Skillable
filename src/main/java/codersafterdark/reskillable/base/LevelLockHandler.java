package codersafterdark.reskillable.base;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.network.MessageLockedItem;
import codersafterdark.reskillable.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class LevelLockHandler {

    public static final String[] DEFAULT_SKILL_LOCKS = new String[]{"minecraft:iron_shovel:*=reskillable:gathering|5", "minecraft:iron_axe:*=reskillable:gathering|5", "minecraft:iron_sword:*=reskillable:attack|5", "minecraft:iron_pickaxe:*=reskillable:mining|5", "minecraft:iron_hoe:*=reskillable:farming|5", "minecraft:iron_helmet:*=reskillable:defense|5", "minecraft:iron_chestplate:*=reskillable:defense|5", "minecraft:iron_leggings:*=reskillable:defense|5", "minecraft:iron_boots:*=reskillable:defense|5", "minecraft:golden_shovel:*=reskillable:gathering|5,reskillable:magic|5", "minecraft:golden_axe:*=reskillable:gathering|5,reskillable:magic|5", "minecraft:golden_sword:*=reskillable:attack|5,reskillable:magic|5", "minecraft:golden_pickaxe:*=reskillable:mining|5,reskillable:magic|5", "minecraft:golden_hoe:*=reskillable:farming|5,reskillable:magic|5", "minecraft:golden_helmet:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_chestplate:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_leggings:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_boots:*=reskillable:defense|5,reskillable:magic|5", "minecraft:diamond_shovel:*=reskillable:gathering|16", "minecraft:diamond_axe:*=reskillable:gathering|16", "minecraft:diamond_sword:*=reskillable:attack|16", "minecraft:diamond_pickaxe:*=reskillable:mining|16", "minecraft:diamond_hoe:*=reskillable:farming|16", "minecraft:diamond_helmet:*=reskillable:defense|16", "minecraft:diamond_chestplate:*=reskillable:defense|16", "minecraft:diamond_leggings:*=reskillable:defense|16", "minecraft:diamond_boots:*=reskillable:defense|16", "minecraft:shears:*=reskillable:farming|5,reskillable:gathering|5", "minecraft:fishing_rod:*=reskillable:gathering|8", "minecraft:shield:*=reskillable:defense|8", "minecraft:bow:*=reskillable:attack|8", "minecraft:ender_pearl=reskillable:magic|8", "minecraft:ender_eye=reskillable:magic|16,reskillable:building|8", "minecraft:elytra:*=reskillable:defense|16,reskillable:agility|24,reskillable:magic|16", "minecraft:lead=reskillable:farming|5", "minecraft:end_crystal=reskillable:building|24,reskillable:magic|32", "minecraft:iron_horse_armor:*=reskillable:defense|5,reskillable:agility|5", "minecraft:golden_horse_armor:*=reskillable:defense|5,reskillable:magic|5,reskillable:agility|5", "minecraft:diamond_horse_armor:*=reskillable:defense|16,reskillable:agility|16", "minecraft:fireworks=reskillable:agility|24", "minecraft:dye:15=reskillable:farming|12", "minecraft:saddle=reskillable:agility|12", "minecraft:redstone=reskillable:building|5", "minecraft:redstone_torch=reskillable:building|5", "minecraft:skull:1=reskillable:building|20,reskillable:attack|20,reskillable:defense|20"};
    public static RequirementHolder EMPTY_LOCK = new RequirementHolder();
    private static final Map<ItemInfo, RequirementHolder> locks = new HashMap<>();
    private static Map<ItemInfo, Set<NBTTagCompound>> nbtLockInfo = new HashMap<>();
    private static RequirementHolder lastLock = EMPTY_LOCK;
    private static ItemStack lastItem;
    private static String[] configLocks;

    public static void loadFromConfig(String[] configValues) {
        configLocks = configValues;
    }

    public static void setupLocks() {
        if (configLocks != null) {
            for (String s : configLocks) {
                String[] tokens = s.split("=");
                if (tokens.length == 2) {
                    String itemName = tokens[0].toLowerCase();
                    String[] itemParts = itemName.split(":");
                    int metadata = 0;
                    if (itemParts.length > 2) {
                        String meta = itemParts[2];
                        try {
                            if (meta.equals("*")) {
                                metadata = OreDictionary.WILDCARD_VALUE;
                            } else {
                                metadata = Integer.parseInt(meta);
                            }
                            itemName = itemParts[0] + ':' + itemParts[1];
                        } catch (NumberFormatException ignored) {
                            //Do nothing if the meta is not a valid number or wildcard (Maybe it somehow is part of the item name)
                        }
                    }
                    Item item = Item.getByNameOrId(itemName);
                    if (item != null) {
                        addLock(new ItemStack(item, 1, metadata), RequirementHolder.fromString(tokens[1]));
                    }
                }
            }
        }
    }

    public static void addLock(ItemStack stack, RequirementHolder holder) {
        locks.put(new ItemInfo(stack.getItem(), stack.getMetadata(), stack.getTagCompound()), holder);
        //Only bother mapping it if there is an NBT tag
        if (stack.hasTagCompound()) {
            //Store the NBT tag in a list for the specific item
            nbtLockInfo.computeIfAbsent(new ItemInfo(stack.getItem(), stack.getMetadata()), k -> new HashSet<>()).add(stack.getTagCompound());
        }

        //Reset the tooltip cache in case the item being hovered is what changed
        lastItem = null;
        lastLock = EMPTY_LOCK;
    }

    public static RequirementHolder getSkillLock(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return EMPTY_LOCK;
        }

        ItemInfo cleanStack = new ItemInfo(stack.getItem(), stack.getMetadata());
        //There is no NBT information so matching is not needed. OR no specific NBT locks for this item stack so the NBT tags can be ignored
        if (!stack.hasTagCompound() || !nbtLockInfo.containsKey(cleanStack)) {
            return locks.getOrDefault(cleanStack, EMPTY_LOCK);
        }

        NBTTagCompound tag = stack.getTagCompound();
        Set<NBTTagCompound> nbtLookup = nbtLockInfo.get(cleanStack);
        ItemInfo bestStack = cleanStack; //Default to the clean variant of the stack
        for (NBTTagCompound nbt : nbtLookup) {
            if (tag.equals(nbt)) {
                bestStack = new ItemInfo(stack.getItem(), stack.getMetadata(), nbt);
                break;
            }
        }
        return locks.getOrDefault(bestStack, EMPTY_LOCK);
    }

    private static class ItemInfo {
        private int metadata;
        private Item item;
        private NBTTagCompound tag;

        private ItemInfo(Item item, int metadata) {
            this(item, metadata, null);
        }

        private ItemInfo(Item item, int metadata, NBTTagCompound tag) {
            this.item = item;
            this.metadata = metadata;
            this.tag = tag;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (!(o instanceof ItemInfo)) {
                return false;
            }
            ItemInfo other = (ItemInfo) o;
            return item == other.item && tag == other.tag &&
                    (metadata == OreDictionary.WILDCARD_VALUE || other.metadata == OreDictionary.WILDCARD_VALUE || metadata == other.metadata);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item, tag);
        }
    }


    public static boolean canPlayerUseItem(EntityPlayer player, ItemStack stack) {
        if (stack.isEmpty())
            return true;

        RequirementHolder lock = getSkillLock(stack);
        if (lock == null)
            return true;

        PlayerData data = PlayerDataHandler.get(player);
        return data.matchStats(lock);
    }

    @SubscribeEvent
    public static void hurtEvent(LivingAttackEvent event) {
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            ItemStack stack = player.getHeldItemMainhand();

            if (ConfigHandler.enforceFakePlayers) {
                if (!player.isCreative() && !canPlayerUseItem(player, stack)) {
                    event.setCanceled(true);
                    if (!isFake(player)) {
                        tellPlayer(player, stack, MessageLockedItem.MSG_ITEM_LOCKED);
                    }
                }
            } else if (!isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                tellPlayer(player, stack, MessageLockedItem.MSG_ITEM_LOCKED);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void leftClick(LeftClickBlock event) {
        if (isFake(event))
            return;
        enforce(event);

        if (!event.isCanceled()) {
            EntityPlayer player = event.getEntityPlayer();
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            Block block = state.getBlock();
            int meta = state.getBlock().getMetaFromState(state);
            ItemStack stack = new ItemStack(state.getBlock(), 1, meta);
            if (stack.isEmpty())
                stack = block.getItem(event.getWorld(), event.getPos(), state);

            if (ConfigHandler.enforceFakePlayers) {
                if (!player.isCreative() && !canPlayerUseItem(player, stack)) {
                    event.setCanceled(true);
                    if (!isFake(player)) {
                        tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_BREAK_LOCKED);
                    }
                }
            } else if (!isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_BREAK_LOCKED);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void rightClickItem(RightClickItem event) {
        if (!isFake(event))
            enforce(event);
    }

    @SubscribeEvent
    public static void rightClickBlock(RightClickBlock event) {
        if (isFake(event))
            return;
        enforce(event);
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        Block block = state.getBlock();
        int meta = state.getBlock().getMetaFromState(state);
        ItemStack stack = new ItemStack(block, 1, meta);
        if (stack.isEmpty())
            stack = block.getItem(event.getWorld(), event.getPos(), state);
        if (ConfigHandler.enforceFakePlayers) {
            if (!player.isCreative() && !canPlayerUseItem(player, stack)) {
                event.setUseBlock(Result.DENY);
                event.setUseItem(player.isSneaking() ? Result.DEFAULT : Result.DENY);
                if (!isFake(player)) {
                    tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_USE_LOCKED);
                }
            }
        } else if (!isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
            tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_USE_LOCKED);
            event.setUseBlock(Result.DENY);
            event.setUseItem(player.isSneaking() ? Result.DEFAULT : Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        Block block = state.getBlock();
        int meta = state.getBlock().getMetaFromState(state);
        ItemStack stack = new ItemStack(block, 1, meta);

        if (ConfigHandler.enforceFakePlayers) {
            if (!player.isCreative() && !canPlayerUseItem(player, stack)) {
                event.setCanceled(true);
                if (!isFake(player)) {
                    tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_BREAK_LOCKED);
                }
            }
        } else if (!isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
            tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_BREAK_LOCKED);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void entityInteract(EntityInteract event) {
        if (!isFake(event))
            enforce(event);
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent event) {
        if (!event.player.isCreative() && !isFake(event.player))
            for (int i = 0; i < event.player.inventory.armorInventory.size(); i++) {
                ItemStack stack = event.player.inventory.armorInventory.get(i);
                if (!stack.isEmpty() && !canPlayerUseItem(event.player, stack)) {
                    ItemStack copy = stack.copy();
                    if (!event.player.inventory.addItemStackToInventory(copy))
                        event.player.dropItem(copy, false);
                    event.player.inventory.armorInventory.set(i, ItemStack.EMPTY);
                    tellPlayer(event.player, stack, MessageLockedItem.MSG_ARMOR_EQUIP_LOCKED);
                }
            }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (ConfigHandler.disableSheepWool && event.getEntity() instanceof EntitySheep)
            event.getDrops().removeIf((e) -> e.getItem().getItem() == Item.getItemFromBlock(Blocks.WOOL));
    }

    private static boolean isFake(EntityEvent e) {
        return isFake(e.getEntity());
    }

    private static boolean isFake(Entity e) {
        return e instanceof FakePlayer;
    }

    private static void enforce(PlayerInteractEvent event) {
        if (event.isCanceled())
            return;

        EntityPlayer player = event.getEntityPlayer();
        if (player.isCreative())
            return;

        ItemStack stack = event.getItemStack();
        if (!canPlayerUseItem(player, stack)) {
            tellPlayer(player, stack, MessageLockedItem.MSG_ITEM_LOCKED);
            event.setCanceled(true);
        }
    }

    public static void tellPlayer(EntityPlayer player, ItemStack stack, String msg) {
        if (player instanceof EntityPlayerMP) {
            MessageLockedItem message = new MessageLockedItem(stack, msg);
            PacketHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack current = event.getItemStack();
        if (lastItem != current) {
            lastItem = current;
            lastLock = getSkillLock(current);
        }
        PlayerData data = PlayerDataHandler.get(Minecraft.getMinecraft().player);
        lastLock.addRequirementsToTooltip(data, event.getToolTip());
    }

}

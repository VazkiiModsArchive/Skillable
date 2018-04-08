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
import net.minecraft.item.crafting.Ingredient;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LevelLockHandler {

    public static final String[] DEFAULT_SKILL_LOCKS = new String[]{"minecraft:iron_shovel=reskillable:gathering|5", "minecraft:iron_axe=reskillable:gathering|5", "minecraft:iron_sword=reskillable:attack|5", "minecraft:iron_pickaxe=reskillable:mining|5", "minecraft:iron_hoe=reskillable:farming|5", "minecraft:iron_helmet=reskillable:defense|5", "minecraft:iron_chestplate=reskillable:defense|5", "minecraft:iron_leggings=reskillable:defense|5", "minecraft:iron_boots=reskillable:defense|5", "minecraft:golden_shovel=reskillable:gathering|5,reskillable:magic|5", "minecraft:golden_axe=reskillable:gathering|5,reskillable:magic|5", "minecraft:golden_sword=reskillable:attack|5,reskillable:magic|5", "minecraft:golden_pickaxe=reskillable:mining|5,reskillable:magic|5", "minecraft:golden_hoe=reskillable:farming|5,reskillable:magic|5", "minecraft:golden_helmet=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_chestplate=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_leggings=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_boots=reskillable:defense|5,reskillable:magic|5", "minecraft:diamond_shovel=reskillable:gathering|16", "minecraft:diamond_axe=reskillable:gathering|16", "minecraft:diamond_sword=reskillable:attack|16", "minecraft:diamond_pickaxe=reskillable:mining|16", "minecraft:diamond_hoe=reskillable:farming|16", "minecraft:diamond_helmet=reskillable:defense|16", "minecraft:diamond_chestplate=reskillable:defense|16", "minecraft:diamond_leggings=reskillable:defense|16", "minecraft:diamond_boots=reskillable:defense|16", "minecraft:shears=reskillable:farming|5,reskillable:gathering|5", "minecraft:fishing_rod=reskillable:gathering|8", "minecraft:shield=reskillable:defense|8", "minecraft:bow=reskillable:attack|8", "minecraft:ender_pearl=reskillable:magic|8", "minecraft:ender_eye=reskillable:magic|16,reskillable:building|8", "minecraft:elytra=reskillable:defense|16,reskillable:agility|24,reskillable:magic|16", "minecraft:lead=reskillable:farming|5", "minecraft:end_crystal=reskillable:building|24,reskillable:magic|32", "minecraft:iron_horse_armor=reskillable:defense|5,reskillable:agility|5", "minecraft:golden_horse_armor=reskillable:defense|5,reskillable:magic|5,reskillable:agility|5", "minecraft:diamond_horse_armor=reskillable:defense|16,reskillable:agility|16", "minecraft:fireworks=reskillable:agility|24", "minecraft:dye:15=reskillable:farming|12", "minecraft:saddle=reskillable:agility|12", "minecraft:redstone=reskillable:building|5", "minecraft:redstone_torch=reskillable:building|5", "minecraft:skull:1=reskillable:building|20,reskillable:attack|20,reskillable:defense|20"};
    public static final Map<ItemStack, RequirementHolder> locks = new HashMap<>();
    public static RequirementHolder EMPTY_LOCK = new RequirementHolder();
    public static Map<Ingredient, RequirementHolder> craftTweakerLocks = new HashMap<>();
    private static String[] configLocks;

    public static void loadFromConfig(String[] configValues) {
        configLocks = configValues;
    }

    public static void setupLocks() {
        locks.clear();
        if (configLocks != null) {
            for (String s : configLocks) {
                String[] tokens = s.split("=");
                if (tokens.length == 2) {
                    RequirementHolder h = RequirementHolder.fromString(tokens[1]);
                    locks.put(new ItemStack(Item.getByNameOrId(tokens[0].toLowerCase())), h);
                }
            }
        }
    }

    public static RequirementHolder getSkillLock(ItemStack stack) {
        if (stack == null || stack.isEmpty())
            return EMPTY_LOCK;
        for (ItemStack itemStack : locks.keySet()) {
            if (compareItem(itemStack, stack)) {
                return locks.get(itemStack);
            }
        }
        for (Ingredient ingredient : craftTweakerLocks.keySet()){
            ItemStack[] ingredientMatchingStacks = ingredient.getMatchingStacks();
            for (int i = 0; i < ingredientMatchingStacks.length; i++){
                compareItem(ingredientMatchingStacks[i], stack);
            }
        }
        return EMPTY_LOCK;
    }

    private static boolean compareItem(ItemStack itemStack, ItemStack stack) {
        if (itemStack.hasTagCompound() != stack.hasTagCompound()){
            return false;
        }
        if (itemStack.hasTagCompound() && stack.hasTagCompound()) {
            if (itemStack.getTagCompound().getKeySet().equals(stack.getTagCompound().getKeySet())){
                for (String s : itemStack.getTagCompound().getKeySet()){
                    if (!itemStack.getTagCompound().getTag(s).equals(stack.getTagCompound().getTag(s))){
                        return false;
                    }
                }
            }
        }
        if (itemStack.hasTagCompound() && !stack.hasTagCompound()){
            return stack.getItem() == itemStack.getItem() && (itemStack.getMetadata() == 32767 || stack.getMetadata() == itemStack.getMetadata());
        }
        if (!itemStack.hasTagCompound() && stack.hasTagCompound()){
            return stack.getItem() == itemStack.getItem() && (itemStack.getMetadata() == 32767 || stack.getMetadata() == itemStack.getMetadata());
        }
        if (!itemStack.hasTagCompound() && !stack.hasTagCompound()){
            return stack.getItem() == itemStack.getItem() && (itemStack.getMetadata() == 32767 || stack.getMetadata() == itemStack.getMetadata());
        }

        // if the item has nbt, then meta shouldn't be accounted for?
        return stack.getItem() == itemStack.getItem() && (itemStack.getMetadata() == 32767 || stack.getMetadata() == itemStack.getMetadata());
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
                if (isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                    event.setCanceled(true);
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
                if (isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                    event.setCanceled(true);
                }
            } else if (!player.isCreative() && !canPlayerUseItem(player, stack) && !isFake(player)) {
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
            if (isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                event.setUseBlock(Result.DENY);
                event.setUseItem(player.isSneaking() ? Result.DEFAULT : Result.DENY);
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
            if (isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                event.setCanceled(true);
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
        return ConfigHandler.enforceFakePlayers && e instanceof FakePlayer;
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

    private static ItemStack lastItem;
    private static RequirementHolder lastLock = EMPTY_LOCK;

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

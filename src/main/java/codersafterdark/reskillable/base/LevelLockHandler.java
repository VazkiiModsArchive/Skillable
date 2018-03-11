package codersafterdark.reskillable.base;

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

import java.util.HashMap;
import java.util.Map;

public class LevelLockHandler {

    public static final String[] DEFAULT_SKILL_LOCKS = new String[]{"minecraft:iron_shovel=gathering|5", "minecraft:iron_axe=gathering|5", "minecraft:iron_sword=attack|5", "minecraft:iron_pickaxe=mining|5", "minecraft:iron_hoe=farming|5", "minecraft:iron_helmet=defense|5", "minecraft:iron_chestplate=defense|5", "minecraft:iron_leggings=defense|5", "minecraft:iron_boots=defense|5", "minecraft:golden_shovel=gathering|5,magic|5", "minecraft:golden_axe=gathering|5,magic|5", "minecraft:golden_sword=attack|5,magic|5", "minecraft:golden_pickaxe=mining|5,magic|5", "minecraft:golden_hoe=farming|5,magic|5", "minecraft:golden_helmet=defense|5,magic|5", "minecraft:golden_chestplate=defense|5,magic|5", "minecraft:golden_leggings=defense|5,magic|5", "minecraft:golden_boots=defense|5,magic|5", "minecraft:diamond_shovel=gathering|16", "minecraft:diamond_axe=gathering|16", "minecraft:diamond_sword=attack|16", "minecraft:diamond_pickaxe=mining|16", "minecraft:diamond_hoe=farming|16", "minecraft:diamond_helmet=defense|16", "minecraft:diamond_chestplate=defense|16", "minecraft:diamond_leggings=defense|16", "minecraft:diamond_boots=defense|16", "minecraft:shears=farming|5,gathering|5", "minecraft:fishing_rod=gathering|8", "minecraft:shield=defense|8", "minecraft:bow=attack|8", "minecraft:ender_pearl=magic|8", "minecraft:ender_eye=magic|16,building|8", "minecraft:elytra=defense|16,agility|24,magic|16", "minecraft:lead=farming|5", "minecraft:end_crystal=building|24,magic|32", "minecraft:iron_horse_armor=defense|5,agility|5", "minecraft:golden_horse_armor=defense|5,magic|5,agility|5", "minecraft:diamond_horse_armor=defense|16,agility|16", "minecraft:fireworks=agility|24", "minecraft:dye:15=farming|12", "minecraft:saddle=agility|12", "minecraft:redstone=building|5", "minecraft:redstone_torch=building|5", "minecraft:skull:1=building|20,attack|20,defense|20"};
    public static final Map<ItemStack, RequirementHolder> locks = new HashMap();
    public static RequirementHolder EMPTY_LOCK = new RequirementHolder();
    private static String[] configLocks;

    public static Map<ItemStack, RequirementHolder> craftTweakerLocks = new HashMap();

    public static void loadFromConfig(String[] configValues) {
        configLocks = configValues;
    }

    public static void setupLocks() {
        locks.clear();
        if(configLocks == null)
            return;

        for(String s : configLocks) {
            String[] tokens = s.split("=");
            if(tokens.length == 2) {
                RequirementHolder h = RequirementHolder.fromString(tokens[1]);
                locks.put(new ItemStack(Item.getByNameOrId(tokens[0].toLowerCase())), h);
            }
        }
        locks.putAll(craftTweakerLocks);
    }

    public static RequirementHolder getSkillLock(ItemStack stack) {
        if(stack == null || stack.isEmpty())
            return EMPTY_LOCK;

        for(ItemStack itemStack : locks.keySet()) {
            if(compareItem(itemStack, stack)) {
                return locks.get(itemStack);
            }
        }

        return EMPTY_LOCK;
    }

    private static boolean compareItem(ItemStack itemStack, ItemStack stack) {
        if(itemStack.hasTagCompound() != stack.hasTagCompound()){
            return false;
        }
        if(itemStack.getTagCompound() == null && stack.getTagCompound() == null) {
            return stack.getItem() == itemStack.getItem() && (itemStack.getMetadata() == 32767 || stack.getMetadata() == itemStack.getMetadata());
        }
        if(itemStack.getTagCompound().getKeySet().equals(stack.getTagCompound().getKeySet())) {
            for(String s : itemStack.getTagCompound().getKeySet()) {
                if(!itemStack.getTagCompound().getTag(s).equals(stack.getTagCompound().getTag(s))) {
                    return false;
                }
            }
        }
        // if the item has nbt, then meta shouldn't be accounted for?
        return stack.getItem() == itemStack.getItem() && (itemStack.getMetadata() == 32767 || stack.getMetadata() == itemStack.getMetadata());
    }

    public static boolean canPlayerUseItem(EntityPlayer player, ItemStack stack) {
        if(stack.isEmpty())
            return true;

        RequirementHolder lock = getSkillLock(stack);
        if(lock == null)
            return true;

        PlayerData data = PlayerDataHandler.get(player);
        return data.matchStats(lock);
    }

    @SubscribeEvent
    public static void hurtEvent(LivingAttackEvent event) {
        if(event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            ItemStack stack = player.getHeldItemMainhand();

            if(ConfigHandler.enforceFakePlayers) {
                if(isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                    event.setCanceled(true);
                }
            } else if(!isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                tellPlayer(player, stack, MessageLockedItem.MSG_ITEM_LOCKED);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void leftClick(LeftClickBlock event) {
        if(isFake(event))
            return;
        enforce(event);

        if(!event.isCanceled()) {
            EntityPlayer player = event.getEntityPlayer();
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            Block block = state.getBlock();
            int meta = state.getBlock().getMetaFromState(state);
            ItemStack stack = new ItemStack(state.getBlock(), 1, meta);
            if(stack.isEmpty())
                stack = block.getItem(event.getWorld(), event.getPos(), state);

            if(ConfigHandler.enforceFakePlayers) {
                if(isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                    event.setCanceled(true);
                }
            } else if(!player.isCreative() && !canPlayerUseItem(player, stack) && !isFake(player)) {
                tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_BREAK_LOCKED);
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void rightClickItem(RightClickItem event) {
        if(!isFake(event))
            enforce(event);
    }

    @SubscribeEvent
    public static void rightClickBlock(RightClickBlock event) {
        if(isFake(event))
            return;
        enforce(event);
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        Block block = state.getBlock();
        int meta = state.getBlock().getMetaFromState(state);
        ItemStack stack = new ItemStack(block, 1, meta);
        if(stack.isEmpty())
            stack = block.getItem(event.getWorld(), event.getPos(), state);
        if(ConfigHandler.enforceFakePlayers) {
            if(isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                event.setUseBlock(Result.DENY);
                event.setUseItem(player.isSneaking() ? Result.DEFAULT : Result.DENY);
            }
        } else if(!isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
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

        if(ConfigHandler.enforceFakePlayers) {
            if(isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
                event.setCanceled(true);
            }
        } else if(!isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
            tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_BREAK_LOCKED);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void entityInteract(EntityInteract event) {
        if(!isFake(event))
            enforce(event);
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent event) {
        if(!event.player.isCreative() && !isFake(event.player))
            for(int i = 0; i < event.player.inventory.armorInventory.size(); i++) {
                ItemStack stack = event.player.inventory.armorInventory.get(i);
                if(!stack.isEmpty() && !canPlayerUseItem(event.player, stack)) {
                    ItemStack copy = stack.copy();
                    if(!event.player.inventory.addItemStackToInventory(copy))
                        event.player.dropItem(copy, false);
                    event.player.inventory.armorInventory.set(i, ItemStack.EMPTY);
                    tellPlayer(event.player, stack, MessageLockedItem.MSG_ARMOR_EQUIP_LOCKED);
                }
            }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if(ConfigHandler.disableSheepWool && event.getEntity() instanceof EntitySheep)
            event.getDrops().removeIf((e) -> e.getItem().getItem() == Item.getItemFromBlock(Blocks.WOOL));
    }

    private static boolean isFake(EntityEvent e) {
        return isFake(e.getEntity());
    }

    private static boolean isFake(Entity e) {
        return ConfigHandler.enforceFakePlayers && e instanceof FakePlayer;
    }

    private static void enforce(PlayerInteractEvent event) {
        if(event.isCanceled())
            return;

        EntityPlayer player = event.getEntityPlayer();
        if(player.isCreative())
            return;

        ItemStack stack = event.getItemStack();
        if(!canPlayerUseItem(player, stack)) {
            tellPlayer(player, stack, MessageLockedItem.MSG_ITEM_LOCKED);
            event.setCanceled(true);
        }
    }

    public static void tellPlayer(EntityPlayer player, ItemStack stack, String msg) {
        if(player instanceof EntityPlayerMP) {
            MessageLockedItem message = new MessageLockedItem(stack, msg);
            PacketHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onTooltip(ItemTooltipEvent event) {
        RequirementHolder lock = getSkillLock(event.getItemStack());
        PlayerData data = PlayerDataHandler.get(Minecraft.getMinecraft().player);
        lock.addRequirementsToTooltip(data, event.getToolTip());
    }

}

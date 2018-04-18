package codersafterdark.reskillable.base;

import codersafterdark.reskillable.api.data.*;
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
import net.minecraft.nbt.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
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
import java.util.stream.IntStream;

public class LevelLockHandler {

    public static final String[] DEFAULT_SKILL_LOCKS = new String[]{"minecraft:iron_shovel:*=reskillable:gathering|5", "minecraft:iron_axe:*=reskillable:gathering|5", "minecraft:iron_sword:*=reskillable:attack|5", "minecraft:iron_pickaxe:*=reskillable:mining|5", "minecraft:iron_hoe:*=reskillable:farming|5", "minecraft:iron_helmet:*=reskillable:defense|5", "minecraft:iron_chestplate:*=reskillable:defense|5", "minecraft:iron_leggings:*=reskillable:defense|5", "minecraft:iron_boots:*=reskillable:defense|5", "minecraft:golden_shovel:*=reskillable:gathering|5,reskillable:magic|5", "minecraft:golden_axe:*=reskillable:gathering|5,reskillable:magic|5", "minecraft:golden_sword:*=reskillable:attack|5,reskillable:magic|5", "minecraft:golden_pickaxe:*=reskillable:mining|5,reskillable:magic|5", "minecraft:golden_hoe:*=reskillable:farming|5,reskillable:magic|5", "minecraft:golden_helmet:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_chestplate:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_leggings:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_boots:*=reskillable:defense|5,reskillable:magic|5", "minecraft:diamond_shovel:*=reskillable:gathering|16", "minecraft:diamond_axe:*=reskillable:gathering|16", "minecraft:diamond_sword:*=reskillable:attack|16", "minecraft:diamond_pickaxe:*=reskillable:mining|16", "minecraft:diamond_hoe:*=reskillable:farming|16", "minecraft:diamond_helmet:*=reskillable:defense|16", "minecraft:diamond_chestplate:*=reskillable:defense|16", "minecraft:diamond_leggings:*=reskillable:defense|16", "minecraft:diamond_boots:*=reskillable:defense|16", "minecraft:shears:*=reskillable:farming|5,reskillable:gathering|5", "minecraft:fishing_rod:*=reskillable:gathering|8", "minecraft:shield:*=reskillable:defense|8", "minecraft:bow:*=reskillable:attack|8", "minecraft:ender_pearl=reskillable:magic|8", "minecraft:ender_eye=reskillable:magic|16,reskillable:building|8", "minecraft:elytra:*=reskillable:defense|16,reskillable:agility|24,reskillable:magic|16", "minecraft:lead=reskillable:farming|5", "minecraft:end_crystal=reskillable:building|24,reskillable:magic|32", "minecraft:iron_horse_armor:*=reskillable:defense|5,reskillable:agility|5", "minecraft:golden_horse_armor:*=reskillable:defense|5,reskillable:magic|5,reskillable:agility|5", "minecraft:diamond_horse_armor:*=reskillable:defense|16,reskillable:agility|16", "minecraft:fireworks=reskillable:agility|24", "minecraft:dye:15=reskillable:farming|12", "minecraft:saddle=reskillable:agility|12", "minecraft:redstone=reskillable:building|5", "minecraft:redstone_torch=reskillable:building|5", "minecraft:skull:1=reskillable:building|20,reskillable:attack|20,reskillable:defense|20"};
    private static final Map<LockKey, RequirementHolder> locks = new HashMap<>();
    public static RequirementHolder EMPTY_LOCK = new RequirementHolder();
    private static Map<ItemInfo, Set<ItemInfo>> nbtLockInfo = new HashMap<>();
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
                    if (itemParts.length == 1) {
                        addModLock(itemName, RequirementHolder.fromString(tokens[1])); //itemName is really the mod name
                        continue;
                    }
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

    public static void addLockByKey(LockKey key, RequirementHolder holder) {
        locks.put(key, holder);

        //Reset the tooltip cache in case the item being hovered is what changed
        lastItem = null;
        lastLock = EMPTY_LOCK;
    }

    public static void addModLock(String modName, RequirementHolder holder) {
        addLockByKey(new ModLockKey(modName), holder);
    }

    public static void addLock(ItemStack stack, RequirementHolder holder) {
        ItemInfo stackKey = new ItemInfo(stack.getItem(), stack.getMetadata(), stack.getTagCompound());
        addLockByKey(stackKey, holder);
        //Only bother mapping it if there is an NBT tag
        if (stack.hasTagCompound()) {
            //Store the NBT tag in a list for the specific item
            nbtLockInfo.computeIfAbsent(new ItemInfo(stack.getItem(), stack.getMetadata()), k -> new HashSet<>()).add(stackKey);
        }
    }

    public static RequirementHolder getSkillLock(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return EMPTY_LOCK;
        }

        ItemInfo cleanStack = new ItemInfo(stack.getItem(), stack.getMetadata());
        //There is no NBT information so matching is not needed. OR no specific NBT locks for this item stack so the NBT tags can be ignored
        ResourceLocation registryName = stack.getItem().getRegistryName();
        ModLockKey modLock = registryName != null ? new ModLockKey(registryName.getResourceDomain()) : null;
        if (!stack.hasTagCompound() || !nbtLockInfo.containsKey(cleanStack)) {
            return locks.getOrDefault(cleanStack, modLock != null ? locks.getOrDefault(modLock, EMPTY_LOCK) : EMPTY_LOCK);
        }

        NBTTagCompound tag = stack.getTagCompound();
        Set<ItemInfo> nbtItemLookup = nbtLockInfo.get(cleanStack);

        List<LockKey> partialLocks = new ArrayList<>();
        for (ItemInfo nbtItem : nbtItemLookup) {
            int comp = compareNBT(tag, nbtItem.getTag());
            if (comp == 0) {
                return locks.getOrDefault(nbtItem, modLock != null ? locks.getOrDefault(modLock, EMPTY_LOCK) : EMPTY_LOCK);
            } else if (comp > 0) { //Build up the best match
                partialLocks.add(nbtItem);
            }
        }
        if (partialLocks.isEmpty()) {
            return locks.getOrDefault(cleanStack, modLock != null ? locks.getOrDefault(modLock, EMPTY_LOCK) : EMPTY_LOCK);
        }

        return new RequirementHolder(partialLocks.stream().map(locks::get).filter(Objects::nonNull).toArray(RequirementHolder[]::new));
    }

    private static int compareNBT(NBTBase full, NBTBase partial) {
        if (full == null) {
            return partial != null ? -1 : 0;
        }
        if (partial == null) {
            return 1;
        }
        if (full.getId() != partial.getId()) {
            return -1;
        }
        if (full.equals(partial)) {
            return 0;
        }
        switch (full.getId()) {
            case Constants.NBT.TAG_COMPOUND:
                NBTTagCompound fullTag = (NBTTagCompound) full;
                NBTTagCompound partialTag = (NBTTagCompound) partial;
                Set<String> ptKeys = partialTag.getKeySet();
                for (String partialKey : ptKeys) {
                    //One of the keys is missing OR the tags are different types OR they do not match
                    if (!fullTag.hasKey(partialKey, partialTag.getTagId(partialKey)) || compareNBT(fullTag.getTag(partialKey), partialTag.getTag(partialKey)) < 0) {
                        return -1;
                    }
                }
                return 1;
            case Constants.NBT.TAG_LIST:
                NBTTagList fTagList = (NBTTagList) full;
                NBTTagList pTagList = (NBTTagList) partial;
                if (fTagList.hasNoTags() && !pTagList.hasNoTags() || fTagList.getTagType() != pTagList.getTagType()) {
                    return -1;
                }
                for (int i = 0; i < pTagList.tagCount(); i++) {
                    NBTBase pTag = pTagList.get(i);
                    boolean hasTag = false;
                    for (int j = 0; j < fTagList.tagCount(); j++) {
                        if (compareNBT(fTagList.get(j), pTag) >= 0) {
                            hasTag = true;
                            break;
                        }
                    }
                    if (!hasTag) {
                        return -1;
                    }
                }
                return 1;
            case Constants.NBT.TAG_BYTE_ARRAY:
                byte[] fByteArray = ((NBTTagByteArray) full).getByteArray();
                byte[] pByteArray = ((NBTTagByteArray) partial).getByteArray();
                for (byte pByte : pByteArray) {
                    boolean hasMatch = false;
                    for (byte fByte : fByteArray) {
                        if (pByte == fByte) {
                            hasMatch = true;
                            break;
                        }
                    }
                    if (!hasMatch) {
                        return -1;
                    }
                }
                return 1;
            case Constants.NBT.TAG_INT_ARRAY:
                int[] fIntArray = ((NBTTagIntArray) full).getIntArray();
                int[] pIntArray = ((NBTTagIntArray) partial).getIntArray();
                for (int pint : pIntArray) {
                    if (IntStream.of(fIntArray).noneMatch(i -> i == pint)){
                        return -1;
                    }
                }
                return 1;
            case Constants.NBT.TAG_LONG_ARRAY:
                //Not sure how to get the long array object from this to actually compare them
                return -1;
            default:
                return -1;
        }
    }

    public static boolean canPlayerUseItem(EntityPlayer player, ItemStack stack) {
        if (stack.isEmpty()) {
            return true;
        }

        RequirementHolder lock = getSkillLock(stack);
        if (lock == null) {
            return true;
        }

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
        if (isFake(event)) {
            return;
        }
        enforce(event);

        if (!event.isCanceled()) {
            EntityPlayer player = event.getEntityPlayer();
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            Block block = state.getBlock();
            int meta = state.getBlock().getMetaFromState(state);
            ItemStack stack = new ItemStack(state.getBlock(), 1, meta);
            if (stack.isEmpty()) {
                stack = block.getItem(event.getWorld(), event.getPos(), state);
            }

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
        enforce(event);
    }

    @SubscribeEvent
    public static void rightClickBlock(RightClickBlock event) {
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        Block block = state.getBlock();
        int meta = state.getBlock().getMetaFromState(state);
        ItemStack stack = new ItemStack(block, 1, meta);
        if (stack.isEmpty()) {
            stack = block.getItem(event.getWorld(), event.getPos(), state);
        }
        if (ConfigHandler.enforceFakePlayers) {
            if (!player.isCreative() && !canPlayerUseItem(player, stack)) {
                event.setUseBlock(Result.DENY);
                event.setUseItem(player.isSneaking() ? Result.DEFAULT : Result.DENY);
                event.setCanceled(true);
                if (!isFake(player)) {
                    tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_USE_LOCKED);
                }
            }
        } else if (!isFake(player) && !player.isCreative() && !canPlayerUseItem(player, stack)) {
            tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_USE_LOCKED);
            event.setUseBlock(Result.DENY);
            event.setUseItem(player.isSneaking() ? Result.DEFAULT : Result.DENY);
            event.setCanceled(true);
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
        enforce(event);
    }

    @SubscribeEvent
    public static void tick(PlayerTickEvent event) {
        if (!event.player.isCreative() && !isFake(event.player)) {
            for (int i = 0; i < event.player.inventory.armorInventory.size(); i++) {
                ItemStack stack = event.player.inventory.armorInventory.get(i);
                if (!stack.isEmpty() && !canPlayerUseItem(event.player, stack)) {
                    ItemStack copy = stack.copy();
                    if (!event.player.inventory.addItemStackToInventory(copy)) {
                        event.player.dropItem(copy, false);
                    }
                    event.player.inventory.armorInventory.set(i, ItemStack.EMPTY);
                    tellPlayer(event.player, stack, MessageLockedItem.MSG_ARMOR_EQUIP_LOCKED);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDrops(LivingDropsEvent event) {
        if (ConfigHandler.disableSheepWool && event.getEntity() instanceof EntitySheep) {
            event.getDrops().removeIf((e) -> e.getItem().getItem() == Item.getItemFromBlock(Blocks.WOOL));
        }
    }

    private static boolean isFake(EntityEvent e) {
        return isFake(e.getEntity());
    }

    private static boolean isFake(Entity e) {
        return e instanceof FakePlayer;
    }

    private static void enforce(PlayerInteractEvent event) {
        if (event.isCanceled()) {
            return;
        }

        EntityPlayer player = event.getEntityPlayer();
        if (player.isCreative()) {
            return;
        }

        ItemStack stack = event.getItemStack();
        if (ConfigHandler.enforceFakePlayers) {
            if (!canPlayerUseItem(player, stack)) {
                event.setCanceled(true);
                if (!isFake(player)){
                    tellPlayer(player, stack, MessageLockedItem.MSG_ITEM_LOCKED);
                }
            }
        } else if (!isFake(player) && !canPlayerUseItem(player, stack)){
            event.setCanceled(true);
            tellPlayer(player, stack, MessageLockedItem.MSG_ITEM_LOCKED);
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

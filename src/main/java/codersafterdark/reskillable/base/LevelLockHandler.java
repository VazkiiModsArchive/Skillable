package codersafterdark.reskillable.base;

import codersafterdark.reskillable.api.ReskillableAPI;
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
import org.apache.logging.log4j.Level;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LevelLockHandler {

    public static final String[] DEFAULT_SKILL_LOCKS = new String[]{"minecraft:iron_shovel:*=reskillable:gathering|5", "minecraft:iron_axe:*=reskillable:gathering|5", "minecraft:iron_sword:*=reskillable:attack|5", "minecraft:iron_pickaxe:*=reskillable:mining|5", "minecraft:iron_hoe:*=reskillable:farming|5", "minecraft:iron_helmet:*=reskillable:defense|5", "minecraft:iron_chestplate:*=reskillable:defense|5", "minecraft:iron_leggings:*=reskillable:defense|5", "minecraft:iron_boots:*=reskillable:defense|5", "minecraft:golden_shovel:*=reskillable:gathering|5,reskillable:magic|5", "minecraft:golden_axe:*=reskillable:gathering|5,reskillable:magic|5", "minecraft:golden_sword:*=reskillable:attack|5,reskillable:magic|5", "minecraft:golden_pickaxe:*=reskillable:mining|5,reskillable:magic|5", "minecraft:golden_hoe:*=reskillable:farming|5,reskillable:magic|5", "minecraft:golden_helmet:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_chestplate:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_leggings:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_boots:*=reskillable:defense|5,reskillable:magic|5", "minecraft:diamond_shovel:*=reskillable:gathering|16", "minecraft:diamond_axe:*=reskillable:gathering|16", "minecraft:diamond_sword:*=reskillable:attack|16", "minecraft:diamond_pickaxe:*=reskillable:mining|16", "minecraft:diamond_hoe:*=reskillable:farming|16", "minecraft:diamond_helmet:*=reskillable:defense|16", "minecraft:diamond_chestplate:*=reskillable:defense|16", "minecraft:diamond_leggings:*=reskillable:defense|16", "minecraft:diamond_boots:*=reskillable:defense|16", "minecraft:shears:*=reskillable:farming|5,reskillable:gathering|5", "minecraft:fishing_rod:*=reskillable:gathering|8", "minecraft:shield:*=reskillable:defense|8", "minecraft:bow:*=reskillable:attack|8", "minecraft:ender_pearl=reskillable:magic|8", "minecraft:ender_eye=reskillable:magic|16,reskillable:building|8", "minecraft:elytra:*=reskillable:defense|16,reskillable:agility|24,reskillable:magic|16", "minecraft:lead=reskillable:farming|5", "minecraft:end_crystal=reskillable:building|24,reskillable:magic|32", "minecraft:iron_horse_armor:*=reskillable:defense|5,reskillable:agility|5", "minecraft:golden_horse_armor:*=reskillable:defense|5,reskillable:magic|5,reskillable:agility|5", "minecraft:diamond_horse_armor:*=reskillable:defense|16,reskillable:agility|16", "minecraft:fireworks=reskillable:agility|24", "minecraft:dye:15=reskillable:farming|12", "minecraft:saddle=reskillable:agility|12", "minecraft:redstone=reskillable:building|5", "minecraft:redstone_torch=reskillable:building|5", "minecraft:skull:1=reskillable:building|20,reskillable:attack|20,reskillable:defense|20"};
    private static final Map<LockKey, RequirementHolder> locks = new HashMap<>(); //This should stay private to ensure that it is added to correctly
    public static final EmptyLockKey EMPTY_LOCK_KEY = new EmptyLockKey();
    public static RequirementHolder EMPTY_LOCK = new RequirementHolder();
    private static Map<Class<?>, List<Class<? extends LockKey>>> lockTypesMap = new HashMap<>();
    private static Map<LockKey, Set<NBTLockKey>> nbtLockInfo = new HashMap<>();
    private static RequirementHolder lastLock = EMPTY_LOCK;
    private static ItemStack lastItem;
    private static String[] configLocks;

    public static void loadFromConfig(String[] configValues) {
        configLocks = configValues;
    }

    public static void setupLocks() {
        registerDefaultLockKeys();
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

    private static void registerDefaultLockKeys() {
        registerLockKey(ItemStack.class, ItemInfo.class, ModLockKey.class, GenericNBTLockKey.class);
    }

    /**
     * Registers LockKey class implementations as key types to be automatically checked when calling {@link #getLocks(Object)} on an
     * Object of the type given by lockTypeClass.
     * @param lockTypeClass A class that represents the type of object that the given keyClasses can be built from.
     * @param keyClasses    A list of Classes that implement LockKey, and have a constructor with the parameter with of the type lockTypeClass
     */
    public static void registerLockKey(Class<?> lockTypeClass, Class<? extends LockKey>... keyClasses) {
        for (Class<? extends LockKey> keyClass : keyClasses) {
            try {
                //Should work, it "may" need to attempt to create a "new instance" to make sure it is properly accessible
                //if this ends up being valid and then errors in getLocks
                keyClass.getDeclaredConstructor(lockTypeClass);

                //Add it to the map
                lockTypesMap.computeIfAbsent(lockTypeClass, k -> new ArrayList<>()).add(keyClass);
            } catch (NoSuchMethodException | SecurityException e) {
                ReskillableAPI.getInstance().log(Level.ERROR, keyClass.getSimpleName() + " does not have a constructor with the parameter: " + lockTypeClass.getSimpleName());
                //TODO: Localize the error message (potentially also rephrase it slightly)
            }
        }
    }

    /**
     * Adds locks to the given key.
     * @param key    The key to register the given holder locks against. If the given LockKey type has not been registered
     *               in {@link #registerLockKey(Class, Class[])}, then it will not be able to be automatically retrieved
     *               using {@link #getLocks(Object)}
     * @param holder The RequirementHolder that represents the locks to add.
     */
    public static void addLockByKey(LockKey key, RequirementHolder holder) {
        if (key == null || key.equals(EMPTY_LOCK_KEY)) { //Do not add an empty lock key to the actual map
            return;
        }
        locks.put(key, holder);

        if (key instanceof NBTLockKey) {
            NBTTagCompound tag = ((NBTLockKey) key).getTag();
            if (tag != null) {
                LockKey without = ((NBTLockKey) key).withoutTag();
                if (without == null) {//Use a key that is constant for purposes of retrieving efficiently
                    without = EMPTY_LOCK_KEY;
                }
                //Store the NBT tag in a list for the specific item
                nbtLockInfo.computeIfAbsent(without, k -> new HashSet<>()).add((NBTLockKey) key);
            }
        }

        //Reset the tooltip cache in case the item being hovered is what changed
        lastItem = null;
        lastLock = EMPTY_LOCK;
    }

    public static void addModLock(String modName, RequirementHolder holder) {
        addLockByKey(new ModLockKey(modName), holder);
    }

    public static void addLock(ItemStack stack, RequirementHolder holder) {
        addLockByKey(new ItemInfo(stack), holder);
    }

    public static RequirementHolder getLockByKey(LockKey key) {
        return locks.containsKey(key) ? locks.get(key) : EMPTY_LOCK;
    }

    public static RequirementHolder getSkillLock(ItemStack stack) {
        return stack == null || stack.isEmpty() ? EMPTY_LOCK : getLocks(stack);
    }

    //If this does not return "guess" the correct registered type use below method where you tell it the type
    public static <T> RequirementHolder getLocks(T toCheck) {
        return toCheck == null ? EMPTY_LOCK : getLocks(toCheck.getClass(), toCheck);
    }

    /**
     * Gets all the locks the given object has on it.
     * @param toCheck The object to retrieve the locks of.
     * @param <T>     Represents the type of the object to check, must be registered using {@link #registerLockKey(Class, Class[])}
     * @return A RequirementHolder of all he locks for the given object.
     */
    public static <T> RequirementHolder getLocks(Class<? extends T> classType, T toCheck) {
        if (toCheck == null) {
            return EMPTY_LOCK;
        }

        if (!lockTypesMap.containsKey(classType)) {
            return EMPTY_LOCK;
        }

        List<Class<? extends LockKey>> lockTypes = lockTypesMap.get(classType);
        List<RequirementHolder> requirements = new ArrayList<>();

        for (Class<? extends LockKey> keyClass : lockTypes) {
            LockKey lock;
            //We know that this constructor exists because of checks done during registering lock types
            try {
                lock = keyClass.getDeclaredConstructor(classType).newInstance(toCheck);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                //Failed to find method initializer (this should not happen because of the checks done in registerLockKey
                e.printStackTrace();
                continue;
            }

            if (lock instanceof NBTLockKey) {
                LockKey baseLock = ((NBTLockKey) lock).withoutTag();
                if (baseLock == null) {
                    //If there is no base lock then use a representation for getting partial locks in general
                    //Used for GenericNBTLockKey but may also be needed for custom NBTLockKey objects
                    baseLock = EMPTY_LOCK_KEY;
                }

                //getNBTLocks may be an empty list but that is fine as then no elements will be added
                requirements.addAll(getNBTLocks(((NBTLockKey) lock).getTag(), nbtLockInfo.get(baseLock)));

                //Ignore NBT for checking if there is a lock without NBT
                lock = baseLock; //Below if should always be false if it is EMPTY_LOCK_KEY
            }
            //Add the base for the item
            if (locks.containsKey(lock)) {
                requirements.add(locks.get(lock));
            }
        }

        return requirements.isEmpty() ? EMPTY_LOCK : new RequirementHolder(requirements.toArray(new RequirementHolder[0]));
    }

    private static List<RequirementHolder> getNBTLocks(NBTTagCompound tag, Set<NBTLockKey> nbtItemLookup) {
        if (tag == null || nbtItemLookup == null) { //If there is no tag for the item just return no lock
            return new ArrayList<>();
        }
        List<LockKey> partialLocks = new ArrayList<>();
        for (NBTLockKey nbtLock : nbtItemLookup) {
            if (compareNBT(tag, nbtLock.getTag()) >= 0) { //Build up the best match
                partialLocks.add(nbtLock);
            }
        }
        return partialLocks.stream().filter(locks::containsKey).map(locks::get).collect(Collectors.toList());
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
                    if (IntStream.of(fIntArray).noneMatch(i -> i == pint)) {
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
        enforce(event);
        if (event.isCanceled()) {
            return;
        }
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

    public static boolean isFake(EntityEvent e) {
        return isFake(e.getEntity());
    }

    public static boolean isFake(Entity e) {
        return e instanceof FakePlayer;
    }

    public static void enforce(PlayerInteractEvent event) {
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
                if (!isFake(player)) {
                    tellPlayer(player, stack, MessageLockedItem.MSG_ITEM_LOCKED);
                }
            }
        } else if (!isFake(player) && !canPlayerUseItem(player, stack)) {
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

    //This is only used internally so store it here so that other places can only reference it if they need to but can not create a new instance
    public static final class EmptyLockKey implements LockKey {
        private EmptyLockKey() {

        }
    }
}

package codersafterdark.reskillable.base;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.data.*;
import codersafterdark.reskillable.base.configs.ConfigHandler;
import codersafterdark.reskillable.network.MessageLockedItem;
import codersafterdark.reskillable.network.PacketHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static net.minecraftforge.fml.common.eventhandler.EventPriority.HIGH;

public class LevelLockHandler {
    public static final String[] DEFAULT_SKILL_LOCKS = new String[]{"minecraft:iron_shovel:*=reskillable:gathering|5", "minecraft:iron_axe:*=reskillable:gathering|5", "minecraft:iron_sword:*=reskillable:attack|5", "minecraft:iron_pickaxe:*=reskillable:mining|5", "minecraft:iron_hoe:*=reskillable:farming|5", "minecraft:iron_helmet:*=reskillable:defense|5", "minecraft:iron_chestplate:*=reskillable:defense|5", "minecraft:iron_leggings:*=reskillable:defense|5", "minecraft:iron_boots:*=reskillable:defense|5", "minecraft:golden_shovel:*=reskillable:gathering|5,reskillable:magic|5", "minecraft:golden_axe:*=reskillable:gathering|5,reskillable:magic|5", "minecraft:golden_sword:*=reskillable:attack|5,reskillable:magic|5", "minecraft:golden_pickaxe:*=reskillable:mining|5,reskillable:magic|5", "minecraft:golden_hoe:*=reskillable:farming|5,reskillable:magic|5", "minecraft:golden_helmet:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_chestplate:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_leggings:*=reskillable:defense|5,reskillable:magic|5", "minecraft:golden_boots:*=reskillable:defense|5,reskillable:magic|5", "minecraft:diamond_shovel:*=reskillable:gathering|16", "minecraft:diamond_axe:*=reskillable:gathering|16", "minecraft:diamond_sword:*=reskillable:attack|16", "minecraft:diamond_pickaxe:*=reskillable:mining|16", "minecraft:diamond_hoe:*=reskillable:farming|16", "minecraft:diamond_helmet:*=reskillable:defense|16", "minecraft:diamond_chestplate:*=reskillable:defense|16", "minecraft:diamond_leggings:*=reskillable:defense|16", "minecraft:diamond_boots:*=reskillable:defense|16", "minecraft:shears:*=reskillable:farming|5,reskillable:gathering|5", "minecraft:fishing_rod:*=reskillable:gathering|8", "minecraft:shield:*=reskillable:defense|8", "minecraft:bow:*=reskillable:attack|8", "minecraft:ender_pearl=reskillable:magic|8", "minecraft:ender_eye=reskillable:magic|16,reskillable:building|8", "minecraft:elytra:*=reskillable:defense|16,reskillable:agility|24,reskillable:magic|16", "minecraft:lead=reskillable:farming|5", "minecraft:end_crystal=reskillable:building|24,reskillable:magic|32", "minecraft:iron_horse_armor:*=reskillable:defense|5,reskillable:agility|5", "minecraft:golden_horse_armor:*=reskillable:defense|5,reskillable:magic|5,reskillable:agility|5", "minecraft:diamond_horse_armor:*=reskillable:defense|16,reskillable:agility|16", "minecraft:fireworks=reskillable:agility|24", "minecraft:dye:15=reskillable:farming|12", "minecraft:saddle=reskillable:agility|12", "minecraft:redstone=reskillable:building|5", "minecraft:redstone_torch=reskillable:building|5", "minecraft:skull:1=reskillable:building|20,reskillable:attack|20,reskillable:defense|20"};
    private static final Map<LockKey, RequirementHolder> locks = new HashMap<>(); //This should stay private to ensure that it is added to correctly
    public static RequirementHolder EMPTY_LOCK = new RequirementHolder();
    private static Map<Class<?>, List<Class<? extends LockKey>>> lockTypesMap = new HashMap<>();
    private static Map<LockKey, Set<FuzzyLockKey>> fuzzyLockInfo = new HashMap<>();
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
     * Registers LockKey class implementations as key types to be automatically checked when calling {@link #getLocks(Class, Object[])} on an
     * Object of the type given by lockTypeClass.
     *
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
     *
     * @param key    The key to register the given holder locks against. If the given LockKey type has not been registered
     *               in {@link #registerLockKey(Class, Class[])}, then it will not be able to be automatically retrieved
     *               using {@link #getLocks(Class, Object[])}
     * @param holder The RequirementHolder that represents the locks to add.
     */
    public static void addLockByKey(LockKey key, RequirementHolder holder) {
        if (key == null || key instanceof GenericLockKey) { //Do not add an empty lock key to the actual map
            return;
        }
        if (holder == null || holder.equals(EMPTY_LOCK) || holder.getRestrictionLength() == 0) {
            //If the holder is invalid or has no data don't actually add it (most likely caused by requirements being disabled or being invalid)
            return;
        }
        locks.put(key, holder);

        if (key instanceof FuzzyLockKey) {
            FuzzyLockKey fuzzy = (FuzzyLockKey) key;
            if (!fuzzy.isNotFuzzy()) {
                LockKey without = fuzzy.getNotFuzzy();
                if (without == null) {//Use a key that is more specialized for purposes of retrieving efficiently
                    without = new GenericLockKey(key.getClass());
                }
                //Store the fuzzy instance in a list for the specific item
                fuzzyLockInfo.computeIfAbsent(without, k -> new HashSet<>()).add(fuzzy);
            }
        }

        //Reset the tooltip cache in case the item being hovered is what changed
        ToolTipHandler.resetLast();
    }

    public static void addModLock(String modName, RequirementHolder holder) {
        addLockByKey(new ModLockKey(modName), holder);
    }

    public static void addLock(ItemStack stack, RequirementHolder holder) {
        addLockByKey(new ItemInfo(stack), holder);
    }

    public static RequirementHolder getLockByKey(LockKey key) {
        if (locks.containsKey(key)) {
            RequirementHolder holder = locks.get(key);
            if (!holder.hasNone()) {
                return holder;
            }
        }
        return EMPTY_LOCK;
    }

    public static RequirementHolder getLockByFuzzyKey(FuzzyLockKey key) {
        List<RequirementHolder> requirements = getFuzzyRequirements(key);
        if (requirements.isEmpty()) {
            return EMPTY_LOCK;
        }
        RequirementHolder holder = new RequirementHolder(requirements.toArray(new RequirementHolder[0]));
        return holder.hasNone() ? EMPTY_LOCK : holder;
    }

    private static List<RequirementHolder> getFuzzyRequirements(FuzzyLockKey key) {
        List<RequirementHolder> requirements = new ArrayList<>();
        if (!key.isNotFuzzy()) {
            LockKey baseLock = key.getNotFuzzy();
            if (baseLock == null) {
                //If there is no base lock then use a representation for getting partial locks in general
                baseLock = new GenericLockKey(key.getClass());
            } else if (locks.containsKey(baseLock)) {
                //Add the base lock's requirements
                RequirementHolder holder = locks.get(baseLock);
                if (holder.hasNone()) {
                    return Collections.singletonList(holder);
                }
                requirements.add(holder);
            }

            Set<FuzzyLockKey> fuzzyLookup = fuzzyLockInfo.get(baseLock);
            if (fuzzyLookup != null) {
                for (FuzzyLockKey fuzzyLock : fuzzyLookup) {
                    if (key.fuzzyEquals(fuzzyLock) && locks.containsKey(fuzzyLock)) { //Build up the best match
                        //fuzzy is the given object and has all info and fuzzyLock is the partial information
                        RequirementHolder holder = locks.get(fuzzyLock);
                        if (holder.hasNone()) {
                            return Collections.singletonList(holder);
                        }
                        requirements.add(holder);
                    }
                }
            }
        } else if (locks.containsKey(key)) {
            RequirementHolder holder = locks.get(key);
            if (holder.hasNone()) {
                return Collections.singletonList(holder);
            }
            requirements.add(holder);
        }
        return requirements;
    }

    public static RequirementHolder getSkillLock(ItemStack stack) {
        return stack == null || stack.isEmpty() ? EMPTY_LOCK : getLocks(ItemStack.class, stack);
    }

    /**
     * Gets all the locks the given object has on it.
     *
     * @param tToCheck A list of objects to retrieve the combined locks of.
     * @param <T>      Represents the type of the objects to check, must be registered using {@link #registerLockKey(Class, Class[])}
     * @return A RequirementHolder of all he locks for the given object.
     */
    @SafeVarargs
    public static <T> RequirementHolder getLocks(Class<? extends T> classType, T... tToCheck) {
        if (tToCheck == null || tToCheck.length == 0 || !lockTypesMap.containsKey(classType)) {
            return EMPTY_LOCK;
        }

        List<Class<? extends LockKey>> lockTypes = lockTypesMap.get(classType);
        List<RequirementHolder> requirements = new ArrayList<>();
        for (T toCheck : tToCheck) {
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

                if (lock instanceof FuzzyLockKey) {
                    requirements.addAll(getFuzzyRequirements((FuzzyLockKey) lock));
                } else if (locks.containsKey(lock)) {
                    RequirementHolder holder = locks.get(lock);
                    if (holder.hasNone()) {
                        return EMPTY_LOCK;
                    }
                    requirements.add(holder);
                }
                if (lock instanceof ParentLockKey) {
                    RequirementHolder subLocks = ((ParentLockKey) lock).getSubRequirements();
                    if (subLocks != null && !subLocks.equals(EMPTY_LOCK)) {
                        if (subLocks.hasNone()) {
                            return EMPTY_LOCK;
                        }
                        requirements.add(subLocks);
                    }
                }
            }
        }

        if (requirements.isEmpty()) {
            return EMPTY_LOCK;
        }
        RequirementHolder holder = new RequirementHolder(requirements.toArray(new RequirementHolder[0]));
        return holder.hasNone() ? EMPTY_LOCK : holder;
    }

    public static boolean canPlayerUseItem(EntityPlayer player, ItemStack stack) {
        RequirementHolder lock = getSkillLock(stack);
        return lock.equals(EMPTY_LOCK) || PlayerDataHandler.get(player).matchStats(lock);
    }

    @SubscribeEvent(priority = HIGH)
    public static void hurtEvent(LivingAttackEvent event) {
        if (!event.isCanceled() && event.getSource().getTrueSource() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
            genericEnforce(event, player, player.getHeldItemMainhand(), MessageLockedItem.MSG_ITEM_LOCKED);
        }
    }

    @SubscribeEvent(priority = HIGH)
    public static void leftClick(LeftClickBlock event) {
        enforce(event);
        if (event.isCanceled()) {
            return;
        }
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        Block block = state.getBlock();
        ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
        if (stack.isEmpty()) {
            stack = block.getItem(event.getWorld(), event.getPos(), state);
        }
        if (block.hasTileEntity(state)) {
            TileEntity te = event.getWorld().getTileEntity(event.getPos());
            if (te != null && !te.isInvalid()) {
                NBTTagCompound tag = new NBTTagCompound();
                te.writeToNBT(tag);
                stack.setTagCompound(tag);
            }
        }
        genericEnforce(event, event.getEntityPlayer(), stack, MessageLockedItem.MSG_BLOCK_BREAK_LOCKED);
    }

    @SubscribeEvent(priority = HIGH)
    public static void rightClickItem(RightClickItem event) {
        enforce(event);
    }

    @SubscribeEvent(priority = HIGH)
    public static void rightClickBlock(RightClickBlock event) {
        enforce(event);
        if (event.isCanceled()) {
            return;
        }
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        Block block = state.getBlock();
        ItemStack stack = new ItemStack(block, 1, state.getBlock().getMetaFromState(state));
        if (stack.isEmpty()) {
            stack = block.getItem(event.getWorld(), event.getPos(), state);
        }
        if (block.hasTileEntity(state)) {
            TileEntity te = event.getWorld().getTileEntity(event.getPos());
            if (te != null && !te.isInvalid()) {
                NBTTagCompound tag = new NBTTagCompound();
                te.writeToNBT(tag);
                stack.setTagCompound(tag);
            }
        }
        genericEnforce(event, event.getEntityPlayer(), stack, MessageLockedItem.MSG_BLOCK_USE_LOCKED);
    }

    @SubscribeEvent(priority = HIGH)
    public static void onBlockBreak(BreakEvent event) {
        if (event.isCanceled()) {
            return;
        }
        IBlockState state = event.getWorld().getBlockState(event.getPos());
        ItemStack stack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
        if (state.getBlock().hasTileEntity(state)) {
            TileEntity te = event.getWorld().getTileEntity(event.getPos());
            if (te != null && !te.isInvalid()) {
                NBTTagCompound tag = new NBTTagCompound();
                te.writeToNBT(tag);
                stack.setTagCompound(tag);
            }
        }
        genericEnforce(event, event.getPlayer(), stack, MessageLockedItem.MSG_BLOCK_BREAK_LOCKED);
    }

    @SubscribeEvent(priority = HIGH)
    public static void entityInteract(EntityInteract event) {
        enforce(event);
    }

    @SubscribeEvent
    public static void onArmorEquip(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            if (!player.isCreative() && !isFake(player)) {
                EntityEquipmentSlot slot = event.getSlot();
                if (slot.getSlotType().equals(EntityEquipmentSlot.Type.ARMOR)) {
                    ItemStack stack = player.inventory.armorInventory.get(slot.getIndex());
                    if (!canPlayerUseItem(player, stack)) {
                        if (!player.inventory.addItemStackToInventory(stack)) {
                            player.dropItem(stack, false);
                        }
                        player.inventory.armorInventory.set(slot.getIndex(), ItemStack.EMPTY);
                        tellPlayer(player, stack, MessageLockedItem.MSG_ARMOR_EQUIP_LOCKED);
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = HIGH)
    public static void onEntityDrops(LivingDropsEvent event) {
        if (!event.isCanceled() && codersafterdark.reskillable.base.configs.ConfigHandler.disableSheepWool && event.getEntity() instanceof EntitySheep) {
            event.getDrops().removeIf(e -> e.getItem().getItem() == Item.getItemFromBlock(Blocks.WOOL));
        }
    }

    public static boolean isFake(Entity e) {
        return e instanceof FakePlayer;
    }

    public static void enforce(PlayerInteractEvent event) {
        genericEnforce(event, event.getEntityPlayer(), event.getItemStack(), MessageLockedItem.MSG_ITEM_LOCKED);
    }

    public static void genericEnforce(Event event, EntityPlayer player, ItemStack stack, String lockMessage) {
        if (!event.isCancelable() || event.isCanceled() || player == null || stack == null || stack.isEmpty() || player.isCreative()) {
            return;
        }
        if (ConfigHandler.enforceFakePlayers) {
            if (!canPlayerUseItem(player, stack)) {
                event.setCanceled(true);
                if (!isFake(player)) {
                    tellPlayer(player, stack, lockMessage);
                }
            }
        } else if (!isFake(player) && !canPlayerUseItem(player, stack)) {
            tellPlayer(player, stack, lockMessage);
            event.setCanceled(true);
        }
    }

    //TODO: Make this properly handle custom requirements instead of just not displaying them and showing that it should be met
    public static void tellPlayer(EntityPlayer player, ItemStack stack, String msg) {
        if (player instanceof EntityPlayerMP) {
            PacketHandler.INSTANCE.sendTo(new MessageLockedItem(stack, msg), (EntityPlayerMP) player);
        }
    }
}
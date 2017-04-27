package vazkii.skillable.base;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.network.NetworkHandler;
import vazkii.skillable.network.MessageLockedItem;

public class LevelLockHandler {

	private static final Map<String, RequirementHolder> locks = new HashMap();
	public static RequirementHolder EMPTY_LOCK = new RequirementHolder();
	
	public static final String[] DEFAULT_SKILL_LOCKS = new String[] {
			"minecraft:iron_shovel=gathering:5",
			"minecraft:iron_axe=gathering:5",
			"minecraft:iron_sword=attack:5",
			"minecraft:iron_pickaxe=mining:5",
			"minecraft:iron_hoe=farming:5",
			"minecraft:iron_helmet=defense:5",
			"minecraft:iron_chestplate=defense:5",
			"minecraft:iron_leggings=defense:5",
			"minecraft:iron_boots=defense:5",
			"minecraft:golden_shovel=gathering:5,magic:5",
			"minecraft:golden_axe=gathering:5,magic:5",
			"minecraft:golden_sword=attack:5,magic:5",
			"minecraft:golden_pickaxe=mining:5,magic:5",
			"minecraft:golden_hoe=farming:5,magic:5",
			"minecraft:golden_helmet=defense:5,magic:5",
			"minecraft:golden_chestplate=defense:5,magic:5",
			"minecraft:golden_leggings=defense:5,magic:5",
			"minecraft:golden_boots=defense:5,magic:5",
			"minecraft:diamond_shovel=gathering:16",
			"minecraft:diamond_axe=gathering:16",
			"minecraft:diamond_sword=attack:16",
			"minecraft:diamond_pickaxe=mining:16",
			"minecraft:diamond_hoe=farming:16",
			"minecraft:diamond_helmet=defense:16",
			"minecraft:diamond_chestplate=defense:16",
			"minecraft:diamond_leggings=defense:16",
			"minecraft:diamond_boots=defense:16",
			"minecraft:shears=farming:5,gathering:5",
			"minecraft:fishing_rod=gathering:8",
			"minecraft:shield=defense:8",
			"minecraft:bow=attack:8",
			"minecraft:ender_pearl=magic:8",
			"minecraft:ender_eye=magic:16,building:8",
			"minecraft:elytra=defense:16,agility:24,magic:16",
			"minecraft:lead=farming:5",
			"minecraft:end_crystal=building:24,magic:32",
			"minecraft:iron_horse_armor=defense:5,agility:5",
			"minecraft:golden_horse_armor=defense:5,magic:5,agility:5",
			"minecraft:diamond_horse_armor=defense:16,agility:16",
			"minecraft:fireworks=agility:24",
			"minecraft:dye:15=farming:12",
			"minecraft:saddle=agility:8",
			"minecraft:redstone=building:5",
			"minecraft:redstone_torch=building:5"
	};
	
	public static void loadFromConfig(String[] configValues) {
		locks.clear();
		for(String s : configValues) {
			String[] tokens = s.split("=");
			if(tokens.length == 2) {
				RequirementHolder h = RequirementHolder.fromString(tokens[1]);
				locks.put(tokens[0].toLowerCase(), h);
			}
		}
	}

	public static RequirementHolder getSkillLock(ItemStack stack) {
		if(stack == null || stack.isEmpty())
			return EMPTY_LOCK;

		String itemName = stack.getItem().getRegistryName().toString();
		String metaName = itemName + ":" + stack.getMetadata();
		RequirementHolder lock = getSkillLock(metaName);
		if(lock != EMPTY_LOCK)
			return lock;

		lock = getSkillLock(itemName);
		return lock;
	}

	public static RequirementHolder getSkillLock(String key) {
		if(locks.containsKey(key)) {
			RequirementHolder lock = locks.get(key);
			if(lock.isRealLock())
				return lock;
		}

		return EMPTY_LOCK;
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
		if(event.getSource().getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
			ItemStack stack = player.getHeldItemMainhand();
			if(!player.isCreative() && !canPlayerUseItem(player, stack)) {
				tellPlayer(player, stack, MessageLockedItem.MSG_ITEM_LOCKED);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void leftClick(LeftClickBlock event) {
		enforce(event);
		
		if(!event.isCanceled()) {
			EntityPlayer player = event.getEntityPlayer();
			IBlockState state = event.getWorld().getBlockState(event.getPos());
			int meta = state.getBlock().getMetaFromState(state);
			ItemStack stack = new ItemStack(state.getBlock(), 1, meta);
			if(!player.isCreative() && !canPlayerUseItem(player, stack)) {
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
		if(!event.getEntityPlayer().isSneaking()) {
			enforce(event);
			if(!event.isCanceled()) {
				EntityPlayer player = event.getEntityPlayer();
				IBlockState state = event.getWorld().getBlockState(event.getPos());
				int meta = state.getBlock().getMetaFromState(state);
				ItemStack stack = new ItemStack(state.getBlock(), 1, meta);
				if(!player.isCreative() && !canPlayerUseItem(player, stack)) {
					tellPlayer(player, stack, MessageLockedItem.MSG_BLOCK_USE_LOCKED);
					event.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public static void entityInteract(EntityInteract event) {
		enforce(event);
	}

	@SubscribeEvent
	public static void tick(PlayerTickEvent event) {
		if(!event.player.isCreative())
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
			event.getDrops().removeIf((e) -> e.getEntityItem().getItem() == Item.getItemFromBlock(Blocks.WOOL));
	}

	private static void enforce(PlayerInteractEvent event) {
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
			NetworkHandler.INSTANCE.sendTo(message, (EntityPlayerMP) player);
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

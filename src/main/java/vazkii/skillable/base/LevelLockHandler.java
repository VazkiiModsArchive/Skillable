package vazkii.skillable.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
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
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.Skills;

public class LevelLockHandler {

	private static final Map<String, Map<Skill, Integer>> skillLocks = new HashMap();

	public static final String[] DEFAULT_SKILL_LOCKS = new String[] {
			"minecraft:iron_shovel=gathering:5",
			"minecraft:iron_axe=gathering:5",
			"minecraft:iron_sword=attack:5",
			"minecraft:iron_pickaxe=mining:5",
			"minecraft:iron_hoe=farming:5",
			"minecraft:shears=farming:5",
			"minecraft:iron_helmet=defense:5",
			"minecraft:iron_chestplate=defense:5",
			"minecraft:iron_leggings=defense:5",
			"minecraft:iron_boots=defense:5",
	};

	public static void loadFromConfig(String[] configValues) {
		skillLocks.clear();
		for(String s : configValues) {
			String[] tokens = s.split("=");
			if(tokens.length == 2) {
				String key = tokens[0];
				String skills = tokens[1];
				tokens = skills.split(",");

				Map<Skill, Integer> lock = new TreeMap();
				for(String s1 : tokens) {
					String[] kv = s1.split(":");
					if(kv.length == 2) {
						String skillStr = kv[0];
						String levelStr = kv[1];

						try {
							int level = Integer.parseInt(levelStr);
							Skill skill = Skills.ALL_SKILLS.get(skillStr);
							if(skill != null && level > 1)
								lock.put(skill, level);
						} catch(NumberFormatException e) { }
					}
				}

				skillLocks.put(key, lock);
			}
		}
	}

	public static Map<Skill, Integer> getSkillLock(ItemStack stack) {
		if(stack == null || stack.isEmpty())
			return null;

		String itemName = stack.getItem().getRegistryName().toString();
		String metaName = itemName + ":" + stack.getMetadata();
		Map<Skill, Integer> lock = getSkillLock(metaName);
		if(lock != null)
			return lock;

		lock = getSkillLock(itemName);
		return lock;
	}

	public static Map<Skill, Integer> getSkillLock(String key) {
		if(skillLocks.containsKey(key))
			return skillLocks.get(key);

		return null;
	}

	public static boolean canPlayerUseItem(EntityPlayer player, ItemStack stack) {
		if(stack.isEmpty())
			return true;

		Map<Skill, Integer> lock = getSkillLock(stack);
		if(lock == null)
			return true;

		PlayerData data = PlayerDataHandler.get(player);
		for(Skill skill : lock.keySet()) {
			PlayerSkillInfo info = data.getSkillInfo(skill);
			if(info.getLevel() < lock.get(skill))
				return false;
		}

		return true;
	}

	@SubscribeEvent
	public static void hurtEvent(LivingAttackEvent event) {
		if(event.getSource().getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getEntity();
			if(!player.isCreative() && !canPlayerUseItem(player, player.getHeldItemMainhand()))
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void leftClick(LeftClickBlock event) {
		enforce(event);
	}

	@SubscribeEvent
	public static void rightClickItem(RightClickItem event) {
		enforce(event);
	}

	@SubscribeEvent
	public static void rightClickBlock(RightClickBlock event) {
		if(!event.getEntityPlayer().isSneaking())
			enforce(event);
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
				}
			}
	}

	private static void enforce(PlayerInteractEvent event) {
		if(event.getEntityPlayer().isCreative())
			return;

		ItemStack stack = event.getItemStack();
		if(!canPlayerUseItem(event.getEntityPlayer(), stack))
			event.setCanceled(true);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onTooltip(ItemTooltipEvent event) {
		Map<Skill, Integer> lock = getSkillLock(event.getItemStack());
		PlayerData data = PlayerDataHandler.get(Minecraft.getMinecraft().player);
		addRequirementsToTooltip(data, lock, event.getToolTip());
	}
	
	@SideOnly(Side.CLIENT)
	public static void addRequirementsToTooltip(PlayerData data, Map<Skill, Integer> lock, List<String> tooltip) {
		if(lock == null || lock.isEmpty())
			return;
		
		tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("skillable.misc.skillLock"));
		for(Skill s : lock.keySet()) {
			PlayerSkillInfo info = data.getSkillInfo(s);
			TextFormatting color = TextFormatting.GREEN;
			int req = lock.get(s);
			if(info.getLevel() < req)
				color = TextFormatting.RED;

			tooltip.add(String.format(TextFormatting.GRAY + " - %s%d %s", color, req, s.getName()));
		}
	}

}

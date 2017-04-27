package vazkii.skillable.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.Skills;
import vazkii.skillable.skill.base.Trait;

public class RequirementHolder {

	public final Map<Skill, Integer> skillLevels = new HashMap();
	public final List<Achievement> achievements = new ArrayList();

	public boolean isRealLock() {
		return getRestrictionLength() > 0;
	}

	public int getRestrictionLength() {
		return skillLevels.size() + achievements.size();
	}

	@SideOnly(Side.CLIENT)
	public void addRequirementsToTooltip(PlayerData data, List<String> tooltip) {
		if(!isRealLock())
			return;

		tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("skillable.misc.skillLock"));
		for(Skill s : skillLevels.keySet()) {
			PlayerSkillInfo info = data.getSkillInfo(s);
			TextFormatting color = TextFormatting.GREEN;
			int req = skillLevels.get(s);
			if(info.getLevel() < req)
				color = TextFormatting.RED;

			tooltip.add(TextFormatting.GRAY + " - " + I18n.translateToLocalFormatted("skillable.misc.skillFormat", color, req, s.getName()));
		}

		EntityPlayer p = data.playerWR.get();
		if(p != null)
			for(Achievement e : achievements)
				tooltip.add(TextFormatting.GRAY + " - " + I18n.translateToLocalFormatted("skillable.misc.achievementFormat", e.getStatName().getUnformattedText()));
	}

	public static RequirementHolder fromString(String s) {
		RequirementHolder holder = new RequirementHolder();
		String[] tokens = s.trim().split(",");

		for(String s1 : tokens) {
			String[] kv = s1.split(":");
			if(kv.length == 2) {
				String keyStr = kv[0];
				String valStr = kv[1];

				System.out.println(keyStr);
				if(keyStr.equals("ach")) {
					StatBase stat = StatList.getOneShotStat(valStr);
					if(stat == null)
						stat = StatList.getOneShotStat("achievement." + valStr);
					
					if(stat instanceof Achievement)
						holder.achievements.add((Achievement) stat);
				} else try {
					int level = Integer.parseInt(valStr);
					Skill skill = Skills.ALL_SKILLS.get(keyStr.toLowerCase());
					System.out.println("locking " + skill.getName() + " " + level);
					if(skill != null && level > 1) 
						holder.skillLevels.put(skill, level);
					else FMLLog.warning("[Skillable] Invalid Level Lock: " + s);
				} catch(NumberFormatException e) { 
					FMLLog.warning("[Skillable] Invalid Level Lock: " + s);
				}
			}
		}

		return holder;
	}



}

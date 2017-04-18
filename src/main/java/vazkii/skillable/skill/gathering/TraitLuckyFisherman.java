package vazkii.skillable.skill.gathering;

import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import vazkii.skillable.skill.base.Trait;

public class TraitLuckyFisherman extends Trait {

	public TraitLuckyFisherman() {
		super("lucky_fisherman", 3, 2, 6);
	}
	
	@Override
	public void onPlayerTick(PlayerTickEvent event) { 
		if(event.player.fishEntity != null)
			event.player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 10, 0, true, true));
	}

}

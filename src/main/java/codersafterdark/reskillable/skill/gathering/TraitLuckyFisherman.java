package codersafterdark.reskillable.skill.gathering;

import codersafterdark.reskillable.skill.base.Trait;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class TraitLuckyFisherman extends Trait {

    public TraitLuckyFisherman() {
        super("lucky_fisherman", 3, 2, 6, "gathering:12,magic:4");
    }

    @Override
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.player.fishEntity != null)
            event.player.addPotionEffect(new PotionEffect(MobEffects.LUCK, 10, 0, true, true));
    }

}

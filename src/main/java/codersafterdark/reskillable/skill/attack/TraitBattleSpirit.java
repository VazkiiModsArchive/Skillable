package codersafterdark.reskillable.skill.attack;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class TraitBattleSpirit extends Trait {

    public TraitBattleSpirit() {
        super("battle_spirit", 3, 2, 6, "attack:16,defense:16,agility:12");
    }

    @Override
    public void onKillMob(LivingDeathEvent event) {
        if (event.getEntity() instanceof IMob)
            ((EntityPlayer) event.getSource().getTrueSource()).addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 120, 0));
    }

}

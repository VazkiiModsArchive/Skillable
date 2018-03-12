package codersafterdark.reskillable.skill.attack;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitBattleSpirit extends Trait {

    public TraitBattleSpirit() {
        super(new ResourceLocation(MOD_ID, "battle_spirit"), 3, 2, new ResourceLocation(MOD_ID, "attack"), 6, "attack:16,defense:16,agility:12");
    }

    @Override
    public void onKillMob(LivingDeathEvent event) {
        if (event.getEntity() instanceof IMob)
            ((EntityPlayer) event.getSource().getTrueSource()).addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 120, 0));
    }

}

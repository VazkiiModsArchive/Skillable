package vazkii.skillable.skill.defense;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.skillable.skill.base.Trait;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TraitEffectTwist extends Trait {

    private Map<Potion, Potion> badPotions;

    public TraitEffectTwist() {
        super("effect_twist", 3, 1, 8, "defense:20,attack:16,magic:16");

        badPotions = new HashMap();
        badPotions.put(MobEffects.SPEED, MobEffects.SLOWNESS);
        badPotions.put(MobEffects.HASTE, MobEffects.MINING_FATIGUE);
        badPotions.put(MobEffects.STRENGTH, MobEffects.WEAKNESS);
        badPotions.put(MobEffects.REGENERATION, MobEffects.POISON);
        badPotions.put(MobEffects.NIGHT_VISION, MobEffects.BLINDNESS);
        badPotions.put(MobEffects.LUCK, MobEffects.UNLUCK);
    }

    @Override
    public void onHurt(LivingHurtEvent event) {
        Entity src = event.getSource().getTrueSource();
        if (src instanceof EntityLivingBase && src instanceof IMob && src.world.rand.nextBoolean()) {
            List<PotionEffect> effects = event.getEntityLiving().getActivePotionEffects().stream().filter((p) -> badPotions.containsKey(p.getPotion())).collect(Collectors.toList());
            if (effects.size() > 0) {
                PotionEffect target = effects.get(src.world.rand.nextInt(effects.size()));
                PotionEffect newEff = new PotionEffect(badPotions.get(target.getPotion()), 80 + src.world.rand.nextInt(60), 0);
                ((EntityLivingBase) src).addPotionEffect(newEff);
            }
        }
    }


}

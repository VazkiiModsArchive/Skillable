package codersafterdark.reskillable.skill.defense;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitEffectTwist extends Trait {

    private Map<Potion, Potion> badPotions;

    public TraitEffectTwist() {
        super(new ResourceLocation(MOD_ID, "effect_twist"), 3, 1, new ResourceLocation(MOD_ID, "defense"),
                8, "reskillable:defense|20", "reskillable:attack|16", "reskillable:magic|16");

        badPotions = new HashMap<>();
        badPotions.put(MobEffects.SPEED, MobEffects.SLOWNESS);
        badPotions.put(MobEffects.HASTE, MobEffects.MINING_FATIGUE);
        badPotions.put(MobEffects.STRENGTH, MobEffects.WEAKNESS);
        badPotions.put(MobEffects.REGENERATION, MobEffects.POISON);
        badPotions.put(MobEffects.NIGHT_VISION, MobEffects.BLINDNESS);
        badPotions.put(MobEffects.LUCK, MobEffects.UNLUCK);
    }

    @Override
    public void onHurt(LivingHurtEvent event) {
        if (event.isCanceled()) {
            return;
        }
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

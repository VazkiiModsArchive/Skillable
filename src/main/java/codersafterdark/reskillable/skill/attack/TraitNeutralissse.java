package codersafterdark.reskillable.skill.attack;

import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.lib.LibObfuscation;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitNeutralissse extends Trait {

    private static final String TAG_DEFUSED = "skillable:defuse";

    public TraitNeutralissse() {
        super(new ResourceLocation(MOD_ID, "neutralissse"), 1, 2, new ResourceLocation(MOD_ID, "attack"),
                10, "reskillable:attack|24", "reskillable:agility|8");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onAttackMob(LivingHurtEvent event) {
        if (event.isCanceled()) {
            return;
        }
        if (event.getEntity() instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) event.getEntity();
            int time = ReflectionHelper.getPrivateValue(EntityCreeper.class, creeper, LibObfuscation.TIME_SINCE_IGNITED);
            if (time < 5) {
                creeper.getEntityData().setInteger(TAG_DEFUSED, 40);
            }
        }
    }

    @SubscribeEvent
    public void entityTick(LivingUpdateEvent event) {
        if (!event.isCanceled() && event.getEntity() instanceof EntityCreeper) {
            EntityCreeper creeper = (EntityCreeper) event.getEntity();
            int defuseTime = creeper.getEntityData().getInteger(TAG_DEFUSED);
            if (defuseTime > 0) {
                creeper.getEntityData().setInteger(TAG_DEFUSED, defuseTime - 1);
                ReflectionHelper.setPrivateValue(EntityCreeper.class, creeper, 6, LibObfuscation.TIME_SINCE_IGNITED);
            }
        }
    }

}

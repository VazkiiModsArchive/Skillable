package vazkii.skillable.skill.attack;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.skillable.lib.LibObfuscation;
import vazkii.skillable.skill.Skills;
import vazkii.skillable.skill.base.Trait;

public class TraitNeutralissse extends Trait {

	private static final String TAG_DEFUSED = "skillable:defuse";
	
	public TraitNeutralissse() {
		super("neutralissse", 1, 2, 10, "attack:24,agility:8");
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@Override
	public void onAttackMob(LivingHurtEvent event) { 
		if(event.getEntity() instanceof EntityCreeper) {
			EntityCreeper creeper = (EntityCreeper) event.getEntity();
			int time = ReflectionHelper.getPrivateValue(EntityCreeper.class, creeper, LibObfuscation.TIME_SINCE_IGNITED);
			if(time < 5)
				creeper.getEntityData().setInteger(TAG_DEFUSED, 40);
		}
	}
	
	@SubscribeEvent
	public void entityTick(LivingUpdateEvent event) {
		if(event.getEntity() instanceof EntityCreeper) {
			EntityCreeper creeper = (EntityCreeper) event.getEntity();
			int defuseTime = creeper.getEntityData().getInteger(TAG_DEFUSED);
			if(defuseTime > 0) {
				creeper.getEntityData().setInteger(TAG_DEFUSED, defuseTime - 1);
				ReflectionHelper.setPrivateValue(EntityCreeper.class, creeper, 6, LibObfuscation.TIME_SINCE_IGNITED);
			}
		}
	}

}

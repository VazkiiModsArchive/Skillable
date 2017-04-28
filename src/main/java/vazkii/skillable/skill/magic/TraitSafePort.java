package vazkii.skillable.skill.magic;

import it.unimi.dsi.fastutil.Stack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import vazkii.skillable.skill.base.Trait;

public class TraitSafePort extends Trait {

	public TraitSafePort() {
		super("safe_port", 1, 1, 6, "magic:20,agility:16,defense:16");
	}
	
	@Override
	public void onHurt(LivingHurtEvent event) { 
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();
		for(StackTraceElement el : trace) // don't hurt me cpw
			if(el.getClassName().equals(EntityEnderPearl.class.getName())) {
				event.setCanceled(true);
				return;
			}
	}

}

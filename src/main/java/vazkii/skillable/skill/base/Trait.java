package vazkii.skillable.skill.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class Trait extends Unlockable implements IAbilityEventHandler {

	public Trait(String name, int x, int y, int cost, String reqs) {
		super(name, x, y, cost, reqs);
	}
}

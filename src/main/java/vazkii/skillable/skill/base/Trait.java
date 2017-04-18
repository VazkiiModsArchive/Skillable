package vazkii.skillable.skill.base;

import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class Trait extends Unlockable implements IAbilityEventHandler {

	public Trait(String name, int x, int y, int cost) {
		super(name, x, y, cost);
	}
	
	@Override
	public void onMobDrops(LivingDropsEvent event) { 
		
	}
	
}

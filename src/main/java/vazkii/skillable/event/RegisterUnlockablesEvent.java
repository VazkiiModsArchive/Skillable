package vazkii.skillable.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import vazkii.skillable.skill.Skill;
import vazkii.skillable.skill.base.Unlockable;

public class RegisterUnlockablesEvent extends Event {
    private Skill skill;

    public RegisterUnlockablesEvent(Skill skill) {
        this.skill = skill;
    }

    public void register(Unlockable unlockable) {
        skill.addUnlockable(unlockable);
    }

    public Skill getSkill() {
        return this.skill;
    }
}
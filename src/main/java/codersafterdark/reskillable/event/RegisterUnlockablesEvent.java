package codersafterdark.reskillable.event;

import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.skill.base.Unlockable;
import net.minecraftforge.fml.common.eventhandler.Event;

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
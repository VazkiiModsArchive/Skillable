package codersafterdark.reskillable.api.event;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class LevelUpEvent extends PlayerEvent {
    private Skill skill;
    private int level;

    protected LevelUpEvent(EntityPlayer player, Skill skill, int level) {
        super(player);
        this.skill = skill;
        this.level = level;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    @Cancelable
    public static class Pre extends LevelUpEvent {
        public Pre(EntityPlayer player, Skill skill, int level) {
            super(player, skill, level);
        }
    }

    public static class Post extends LevelUpEvent {
        public Post(EntityPlayer player, Skill skill, int level) {
            super(player, skill, level);
        }
    }
}

package codersafterdark.reskillable.api.event;

import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

public class LevelUpEvent extends PlayerEvent {
    private Skill skill;
    private int level;
    private int oldLevel;

    protected LevelUpEvent(EntityPlayer player, Skill skill, int level, int oldLevel) {
        super(player);
        this.skill = skill;
        this.level = level;
        this.oldLevel = oldLevel;
    }

    public Skill getSkill() {
        return skill;
    }

    public int getLevel() {
        return level;
    }

    public int getOldLevel() {
        return oldLevel;
    }

    @Cancelable
    public static class Pre extends LevelUpEvent {
        public Pre(EntityPlayer player, Skill skill, int level) {
            this(player, skill, level, level - 1);
        }

        public Pre(EntityPlayer player, Skill skill, int level, int oldLevel) {
            super(player, skill, level, oldLevel);
        }
    }

    public static class Post extends LevelUpEvent {
        public Post(EntityPlayer player, Skill skill, int level) {
            this(player, skill, level, level - 1);
        }

        public Post(EntityPlayer player, Skill skill, int level, int oldLevel) {
            super(player, skill, level, oldLevel);
        }
    }
}

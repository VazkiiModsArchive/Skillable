package codersafterdark.reskillable.skill;

import codersafterdark.reskillable.lib.LibMisc;
import codersafterdark.reskillable.skill.base.Unlockable;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.apache.commons.lang3.tuple.*;

import java.util.ArrayList;
import java.util.List;

public abstract class Skill implements Comparable<Skill> {

    private final String name;
    private final int index;
    private final ResourceLocation background;
    private final ResourceLocation spriteLocation;
    
    private int cap;

    private final List<Unlockable> unlockables = new ArrayList();

    public Skill(String name, int index, ResourceLocation background) {
        this.name = name;
        this.index = index;
        this.background = background;
        this.spriteLocation = new ResourceLocation(LibMisc.MOD_ID, "textures/gui/skills.png");
    }
    public Skill(String name, int index, ResourceLocation background, ResourceLocation spriteLocation) {
        this.name = name;
        this.index = index;
        this.background = background;
        this.spriteLocation = spriteLocation;
    }

    public abstract void initUnlockables();

    public void addUnlockable(Unlockable u) {
        if (u.enabled) {
            unlockables.add(u);
            u.setParentSkill(this);
        }
    }

    public List<Unlockable> getUnlockables() {
        return unlockables;
    }

    public String getKey() {
        return name;
    }

    public String getName() {
        return I18n.translateToLocal("skillable.skill." + getKey());
    }

    public int getIndex() {
        return index;
    }

    public ResourceLocation getBackground() {
        return background;
    }
    
    public int getCap() {
        return cap;
    }
    
    public void setCap(int cap) {
        this.cap = cap;
    }
    
    
    public ResourceLocation getSpriteLocation() {
        return spriteLocation;
    }
    
    public Pair<Integer, Integer> getSpriteFromRank(int rank){
        return new MutablePair<>(176 + Math.min(rank, 3) * 16, 44 + getIndex() * 16);
    }
    
    
    @Override
    public int compareTo(Skill o) {
        return getIndex() - o.getIndex();
    }

}

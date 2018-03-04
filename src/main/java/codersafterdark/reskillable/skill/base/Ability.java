package codersafterdark.reskillable.skill.base;

public class Ability extends Unlockable {

    public Ability(String name, int x, int y, int cost, String reqs) {
        super(name, x, y, cost, reqs);
    }

    @Override
    public boolean hasSpikes() {
        return true;
    }

}

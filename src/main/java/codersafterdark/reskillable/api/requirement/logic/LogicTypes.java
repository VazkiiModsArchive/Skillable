package codersafterdark.reskillable.api.requirement.logic;

public enum LogicTypes {
    NOT("not"),
    OR("or"),
    AND("and"),
    XOR("xor"),
    NOR("nor"),
    NAND("nand"),
    XNOR("xnor");

    private final String name;

    LogicTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
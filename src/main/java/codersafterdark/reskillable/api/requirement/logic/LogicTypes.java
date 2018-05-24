package codersafterdark.reskillable.api.requirement.logic;

public enum LogicTypes {
    NOT("not"),
    AND("and"),
    NAND("nand"),
    OR("or"),
    NOR("nor"),
    XOR("xor"),
    XNOR("xnor");

    private final String name;

    LogicTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
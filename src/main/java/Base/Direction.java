package Base;

public enum Direction {
    NORTH("North"),
    EAST("East"),
    SOUTH("South"),
    WEST("West"),
    UNKNOWN("Unknown");

    private String value;

    Direction(String value) {
        this.value = value;
    }

    public String getDirectionName() {
        return value;
    }
}

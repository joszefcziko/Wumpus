package Base;

public enum TileType {
    EMPTY("_"),
    WALL("W"),
    HERO("H"),
    GOLD("G"),
    WUMPUS("U"),
    PIT("P"),
    UNKNOWN("X");

    private String value;

    TileType(String value) {
        this.value = value;
    }

    public String getTileType() {
        return value;
    }
}

package Base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TileTypeTest {

    @Test
    void getTileType() {
        assertEquals("_", TileType.EMPTY.getTileType());
        assertEquals("W", TileType.WALL.getTileType());
        assertEquals("P", TileType.PIT.getTileType());
        assertEquals("U", TileType.WUMPUS.getTileType());
        assertEquals("H", TileType.HERO.getTileType());
        assertEquals("G", TileType.GOLD.getTileType());
    }
}
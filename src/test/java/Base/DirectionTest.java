package Base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void getDirectionName() {
        assertEquals("North", Direction.NORTH.getDirectionName());
        assertEquals("East", Direction.EAST.getDirectionName());
        assertEquals("South", Direction.SOUTH.getDirectionName());
        assertEquals("West", Direction.WEST.getDirectionName());
    }
}
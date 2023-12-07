package Base;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {

    @Test
    void testConstructor() {
        int size = 5;
        Board gameBoard = new Board(size);
        assertNotNull(gameBoard.getBoard());
        assertEquals(size, gameBoard.getSize());
    }

    @Test
    void testAddElement() {
        Board gameBoard = new Board(10);
        gameBoard.addElement(TileType.HERO,6,3);
        assertEquals(TileType.HERO, gameBoard.getTile(6,3));
    }

    @Test
    void testRemoveElement() {
        Board gameBoard = new Board(10);
        gameBoard.addElement(TileType.HERO,5,5);
        gameBoard.removeElement(5, 5);
        assertEquals(TileType.EMPTY, gameBoard.getTile(5,5));
    }

    @Test
    void testInField() {
        Board gameBoard = new Board(5);
        assertTrue(gameBoard.inField(1, 1));
        assertFalse(gameBoard.inField(-1, 1));
    }

    @Test
    void testOnBoard() {
        Board gameBoard = new Board(5);
        assertTrue(gameBoard.onBoard(1, 1));
        assertFalse(gameBoard.onBoard(-1, 1));
    }

    @Test
    void testMoveHero() {
        Board gameBoard = new Board(5);
        gameBoard.addElement(TileType.HERO,2, 2);
        gameBoard.canMoveHero(3,2);
    }

    @Test
    void testInitializeBoard() {
        int size = 5;
        Board gameBoard = new Board(size);
        gameBoard.Init();
        assertNotNull(gameBoard.getBoard());
    }

    @Test
    void testIsOnBoardPosition() {
        Board gameBoard = new Board(5);
        assertTrue(gameBoard.onBoard(1, 1));
        assertFalse(gameBoard.onBoard(-1, 1));
    }

    @Test
    void testIsInFieldPosition() {
        Board gameBoard = new Board(5);
        assertTrue(gameBoard.inField(1, 1));
        assertFalse(gameBoard.inField(-1, 1));
    }


    @Test
    void testRemoveElementOnValidPosition() {
        Board gameBoard = new Board(5);
        gameBoard.addElement(TileType.GOLD, 2, 2);
        gameBoard.removeElement(2, 2);
        assertEquals(TileType.EMPTY, gameBoard.getTileType(2,2));
    }

    @Test
    void testRemoveElementONInvalidPosition() {
        Board gameBoard = new Board(5);
        assertFalse(gameBoard.removeElement(5, 5));
    }

    @Test
    void testMoveHeroConnectWithWall() {
        Board gameBoard = new Board(5);
        gameBoard.addElement(TileType.HERO,1, 1);
        gameBoard.getBoard().heroStep();
        assertEquals(TileType.HERO, gameBoard.getTile(1,1));
    }
    @Test
    void testMoveHeroConnectWithWumpus() {
        Game game = new Game();
        game.Start(true);
        game.createBoard(6);
        game.getBoard().addElement(TileType.WUMPUS,2,2);
        game.getBoard().addElement(TileType.HERO,3,2);
        game.getBoard().heroStep();
        assertTrue(game.getBoard().getHero().isDead());
    }

    @Test
    void testAddHero() {
        Board gameBoard = new Board(5);
        assertTrue(gameBoard.addElement(TileType.HERO,1, 1));
    }

    @Test
    void testPickUpGoldCommand_WithGold() {
        Board gameBoard = new Board(8);
        gameBoard.addElement(TileType.GOLD,2, 1);
        gameBoard.addElement(TileType.HERO, 4, 1);

        gameBoard.heroStep();
        gameBoard.heroPickUp();
        assertTrue(gameBoard.getHero().hasGold());
    }

    @Test
    void testRemoveWumpus() {
        Board gameBoard = new Board(5);
        gameBoard.addElement(TileType.WUMPUS, 2, 2);
        gameBoard.removeElement(2, 2);
        assertEquals(TileType.EMPTY, gameBoard.getTile(2, 2));
    }

    @Test
    void testRemovePit() {
        Board gameBoard = new Board(5);
        gameBoard.addElement(TileType.PIT, 2, 2);
        gameBoard.removeElement(2, 2);
        assertEquals(TileType.EMPTY, gameBoard.getTile(2, 2));
    }

    @Test
    void testRemoveGold() {
        Board gameBoard = new Board(5);
        gameBoard.addElement(TileType.GOLD, 2, 2);
        gameBoard.removeElement(2, 2);
        assertEquals(TileType.EMPTY, gameBoard.getTile(2, 2));
    }

    @Test
    void testHeroEncounterWithPit() {
        Board gameBoard = new Board(5);
        gameBoard.addElement(TileType.PIT, 2, 2);
        gameBoard.addElement(TileType.HERO, 3, 2);
        gameBoard.getHero().setArrows(3);
        gameBoard.heroStep();
        assertEquals(2, gameBoard.getHero().getArrows());
    }

    @Test
    void testCalculateWumpusCount() {
        Board gameBoard = new Board(7);
        assertEquals(1, gameBoard.getMaxWumpus());

        gameBoard = new Board(10);
        assertEquals(2, gameBoard.getMaxWumpus());

        gameBoard = new Board(15);
        assertEquals(3, gameBoard.getMaxWumpus());
    }

    @Test
    void testShoot() {
        Board gameBoard = new Board(15);
        gameBoard.addElement(TileType.WUMPUS, 2,7);
        gameBoard.addElement(TileType.WUMPUS, 13,7);
        gameBoard.addElement(TileType.WUMPUS, 7,2);
        gameBoard.addElement(TileType.WUMPUS, 7,13);
        gameBoard.addElement(TileType.HERO, 7,7);

        gameBoard.getHero().setArrows(4);

        gameBoard.getHero().setDirection(Direction.NORTH);
        gameBoard.Shoot();

        gameBoard.getHero().setDirection(Direction.EAST);
        gameBoard.Shoot();

        gameBoard.getHero().setDirection(Direction.SOUTH);
        gameBoard.Shoot();

        gameBoard.getHero().setDirection(Direction.WEST);
        gameBoard.Shoot();

        assertEquals(0,gameBoard.getNumberOfWumpus());
        assertEquals(0,gameBoard.getHero().getArrows());
        assertFalse(gameBoard.Shoot());

    }
}




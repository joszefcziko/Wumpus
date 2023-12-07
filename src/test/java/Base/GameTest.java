package Base;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
/*
    @Test
    void checkDbConnection() {

        Game game = new Game();
        game.Start(true);
        assertTrue(game.dbConnection());
    }
*/
    @Test
    void isWin() {
        Game game = new Game();
        game.Start(true);
        game.createBoard(6);
        game.getBoard().addElement(TileType.GOLD,2,2);
        game.getBoard().addElement(TileType.HERO,3,2);
        game.getBoard().heroStep();
        assertTrue(game.getBoard().getHero().hasGold());
    }

    @Test
    void isHerodead() {
        Game game = new Game();
        game.Start(true);
        game.createBoard(6);
        game.getBoard().addElement(TileType.WUMPUS,2,2);
        game.getBoard().addElement(TileType.HERO,3,2);
        game.getBoard().heroStep();
        assertTrue(game.getBoard().getHero().isDead());
    }

    @Test
    void testStateLoader() {
        Game game = new Game();
        game.Start(true);
        assertTrue(game.StateLoader(false, "wumpluszinput.xml"));
        assertTrue(game.StateLoader(false, "wumpluszinput.txt"));
    }

    @Test
    void testSaveState() {
        Game game = new Game();
        game.Start(true);
        game.createBoard(15);
        game.getBoard().addHero(5,5,5,Direction.NORTH);
        game.getBoard().addGold();
        game.getBoard().addWumpus();
        assertTrue(game.getBoard().saveToFile("testSaveMap.xml", false));
        assertTrue(game.getBoard().saveToFile("testSaveState.xml", true));
        assertTrue(game.getBoard().saveToFile("testSaveMap.txt", false));
        assertTrue(game.getBoard().saveToFile("testSaveState.txt", true));
        assertTrue(game.saveProfile());
    }

    @Test
    void testRandomGenerator() {
        Game game = new Game();
        game.Start(true);
        game.randomMap();
    }


    @Test
    void testDrawRandomMap() {
        Game game = new Game();
        game.Start(true);
        game.randomMap();
        game.getBoard().DrawBoard();
    }
}
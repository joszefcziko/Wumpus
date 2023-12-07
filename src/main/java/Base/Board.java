package Base;


import Sfv.Sfv;
import State.WriteToFile;
import State.XmlMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class Board {
    private Tile[][] tiles;

    private int size, numberOfWumpus, numberOfGold;
    private Hero hero;
    private int MAXGOLD = 1;
    private int MAXWUMPUS = 0;

    private File soundFile;
    private AudioInputStream audioIn;
    private Clip clip;

    public Board(int size) {

        try {
            soundFile = new File("src\\Sounds\\uhh.wav");
            audioIn = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioIn);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        this.size = (size <= 0 ? 6 : size > 20 ? 20 : size);
        this.numberOfWumpus = 0;
        this.numberOfGold = 0;

        this.tiles = new Tile[size][size];
        this.MAXWUMPUS = ( size <= 8 ? 1 : ( size <= 14 ? 2 : 3 ) );
        Init();
    }

    public Board() {
        this(6);
    }

    public void Init() {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i][j] = new Tile();

                if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
                    tiles[i][j].setType(TileType.WALL);
                }
            }
        }
    }

    public boolean getMap(wMap wmap) {
        if (wmap.isOkay()) {
            String data = wmap.getData();
            int size = wmap.getSize();
            int herox = wmap.getPosx();
            int heroy = wmap.getPosy();
            Direction dir = wmap.getDirrection();
            int arrows = wmap.getArrows();

            this.hero = null; //new Hero(herox, heroy, arrows);
            this.tiles = new Tile[size][size];
            Init();

            for(int i = 0; i < size; i++) {
                for(int j = 0; j < size; j++) {

                    Tile tile = new Tile();
                    char c = data.charAt(i * size + j);

                    switch(c) {

                        case '_': tile.setType(TileType.EMPTY); break;
                        case 'W': tile.setType(TileType.WALL); break;
                        case 'U': tile.setType(TileType.WUMPUS); break;
                        case 'P': tile.setType(TileType.PIT); break;
                        case 'G': tile.setType(TileType.GOLD); break;
                        case 'H': tile.setType(TileType.HERO); break;
                    }
                    //this.tiles[i][j] = tile;
                    addElement(tile.getType(), i, j);
                }
            }
            addHero(heroy, herox, arrows, dir);
            return true;
        } else {
            System.out.println(wmap.getErrorMessage());
        }
        return false;
    }

    public void DrawBoard() {

        System.out.print("   ");
        for(int i = 0; i < size; i++){
            System.out.print(String.format("%c", 'A' + i));
        }

        System.out.println();

        for (int i = 0; i < size; i++) {
            System.out.print(String.format("%2d ", i));
            for (int j = 0; j < size; j++) {

                String s = tiles[i][j].toString();

                if(s.equals("W")) {
                    if (i == 0) {
                        System.out.print("▄");
                    } else if (i == size - 1) {
                        System.out.print("▀");
                    } else if (j == 0 || j == size - 1) {
                        System.out.print("█");
                    } else {
                        System.out.print("█");
                    }
                } else {
                    System.out.print(tiles[i][j].toString());
                }
            }

            if(this.hero != null) {
                int ug = getHeroGold();
                int bg = getGoldOfBoard();
                int ua = getHeroArrows();
                int bw = getWumpusOfBoard();
                int hx = getHeroX();
                int hy = getHeroY();

                String dn = getHeroDirectionName();

                switch(i) {
                    case 0: System.out.print(String.format("     Arany:  %d / %d", ug, bg)); break;
                    case 1: System.out.print(String.format("     Nyilak: %d", ua)); break;
                    case 2: System.out.print(String.format("     Wumpus: %d", bw)); break;
                    case 3: System.out.print(String.format("     Irány:  %s, x: %2d, y: %2d", dn, hx, hy)); break;
                }
            }

            System.out.println();
        }
    }

    public void heroStep( ) {
        int row0 = hero.getRow();
        int col0 = hero.getCol();
        Direction dir = hero.getDirection();
        int row = 0, col = 0;

        switch(dir) {
            case NORTH: row = row0 - 1; col = col0; break;
            case EAST: row = row0; col = col0 + 1; break;
            case SOUTH: row = row0 + 1; col = col0; break;
            case WEST: row = row0; col = col0 - 1; break;
        }

        Tile tileTo = new Tile(tiles[row][col].getType());

        //if (onBoard(row, col)) {
        if(canMoveHero(row, col)){
            switch(tileTo.getType()) {
                case EMPTY: tiles[row0][col0].setType(TileType.EMPTY);
                            tiles[row][col].setType(TileType.HERO);
                            hero.setXY(col, row); break;
                case WUMPUS: hero.setDead(); break;
                case PIT: hero.loseArrow(); break;
                case GOLD: tiles[row0][col0].setType(TileType.EMPTY);
                           tiles[row][col].setType(TileType.HERO);
                           hero.setXY(col,row);
                           hero.pickupGold(); break;
                //case WALL: break;
            }
            //tiles[row0][col0].setType(TileType.EMPTY);
        }
    }

    public void heroTurnRight() {
        hero.turnRight();
    }

    public void heroTurnLeft() {
        hero.turnLeft();
    }

    public int getSize() { return this.size; }

    public int getNumberOfGold() { return this.numberOfGold; }

    public int getNumberOfWumpus() { return this.numberOfWumpus; }

    public int getMaxWumpus() { return this.MAXWUMPUS; }

    ///////

    public boolean onBoard(int row, int col) {
        if (row >= 0 && row < size && col >= 0 && col < size) return true;
        return false;
    }

    public boolean inField(int row, int col) {
        if (row > 0 && row < size - 1 && col > 0 && col < size - 1) return true;
        return false;
    }

    public boolean canAddWumpus() {

        // WUMPUSZ-ból N <= 8 esetén 1, 9 <= N <= 14 esetén 2, N > 14 esetén 3.
        // ez automatikusan állítódjon be N alapján

        //int numberOfWumpus = ( size <= 8 ? 1 : ( size <= 14 ? 2 : 3 ) );
        if(this.numberOfWumpus < MAXWUMPUS) return true;
        return false;
    }

    public boolean canAddGold() {
        if (this.numberOfGold < MAXGOLD) return true;
        return false;
    }

    public boolean canAddHero() {
        if(hero == null) return true;
        return false;
    }

    public boolean canMoveHero(int row, int col) {
        if(onBoard(row,col)) {
            //System.out.println("------- MOVE TO [" + row + "," + col + "]: " + tiles[row][col].getType());
            if(tiles[row][col].getType() != TileType.WALL) {
                return true;
            }
        }
        return false;
    }

    ///////

    public boolean addWumpus() {

        if(canAddWumpus()) {

            Random random = new Random();

            for (int i = this.numberOfWumpus; i < MAXWUMPUS; i++) {
                int wumpusRow = 0, wumpusColumn = 0;
                while (tiles[wumpusRow][wumpusColumn].getType() != TileType.EMPTY) {
                    wumpusRow = random.nextInt(size - 2) + 1;
                    wumpusColumn = random.nextInt(size - 2) + 1;
                }
                tiles[wumpusRow][wumpusColumn].setType(TileType.WUMPUS);
            }
            this.numberOfWumpus = MAXWUMPUS;
            if(hero != null) hero.setArrows(this.numberOfWumpus);
            return true;
        }
        return false;
    }

    public boolean addGold(){

        if (canAddGold()) {

            Random random = new Random();

            int goldRow = 0, goldColumn = 0;

            while (tiles[goldRow][goldColumn].getType() != TileType.EMPTY) {
                goldRow = random.nextInt(size - 2) + 1;
                goldColumn = random.nextInt(size - 2) + 1;
            }
            tiles[goldRow][goldColumn].setType(TileType.GOLD);
            this.numberOfGold = 1;
            return true;
        }
        return false;
    }

    public boolean addHero() {

        if (canAddHero()) {

            Random random = new Random();

            int row = 0, col = 0;

            while (tiles[row][col].getType() != TileType.EMPTY) {
                row = random.nextInt(size - 2) + 1;
                col = random.nextInt(size - 2) + 1;
            }
            addHero(row, col, 3, Direction.NORTH);
            return true;
        }
        return false;
    }

    public boolean addHero(int row, int col, int arrow, Direction dir) {
        if(this.hero == null) {
            if(inField(row, col)) {
                this.hero = new Hero(row, col, arrow);
                this.hero.setDirection(dir);
                this.tiles[row][col].setType(TileType.HERO);
                return true;
            }
        }
        return false;
    }

    public boolean addElement(TileType type, int row, int col) {

        boolean canputgold = (type == TileType.GOLD && canAddGold());
        boolean canputwumpus = (type == TileType.WUMPUS && canAddWumpus());
        boolean canputelement = ((type == TileType.WALL || type == TileType.PIT));
        boolean canputhero = (type == TileType.HERO && canAddHero());
        boolean correctzone = (inField(row, col) && (tiles[row][col].getType() == TileType.EMPTY));

        if(canputgold || canputwumpus || canputelement || canputhero) {
            if (correctzone) {
                tiles[row][col].setType(type);
                if(canputgold) numberOfGold++;
                if(canputwumpus) numberOfWumpus++;
                if(canputhero) addHero(row,col,0,Direction.NORTH);
                return true;
            }
        }
        return false;
    }

    public boolean removeElement(int row, int col) {
        if (inField(row, col)) {
            tiles[row][col].setType(TileType.EMPTY);
            return true;
        }
        return false;
    }

    public void heroPickUp() {
        int row = hero.getRow();
        int col = hero.getCol();
        int row0 = 0, col0 = 0;

        //System.out.println("------- HERO: "  + row + "," + col);

        switch(hero.getDirection()) {
            case NORTH: row0 = row - 1; col0 = col; break;
            case EAST: row0 = row; col0 = col + 1; break;
            case SOUTH: row0 = row + 1; col0 = col; break;
            case WEST: row0 = row; col0 = col - 1; break;
        }

        switch(tiles[row0][col0].getType()) {
            case GOLD: tiles[row0][col0].setType(TileType.EMPTY);
                 hero.pickupGold(); break;
            case WUMPUS: hero.setDead(); break;
        }
    }

    ///////

    private int getWumpusOfBoard() { return this.numberOfWumpus; }

    private int getGoldOfBoard() {
        return this.numberOfGold;
    }

    private int getHeroGold() {
        if(this.hero != null) return this.hero.getGold();
        return 0;
    }

    private int getHeroArrows() {
        if(this.hero != null) return this.hero.getArrows();
        return -1;
    }

    private int getHeroX() {
        if(this.hero != null) return this.hero.getCol();
        return 0;
    }

    private int getHeroY() {
        if(this.hero != null) return this.hero.getRow();
        return 0;
    }

    public String getHeroDirectionName() {
        if (hero != null) return hero.getDirection().getDirectionName();
        return "NONE";
    }

    public Board getBoard() {
        return this;
    }

    public Tile[][] getTiles() {
        return this.tiles;
    }

    public TileType getTileType(int row, int col) {
        if(onBoard(row, col)) {
            return tiles[row][col].getType();
        }
        return TileType.UNKNOWN;
    }

    public TileType getTile(int row, int col) {
        return this.tiles[row][col].getType();
    }

    public Hero getHero() {
        return this.hero;
    }


    public boolean Shoot() {

        if (hero.loseArrow() ) {

            boolean HORISONTAL = false;
            boolean UP = false;
            int col = 0, row = 0;
            int from = 0, to = 0;

            switch (hero.getDirection()) {
                case NORTH:
                    from = hero.getRow();
                    to = 0;
                    break;
                case EAST:
                    from = hero.getCol();
                    to = size - 1;
                    HORISONTAL = true;
                    UP = true;
                    break;
                case SOUTH:
                    from = hero.getRow();
                    to = size - 1;
                    UP = true;
                    break;
                case WEST:
                    from = hero.getCol();
                    to = 0;
                    HORISONTAL = true;
                    break;
            }

            Tile tile = new Tile();
            boolean stop = false;

            if (UP) {
                for (int i = from; i < to && !stop; i++) {
                    if (HORISONTAL) {
                        row = hero.getRow();
                        col = i;
                        tile.setType(tiles[row][col].getType());

                    } else {
                        row = i;
                        col = hero.getCol();
                        tile.setType(tiles[row][col].getType());
                    }

                    if (tile.getType() == TileType.WALL || tile.getType() == TileType.WUMPUS) stop = true;
                }
            } else {
                for (int i = from; i > to && !stop; i--) {
                    if (HORISONTAL) {
                        row = hero.getRow();
                        col = i;
                        tile.setType(tiles[row][col].getType());
                    } else {
                        row = i;
                        col = hero.getCol();
                        tile.setType(tiles[row][col].getType());
                    }

                    if (tile.getType() == TileType.WALL || tile.getType() == TileType.WUMPUS) {
                        stop = true;
                    }
                }
            }

            if (tile.getType() == TileType.WALL) hero.loseArrow();
            if (tile.getType() == TileType.WUMPUS) {
                numberOfWumpus--;
                clip.setFramePosition(0);
                clip.start();
                tiles[row][col].setType(TileType.EMPTY);
            }
            return true;
        }
        return false;
    }

    public boolean saveToFile(String filename, boolean state) {

        String extension = Sfv.getFileExtension(filename);

        ArrayList<String> rows = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        String DIR = (state ? "src\\States\\" : "src\\Maps\\");

        int hx = 0;
        int hy = 0;
        int arrows = 0;
        char cx = 0;
        String direction = "N";

        if(hero != null) {

            hx = hero.getCol();
            hy = hero.getRow();
            arrows = hero.getArrows();
            cx = (char) ('A' + hx);
            direction = String.valueOf(hero.getDirection().toString().charAt(0));

            tiles[hy][hx].setType(TileType.EMPTY);
        }


        if(extension.equals("txt")) {
            sb.append(size + " " + hy + " " + cx + " " + direction + "\r\n");
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sb.append(tiles[i][j].toString());
                }
                sb.append("\r\n");
            }

            WriteToFile writer = new WriteToFile();
            boolean ok = writer.saveToTextFile(DIR + filename, sb.toString());
            sb.delete(0, sb.length());
            return ok;
        }

        if(extension.equals("xml")) {

            for(int i = 0; i < size; i++) {
                for(int j = 0; j < size; j++) {
                    sb.append(tiles[i][j].toString());
                }
                rows.add(sb.toString());
                sb.delete(0, sb.length());
            }

            XmlMap map = new XmlMap();

            map.setSize(size);
            map.setArrows(arrows);
            map.setRows(rows);
            map.setHeroCol(String.valueOf(cx));
            map.setHeroRow(hy);
            map.setHeroDir(direction);

            WriteToFile writer = new WriteToFile();
            return writer.saveGameState(map, DIR + filename);

        }
        return false;
    }
}


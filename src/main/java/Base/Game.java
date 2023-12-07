package Base;

import Sfv.Sfv;
import State.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Game {
    private Board board;
    private Scanner scanner;
    private String username;
    private int userscore, usersteps;
    private Database db;

    private boolean win;
    private boolean giveup;
    private boolean gameover;
    private boolean herodead;

    public Game() {

        this.db = new Database();
        this.scanner = new Scanner(System.in);
    }

    public void Start() {
        Start(false);
    }

    public void Start(boolean testMode) {

        if(db.isConnected()) {
            ArrayList<User> users = db.getUsers();

            if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    System.out.println(users.get(i));
                }
            }
        }

        if(!testMode) {
            setName();
            drawBoard();
            MainMenu();
        } else {
            username = "Johnnytest";
        }
    }

    private int getFileType() {
        boolean ok = false;
        int tipus = -1;

        while(!ok) {
            System.out.print("Mentés típusa: 0. TXT, 1. XML: ");
            String s = scanner.nextLine();
            if(Sfv.isNumeric(s)) tipus = Sfv.getInteger(s);
            if(tipus == 0 || tipus == 1) ok = true;
        }
        return tipus;
    }


    public boolean createBoard(int manualSize) {
        int size = -1;
        if(manualSize == -1) {
            boolean finished = false;

            while (!finished) {
                System.out.println("Pálya méret (6-20): ");
                String s = scanner.next();
                size = Sfv.getInteger(s);
                if (size > 5 && size < 21) finished = true;
            }
        } else {
            size = manualSize;
        }

        if(size > 5 && size <21) {
            this.board = null;
            this.board = new Board(size);
            board.Init();
            System.gc();
            return true;
        }
        return false;
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean dbConnection() {
        return db.isConnected();
    }

    public boolean isWin() {
        return win;
    }

    public boolean isGiveup() {
        return giveup;
    }

    public boolean isGameover() {
        return gameover;
    }

    public boolean isHerodead() {
        return herodead;
    }

    ///////

    public boolean StateLoader(boolean state) {
        return StateLoader(state, "");
    }

    public  boolean StateLoader(boolean state, String manualFilename) {
        ReadFile reader = new ReadFile();

        String DIR = (state ? "src\\States\\" : "src\\Maps\\");
        String filename = null;
        int index = -1;

        if(manualFilename.length() == 0) {
            ArrayList<String> list = Sfv.listFiles(DIR);

            System.out.println("--== FÁJL LISTA ==--");
            for (int i = 0; i < list.size(); System.out.println(String.format("%3d. ", i) + list.get(i++))) ;

            while (index < 0 || index > list.size()) {
                System.out.print("Adja meg a mentés sorszámát: ");
                String s = scanner.next();
                index = Sfv.getInteger(s);
            }
            filename = list.get(index);
        } else {
            filename = manualFilename;
        }
        String extension = Sfv.getFileExtension(filename);

        int size = 0;
        int posx = 0;
        int posy = 0;
        int dir = 0;
        int arrows = 0;

        String adat = "";
        String fajladat = "";

        if(extension.equals("")) {

            ArrayList<wMap> levels = db.getMaps(filename, username);

            if (levels != null) {
                for (int i = 0; i < levels.size(); i++) {
                    System.out.println(levels.get(i));
                }
            }
        }

        if (extension.equals("txt")) {
            fajladat = reader.ReadInTextFile(DIR + filename).toUpperCase();
            String head = fajladat.substring(0, fajladat.indexOf('\r')).trim();
            adat = fajladat.substring(fajladat.indexOf('\n')).trim();

            StringTokenizer st = new StringTokenizer(head, " ");

            index = 0;

            while (st.hasMoreTokens()) {
                String s = st.nextToken();

                switch (index) {
                    case 0:
                        size = Sfv.getInteger(s);
                        break;
                    case 1:
                        if (s.length() == 1) {
                            posx = s.charAt(0) - 'A';
                        } else {
                            posx = -1;
                        }
                        break;
                    case 2:
                        posy = Sfv.getInteger(s);
                        break;
                    case 3:
                        if (s.length() == 1) {
                            dir = s.charAt(0);
                        } else {
                            dir = 0;
                        }
                        break;
                }
                index++;
            }
        }

        if (extension.equals("xml")) {
            XmlMap map = reader.ReadInXmlFile(DIR + filename);
            size = map.getSize();
            posx = map.getHeroCol().charAt(0) - 'A';
            posy = map.getHeroRow();
            dir = map.getHeroDir().toUpperCase().charAt(0);
            arrows = map.getArrows();
            adat = map.getData();
        }

        adat = Sfv.StringReplaceAll(adat, "\r", "");
        adat = Sfv.StringReplaceAll(adat, "\n", "");
        adat = Sfv.StringReplaceAll(adat, "\t", "");
        adat = Sfv.StringReplaceAll(adat, " ", "");

        int check = adat.length();
        int check_a = (size * size) - check;

        wMap map = new wMap(size, posx, posy, dir, arrows, 0,0,adat);

        if ((size > 5 && size < 21)) {
            if ((posx > 0 && posx < size)) {
                if ((posy > 0 && posy < size)) {
                    if ((dir == 'N' || dir == 'E' || dir == 'S' || dir == 'W')) {
                        if (check_a == 0) {

                            boolean ok = true;
                            char[] legal_chars = {'_', 'W', 'U', 'P', 'G', 'H'};

                            for (int i = 0; i < adat.length() && ok; i++) {
                                ok = false;
                                for (int j = 0; j < legal_chars.length; j++) {
                                    if (adat.charAt(i) == legal_chars[j]) ok = true;
                                }
                            }
                            if (!ok) {
                                map.add_error_mess("Wrong symbol in data: " + adat);
                            }
                        } else {
                            map.add_error_mess("Bad data size : " + check_a);
                        }
                    } else {
                        map.add_error_mess(String.format("Wrong dirrection '%c'", dir));
                    }
                } else {
                    map.add_error_mess("Wrong Y position: " + posy);
                }
            } else {
                map.add_error_mess("Wrong X position: " + posx);
            }
        } else {
            map.add_error_mess("Wrong board size: " + size);
        }

        this.board = null;
        this.board = new Board(size);
        return this.board.getMap(map);
    }

    private boolean LoadState() {
        return StateLoader(true);
    }

    private boolean LoadMap() {
        return StateLoader(false);
    }

    private void saveState(String filename) {

        int tipus = getFileType();
        String kit = (tipus == 0 ? ".txt" : ".xml");
        board.saveToFile(filename + kit, true);
    }

    private void saveMap() {

        int tipus = getFileType();

        boolean ok = false;

        while(!ok) {
            System.out.print("\nKérem a pálya mentési nevét:");
            String s = scanner.nextLine();
            if(Sfv.isFileName(s)) {
                String kit = (tipus == 0 ? ".txt" : ".xml");
                board.saveToFile(s + kit, false);
                ok = true;
            }
        }
    }

    private void setName() {
        boolean ok = false;
        String name = null;

        ArrayList<String> list = Sfv.listFiles("src\\Profiles\\");

        while(!ok){
            System.out.print("User name:");
            name = scanner.nextLine();

            if(Sfv.isAlpha(name) && name.length() >= 3) {
                if(!list.contains(name)) {
                    XmlMap profile = new XmlMap(0,0,0);
                    WriteToFile writer = new WriteToFile();
                    writer.saveGameState(profile, "src\\Profiles\\" + username + ".xml");
                }

                ok = true;
            }
        }
        username = name;
        userscore = 0;
    }

    ///////

    private void drawBoard() {
        int Col1 = 2;
        int Row1 = 2;

        if(board != null) {
            int ch = 16 - (board.getSize() / 2) + Col1;
            int cw = 12 - (board.getSize() / 2) + Row1;

            board.DrawBoard();

        }
    }

    private Coord getCoord() {

        boolean correctCoords = false;
        int col = 0, row = 0;

        while (!correctCoords) {

            System.out.print("Sor: ");
            String srow = scanner.next();

            System.out.print("Oszlop: ");
            String scol = scanner.next();

            row = Sfv.getInteger(srow);

            if(scol.length() == 1) {    // ha egyetlen számjegy vagy betű
                int temp = scol.charAt(0);

                if(temp > 96) {         // ha nagybetű
                    col = temp - 97;
                }else if(temp > 64) {   // ha kisbetű
                    col = temp - 65;
                } else {
                    col = temp - 48;
                }
            } else {
                col = Sfv.getInteger(scol);
            }

            if (col > -1 && row > -1) {
                correctCoords = true;
            }
        }
        return new Coord(col, row);
    }

    private boolean addHero() {

        Coord coord = getCoord();
        int col = coord.getX();
        int row = coord.getY();

        return this.board.addHero(row,col,0, Direction.NORTH);

    }

    private boolean addHeroR() {
        if(board != null) {
            return board.addHero();
        }
        return false;
    }

    private boolean addWumpus() {
        if(board != null) {
            return board.addWumpus();
        }
        return false;
    }

    private boolean addGold() {
        if(board != null) {
            return board.addGold();
        }
        return false;
    }

    private void addElement() {
        if(board != null) {

            boolean correctElement;
            boolean correctType;

            boolean ok = false;
            boolean finished = false;

            int choice = -1;
            int col = 0, row = 0;

            while (!finished) {

                correctElement = false;
                correctType = false;

                Coord coord = getCoord();
                col = coord.getX();
                row = coord.getY();

                while (!correctElement) {
                    System.out.println( "1. Fal    ");
                    System.out.println( "2. Verem  ");
                    System.out.println( "3. Arany  ");
                    System.out.println( "4. Wumpusz");
                    System.out.println( "   Válassz:");

                    String s = scanner.next();

                    choice = Sfv.getInteger(s);

                    if (choice > 0 && choice < 5) correctElement = true;
                }

                TileType type = TileType.EMPTY;

                while (!correctType) {

                    correctType = true;
                    switch (choice) {
                        case 1:
                            type = TileType.WALL;
                            break;
                        case 2:
                            type = TileType.PIT;
                            break;
                        case 3:
                            type = TileType.GOLD;
                            break;
                        case 4:
                            type = TileType.WUMPUS;
                            break;
                        default:
                            correctType = false;
                            break;
                    }
                }

                if (board.addElement(type, row, col)) finished = true;
            }
        }
    }

    private void removeElement() {
        if(board != null) {

            boolean finished = false;
            int col = 0, row = 0;

            while (!finished) {

                Coord coord = getCoord();
                col = coord.getX();
                row = coord.getY();

                board.removeElement(row, col);
                finished = true;
            }
        }
    }

    public boolean saveProfile() {
        XmlMap profile = new XmlMap();
        ReadFile reader = new ReadFile();
        WriteToFile writer = new WriteToFile();

        profile = reader.ReadInXmlFile("src\\Profiles\\" + username + ".xml");

        int hiscr = profile.getHiscore();

        hiscr++;
        int step = usersteps;
        int arrow = board.getHero().getArrows();

        profile.setArrows(arrow);
        profile.setHiscore(hiscr);
        profile.setSteps(step);

        return writer.saveGameState(profile, "src\\Profiles\\" + username + ".xml");
    }

    public boolean randomMap() {
        Random random = new Random();
        int size = -1;

        while(size < 6 || size > 20) {
            size = random.nextInt(20);
        }

        createBoard(size);
        if(addHeroR() && addGold() && addWumpus()) return true;
        return false;
    }

    ///////

    private void MainMenu() {

        boolean finished = false;
        int choice = 0;
        String s;

        while(!finished) {
            drawBoard();

            System.out.println("1. Pálya szerkesztés");
            System.out.println("2. Pálya betöltése  ");
            System.out.println("3. Pálya mentése    ");
            System.out.println("4. Véletlen pálya   ");
            System.out.println("0. Kilépés          ");
            System.out.println("   Válassz:         ");

            s = scanner.next();

            choice = Sfv.getInteger(s);

            switch(choice) {
                case 1: EditBoardMenu(); break;
                case 2: LoadMap(); break;
                case 3: saveMap(); break;
                case 4: randomMap(); break;
                case 0: finished = true; break;
            }
        }
    }

    private void EditBoardMenu() {

        boolean finished = false;
        int choice;
        String s;

        while (!finished) {
            drawBoard();

            System.out.println("1. Pálya létrehozása    ");
            System.out.println("2. Elem hozzáadása      ");
            System.out.println("3. Elem eltávolítása    ");
            System.out.println("4. Hős elhelyezése      ");
            System.out.println("5. Wumpuszok elhelyezése");
            System.out.println("6. Arany elhelyezése    ");
            System.out.println("7. Játék intdítása      ");
            //System.out.println("8. Játék Betöltése      ");
            //System.out.println("9. Játék mentése        ");
            System.out.println("0. Főmenü               ");
            System.out.println("   Válassz:             ");

            s = scanner.next();

            choice = Sfv.getInteger(s);

            switch (choice) {
                case 1: createBoard(-1); break;
                case 2: addElement(); break;
                case 3: removeElement(); break;
                case 4: addHero(); break;
                case 5: addWumpus(); break;
                case 6: addGold(); break;
                case 7: InGameMenu(); break;
                //case 9: saveState(username + "_state.xml"); break;
                case 0: finished = true;
            }
        }
    }

    private void InGameMenu() {
        win = false;
        giveup = false;
        gameover = false;
        herodead = false;

        usersteps = 0;
        int wumpus = board.getNumberOfWumpus();
        board.getHero().setArrows(wumpus);

        while (!gameover && !win && !giveup && !herodead) {
            drawBoard();

            System.out.println("1. Lépés         ");
            System.out.println("2. Jobbra fordul ");
            System.out.println("3. Balra fordul  ");
            System.out.println("4. Felszed       ");
            System.out.println("5. Lő            ");
            System.out.println("6. Mentés        ");
            System.out.println("7. Betölt        ");
            System.out.println("0. Felad / Kilép ");
            System.out.println("   Válassz:      ");

            String s = scanner.next();
            int choice = Sfv.getInteger(s);

            usersteps++;

            switch(choice) {
                case 1: board.heroStep(); break;
                case 2: board.heroTurnRight(); break;
                case 3: board.heroTurnLeft(); break;
                case 4: board.heroPickUp(); break;
                case 5: board.Shoot(); break;
                case 6: saveState(username + "_state"); break;
                case 7: LoadState(); break;
                case 0: gameover = true; break;
            }
            herodead = board.getHero().isDead();
            win = board.getHero().hasGold();

        }

        XmlMap profile = new XmlMap();
        ReadFile reader = new ReadFile();
        WriteToFile writer = new WriteToFile();

        profile = reader.ReadInXmlFile("src\\Profiles\\" + username + ".xml");

        int hiscr = profile.getHiscore();

        hiscr++;
        int step = usersteps;
        int arrow = board.getHero().getArrows();

        profile.setArrows(arrow);
        profile.setHiscore(hiscr);
        profile.setSteps(step);

        writer.saveGameState(profile, "src\\Profiles\\" + username + ".xml");
        if(herodead) System.out.println("\n\n------- GAME OVER: A HŐS HALOTT!");
        if(giveup) System.out.println("\n\n------- GAME OVER: FELADTAD!");
        if(gameover) System.out.println("\n\n------- GAME OVER: KILÉPTÉL!");
        if(win) System.out.println("\n\n------- GAME OVER: NYERTÉL!");
    }

    ///////


}

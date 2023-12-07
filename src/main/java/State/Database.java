package State;

import Base.wMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/wumpus";
    private final String USER = "root";
    private final String PASS = "";
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private boolean connected;

    public Database() {
        connected = false;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();
            connected = true;

        } catch ( SQLException se) { System.out.println("------- SQL HIBA: " + se.getMessage());
        } catch (Exception e) {  System.out.println("------- EXC HIBA: " + e.getMessage());
        }
    }

    public ArrayList<User> getUsers() {

        ArrayList<User> users = new ArrayList<User>();
        boolean ok = false;

        if(connected) {
            try {

                rs = stmt.executeQuery("SELECT name, passw, hiscore FROM users");

                while (rs.next()) {

                    String name = rs.getString("name");
                    String passw = rs.getString("passw");
                    int hiscore = rs.getInt("hiscore");

                    User usr = new User(name, passw, hiscore);
                    users.add(usr);
                }

                rs.close();
                ok = true;

            } catch (SQLException se) {
                System.out.println("------- SQL HIBA: " + se.getMessage());
            } catch (Exception e) {
                System.out.println("------- EXC HIBA: " + e.getMessage());
            }
        }
        if(!ok) users = null;   // Megsemmisíti a lista objektumot
        return users;
    }

    public ArrayList<wMap> getMaps(String map_name, String usr_name) {

        boolean ok = false;
        ArrayList<wMap> levels = new ArrayList<>();

        if(connected) {
            try {

                rs = stmt.executeQuery("call LoadMap('" + map_name + "', '" + usr_name + "');");

                while (rs.next()) {

                    /*
                        map.id, users.name, map.name, map.size, map.herocol, map.herorow, map.data
                    */
                    //String name = rs.getString("name");
                    int size = rs.getInt("map.size");
                    String usercol = rs.getString("map.herocol");
                    int userrow = rs.getInt("map.herorow");
                    String data = rs.getString("map.data");

                    wMap lv = new wMap(size, size, usercol.charAt(0) - 'A', userrow, 0, 0,0,data);
                    levels.add(lv);
                }

                rs.close();
                ok = true;

            } catch (SQLException se) {
                System.out.println("------- SQL HIBA: " + se.getMessage());
            } catch (Exception e) {
                System.out.println("------- EXC HIBA: " + e.getMessage());
            }
        }
        if(!ok) levels = null;
        return levels;
    }

    public boolean isConnected() {
        return this.connected;
    }
}

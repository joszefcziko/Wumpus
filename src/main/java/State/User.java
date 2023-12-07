package State;

public class User {
    private String name;
    private String passw;
    private int hiscore;

    public User(String name, String passw, int hiscr) {
        this.name = name;
        this.passw = passw;
        this.hiscore = hiscr;
    }

    public String getName() {
        return name;
    }

    public String getPassw() {
        return passw;
    }

    public int getHiscore() {
        return hiscore;
    }

    public String toString() {
        return "------- Name: '" + name + "' , Password: '" + passw + "' , Hiscore: " + hiscore;
    }
}

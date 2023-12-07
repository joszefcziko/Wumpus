import Base.Game;
import Sfv.Sfv;

public class Main {


    public static void main(String args[]) {

        try {

            Game game = new Game();
            game.Start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

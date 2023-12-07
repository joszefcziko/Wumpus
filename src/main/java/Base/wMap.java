package Base;

public class wMap {
    private int size;
    private int posx, posy, steps, hiscore;
    private Direction dirrection;
    private int arrows;
    private String data;
    private StringBuilder error_mess;

    boolean error;

    public wMap(int size, int posx, int posy, int dir, int arrows, int steps, int hi, String data) {
        this.size = size;
        this.posx = posx;
        this.posy = posy;
        this.data = data;
        this.arrows = arrows;
        this.steps = steps;
        this.hiscore = hi;
        this.error = false;
        this.error_mess = new StringBuilder();

        switch(dir) {
            case 'N': this.dirrection = Direction.NORTH; break;
            case 'E': this.dirrection = Direction.EAST; break;
            case 'S': this.dirrection = Direction.SOUTH; break;
            case 'W': this.dirrection = Direction.WEST; break;
            default: this.dirrection = Direction.UNKNOWN; break;
        }
    }

    public void add_error_mess(String err) {
        this.error = true;
        this.error_mess.append(err + "\n");
    }

    public String getErrorMessage() {
        return error_mess.toString();
    }

    public boolean isError() {
        return error;
    }

    public boolean isOkay() { return !error; }

    public int getSize() {
        return size;
    }

    public int getPosx() {
        return posx;
    }

    public int getPosy() {
        return posy;
    }

    public Direction getDirrection() {
        return dirrection;
    }

    public int getArrows() { return this.arrows; }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "wMap{" +
                "size=" + size +
                ", posx=" + posx +
                ", posy=" + posy +
                ", dirrection=" + dirrection +
                ", arrows=" + arrows +
                ", data='" + data + '\'' +
                ", error_mess=" + error_mess +
                ", error=" + error +
                '}';
    }
}

package Base;

import Sfv.Sfv;

public class Hero {
    private int row;
    private int column;
    private Direction direction;
    private int arrows;
    private boolean hasGold;
    private int steps;
    private boolean live;
    private Sfv sfv;

    public Hero(int row, int col, int arrows) {
        this.row = row;
        this.column = col;
        this.direction = Direction.NORTH;
        this.arrows = arrows;
        this.hasGold = false;
        this.steps = 0;
        this.live = true;
    }


    public void turnLeft() {
        int dir = sfv.StepBackInArray(this.direction.ordinal(), 4);
        this.direction = Direction.values()[dir];
    }

    public void turnRight() {
        int dir = sfv.StepFowardInArray(this.direction.ordinal(), 4);
        this.direction = Direction.values()[dir];
    }

    public boolean shoot() {
        if (this.arrows > 0) {
            this.arrows--;
            return true;
        }
        return false;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return column;
    }

    public void setCol(int col) {
        this.column = col;
    }

    public void setXY(int x, int y) {
        setCol(x);
        setRow(y);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public int getArrows() {
        return arrows;
    }

    public void setArrows(int arrows) { this.arrows = arrows; }

    public boolean loseArrow() {
        if(this.arrows > 0) {
            this.arrows--;
            return true;
        }
        return false;
    }

    public int getSteps() { return this.steps; }

    public void incSteps() { this.steps++; }

    public boolean isDead() { return !this.live; }

    public void setDead() { this.live = false; }

    public void pickupGold() {
        this.hasGold = true;
    }

    public int getGold() {
        if(this.hasGold) return 1;
        return 0;
    }

    public boolean hasGold() {
        return hasGold;
    }

}
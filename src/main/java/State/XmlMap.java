package State;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class XmlMap {
    private int size;
    private String heroCol;
    private int heroRow;
    private int arrows;
    private int hiscore;
    private int steps;
    private String heroDir;
    private List<String> rows;
    private boolean error;

    public XmlMap() {
         this.error = false;
    }

    public XmlMap(int arrows, int hiscore, int steps) {
        this.arrows = arrows;
        this.hiscore = hiscore;
        this.steps = steps;
    }

    @XmlElement(name = "size")
    public int getSize() {
        return size;
    }

    @XmlElement(name = "heroCol")
    public String getHeroCol() {
        return heroCol;//.toUpperCase();
    }

    @XmlElement(name = "heroRow")
    public int getHeroRow() { return heroRow; }

    @XmlElement(name = "heroDir")
    public String getHeroDir() {
        return heroDir;//.toUpperCase();
    }

    @XmlElement(name = "row")
    public List<String> getRows() {
        return rows;
    }

    @XmlElement(name = "arrows")
    public int getArrows() {
        return arrows;
    }

    @XmlElement(name = "hiscore")
    public int getHiscore() {
        return hiscore;
    }

    @XmlElement(name = "steps")
    public int getSteps() {
        return steps;
    }

    ////////////////

    public void setArrows(int arrows) {
        this.arrows = arrows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setHeroCol(String heroCol) {
        this.heroCol = heroCol;
    }

    public void setHeroRow(int heroRow) {
        this.heroRow = heroRow;
    }

    public void setHeroDir(String heroDir) {
        this.heroDir = heroDir;
    }

    public void setHiscore(int hiscore) {
        this.hiscore = hiscore;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getData() {
        StringBuilder data = new StringBuilder();
        for(int i = 0; i < rows.size(); data.append(rows.get(i++)));
        return data.toString().toUpperCase();
    }

    @Override
    public String toString() {
        return "XmlMap{" +
                ", size=" + size +
                ", heroCol='" + heroCol + '\'' +
                ", heroRow=" + heroRow +
                ", heroDir='" + heroDir + '\'' +
                ", rows=" + rows +
                ", error=" + error +
                '}';
    }
}
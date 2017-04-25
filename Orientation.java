package waterdrop;

/**
 *
 * @author humzah
 */
import java.util.ArrayList;

public class Orientation {

    public int[] ONE;
    public int[] TWO;
    public int[] THREE;
    public int[] FOUR;
    public int[] currentOrientation;

    public Orientation(int[] val) {
        //0 30 100 40
        //30 0 40 100
        this.ONE = (new int[]{val[0], val[1], val[2], val[3]});
        this.TWO = (new int[]{val[4], val[5], val[6], val[7]});
        this.THREE = (new int[]{val[8], val[9], val[10], val[11]});
        this.FOUR = (new int[]{val[12], val[13], val[14], val[15]});
        this.currentOrientation = ONE;
    }

    public int[] next() {
        int[] nextOrientation = null;
        if (currentOrientation == FOUR) {
            nextOrientation = ONE;
        } else if (currentOrientation == ONE) {
            nextOrientation = TWO;
        } else if (currentOrientation == TWO) {
            nextOrientation = THREE;
        } else if (currentOrientation == THREE) {
            nextOrientation = FOUR;
        }
        return nextOrientation;
    }

    public void printVals() {
        ArrayList<int[]> orient = new ArrayList<int[]>();

        orient.add(ONE);
        orient.add(TWO);
        orient.add(THREE);
        orient.add(FOUR);

        for (int[] vals : orient) {
            for (int val : vals) {
                System.out.print(val + " ,");
            }
            System.out.println("\n");
        }
    }

    public String toString() {
        String string = "";
        for (int val : this.currentOrientation) {
            string += val + " ,";
        }
        string += "\n";
        return string;
    }

    public void changePositions(int x, int y) {
        //takes a grid reference x and y
        //applies the change to all the Orientations of the orientation object
        x *= 100;
        y *= 100;
        this.changePosition(this.ONE, x, y);
        this.changePosition(this.TWO, x, y);
        this.changePosition(this.THREE, x, y);
        this.changePosition(this.FOUR, x, y);
    }

    public void changePosition(int[] orientation, int xDiff, int yDiff) {
        //updates position for a given orientation
        this.ONE[0] += xDiff;
        this.ONE[1] += yDiff;
    }
}

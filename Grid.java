package waterdrop;

/**
 *
 * @author humzah
 */
import java.util.List;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
//old cloning library imports requirements
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
//new cloning library (used to make deep copies of pipes)
//https://github.com/kostaskougios/cloning
import com.rits.cloning.*;

@SuppressWarnings("serial")
public class Grid implements Cloneable, Serializable {

    List<List<Pipe>> grid;
    int numColumns;
    int numRows;

    Pipe pipeA;
    Pipe pipeB;
    Pipe pipeC;
    Pipe pipeStart;
    Pipe pipeEnd;
    int randomStart;
    int randomEnd;

    Grid(int numColumns, int numRows) {
        this.numColumns = numColumns;
        this.numRows = numRows;
        //used to clone pipes
        Cloner cloner = new Cloner();
        //creates the blueprint of the playable pipe pieces (each with 4 orientations)
        this.setupDefaultPipes();

        List<List<Pipe>> grid2 = new ArrayList<List<Pipe>>(numColumns);
        //creates the spaces for the pipes
        for (int i = 0; i < 13; i++) { //< 14 (pos of right border)
            grid2.add(new ArrayList<Pipe>(numRows));
        }

        //fills in playable pipes
        for (int i = 1; i < 13; i++) {
            for (int j = 1; j < numRows; j++) {

                int randomNum = this.getRandomNumber(3);
                switch (randomNum) {
                    case 1: {
                        grid2.get(i).add(cloner.deepClone(this.pipeA));
                    }
                    break;
                    case 2: {
                        grid2.get(i).add(cloner.deepClone(this.pipeB));
                    }
                    break;

                    case 3: {
                        grid2.get(i).add(cloner.deepClone(this.pipeC));
                    }
                    break;

                    default: {
                        grid2.get(i).add(cloner.deepClone(this.pipeA));
                    }
                }
            }
        }

        //sets all the squares(pipes) in the top row to null (due to shortcomings of arrayList)
        for (int i = 1; i < 13; i++) {
            grid2.get(i).set(0, null);
        }

        this.randomStart = getRandomNumber(numColumns - 2);
        this.randomEnd = getRandomNumber(numColumns - 2);
        grid2.get(this.randomStart).set(0, this.pipeStart);
        //add another pipe to randomEndX

        grid2.get(this.randomEnd).add(this.pipeEnd);
        this.grid = grid2;
        //moves each generated pipe to the correct grid square (using its grid reference)
        this.updatePipePosition();
        this.updateStartEndPipePosition();
    }

    private int getRandomNumber(int max) {
        int randomNumber = (int) (Math.random() * max);
        return (randomNumber + 1);
    }

    public void setupDefaultPipes() {
        Cloner cloner = new Cloner();
        //set up the default pipes (each with their respective blocks)
        List<Block> pipeABlock = new ArrayList<Block>() {
            {
                this.add(new Block(0, 30, 100, 40, 30, 0, 40, 100, 0, 30, 100, 40, 30, 0, 40, 100));
            }
        };
        this.pipeA = new Pipe(pipeABlock, Color.web("#54F3B7"));

        List<Block> pipeBBlocks = new ArrayList<Block>() {
            {
                this.add(new Block(30, 0, 40, 70, 30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40));
                this.add(new Block(30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40, 30, 0, 40, 70));
            }
        };
        this.pipeB = new Pipe(pipeBBlocks, Color.web("#FFC0CB"));

        List<Block> pipeCBlocks = new ArrayList<Block>() {
            {
                this.add(new Block(30, 30, 40, 70, 0, 30, 70, 40, 30, 0, 40, 70, 30, 30, 70, 40));
                this.add(new Block(0, 30, 100, 40, 30, 0, 40, 100, 0, 30, 100, 40, 30, 0, 40, 100));
            }
        };
        this.pipeC = new Pipe(pipeCBlocks, Color.BISQUE);//#F3F354
        //water colour #54E7F2

        List<Block> pipeStartEndBlocks = new ArrayList<Block>() {
            {
                this.add(new Block(30, 0, 40, 100, 30, 0, 40, 100, 30, 0, 40, 100, 30, 0, 40, 100));
            }
        };
        this.pipeStart = new Pipe(cloner.deepClone(pipeStartEndBlocks), Color.RED);
        this.pipeEnd = new Pipe(cloner.deepClone(pipeStartEndBlocks), Color.BLUE);
    }

    private void updatePipePosition() {
        for (int i = 1; i < this.numColumns - 1; i++) {
            for (int j = 1; j < this.numRows - 1; j++) {
                Pipe pipe = this.grid.get(i).get(j);
                List<Block> blocks = pipe.blocks;
                for (Block block : blocks) {
                    //for each blocks orientation update the value of ONE TWO THREE FOUR
                    block.orientation.changePositions(i, j);
                    block.update();
                }
                pipe.updatesEdges();
            }
        }
    }

    private void updateStartEndPipePosition() {
        Pipe startPipe = this.grid.get(this.randomStart).get(0);
        List<Block> blocks = startPipe.blocks;
        for (Block block : blocks) {
            //for each blocks orientation update the value of ONE TWO THREE FOUR
            block.orientation.changePositions(this.randomStart, 0);
            block.update();
        }
        startPipe.updatesEdges();

        Pipe endPipe = this.grid.get(this.randomEnd).get(this.numRows - 1);
        List<Block> blocks2 = endPipe.blocks;
        for (Block block : blocks2) {
            //for each blocks orientation update the value of ONE TWO THREE FOUR
            block.orientation.changePositions(this.randomEnd, (this.numRows - 1));
            block.update();
        }
        endPipe.updatesEdges();
    }

    public void draw(GraphicsContext ctx) {
        //draw most of the pipes in the grid
        for (int i = 1; i < this.grid.size(); i++) {
            for (int j = 1; j < this.grid.get(1).size(); j++) {
                Pipe pipe = this.grid.get(i).get(j);
                pipe.draw(ctx);
            }
        }
        //draw start and end pipes
        Pipe startPipe = this.grid.get(this.randomStart).get(0);
        startPipe.draw(ctx);
        Pipe endPipe = this.grid.get(this.randomEnd).get(this.numRows - 1);
        endPipe.draw(ctx);
    }

    public void drawBorders(GraphicsContext ctx) {
        ctx.setFill(Color.BLACK);
        for (int i = 0; i < this.numColumns; i += (this.numColumns - 1)) {
            for (int j = 0; j < this.numRows; j++) {
                ctx.fillRect((i * 100), (j * 100), 100, 100);
            }
        }
    }

    public void drawBoxes(GraphicsContext ctx) {
        ctx.setFill(Color.BURLYWOOD);
        double width = ctx.getCanvas().getWidth();
        double height = ctx.getCanvas().getHeight();
        for (int i = 0; i < width; i += 100) {
            ctx.fillRect(i, 0, 1, height);
        }
        for (int j = 0; j < height; j += 100) {
            ctx.fillRect(0, j, width, 1);
        }
    }

    public Grid clone() {
        try {
            return (Grid) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public Grid deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Grid) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public String toString() {
        String string = "";

        for (int i = 0; i < this.grid.size(); i++) {
            string += "\n";
            string += this.grid.get(i).toString();
        }
        string += this.grid.get(1).toString();

        return string;
    }
}

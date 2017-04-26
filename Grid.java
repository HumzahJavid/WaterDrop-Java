package waterdrop;

/**
 *
 * @author humzah
 */
import java.util.List;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.apache.commons.lang3.SerializationUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

//external lib
//http://stackoverflow.com/questions/17402009/importing-library-in-java
//look into deep clone of pipeA
//http://stackoverflow.com/questions/869033/how-do-i-copy-an-object-in-java
//http://stackoverflow.com/questions/9264066/beanutils-clonebean-deep-copy
@SuppressWarnings("serial")
public class Grid implements Cloneable, Serializable {

    List<List<Pipe>> grid;
    int numColumns;
    int numRows;

    Pipe pipeA;
    Pipe pipeB;
    Pipe pipeC;

    Grid(int numColumns, int numRows) {
        for (int i = 0; i < 10; i++) {
            System.out.println(this.getRandomNumber(3));
            System.out.println("\n");
        }
        this.numColumns = numColumns;
        this.numRows = numRows;
        //creates the blueprint of the playable pipe pieces (each with 4 orientations)
        this.setupDefaultPipes();
        System.out.println("ABC" + this.pipeA + " " + this.pipeB + " " + this.pipeC);
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
                        grid2.get(i).add(pipeA.deepClone());
                    }
                    break;
                    case 2: {
                        grid2.get(i).add(pipeB.deepClone());
                    }
                    break;

                    case 3: {
                        grid2.get(i).add(pipeC.deepClone());
                    }
                    break;

                    default: {

                        grid2.get(i).add(pipeA.deepClone());
                    }
                }
            }
        }
        for (int i = 1; i < 13; i++) {
            for (int j = 0; j < (numRows - 1); j++) {
                System.out.println(grid2.get(i).get(j));
            }
        }
        //sets all the squares(pipes) in the top row to null (due to shortcomings of arrayList)
        for (int i = 1; i < 13; i++) {
            grid2.get(i).set(0, null);
        }

        //if i use pipe constructor Pipe(List<Block> blocks, Color colour) in setting up default pipes, this pipe(1,1) and all other pipes become null ? 
        System.out.println("in grid2 1  1" + grid2.get(1).get(1)); //ERROR HERE 

        this.grid = grid2;
        System.out.println(this.grid.get(1).get(1));

        //moves each generated pipe to the correct grid square (using its grid reference)
        this.updatePipePosition();

    }

    private int getRandomNumber(int max) {
        int randomNumber = (int) (Math.random() * max);
        return (randomNumber + 1);
    }

    public void setupDefaultPipes() {
        //set up the default pipes (each with their respective blocks)
        List<Block> pipeABlock = new ArrayList<Block>() {
            {
                this.add(new Block(0, 30, 100, 40, 30, 0, 40, 100, 0, 30, 100, 40, 30, 0, 40, 100));
            }
        };
        this.pipeA = new Pipe(pipeABlock);//, Color.web("#54fb37");

        List<Block> pipeBBlocks = new ArrayList<Block>() {
            {
                this.add(new Block(30, 0, 40, 70, 30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40));
                this.add(new Block(30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40, 30, 0, 40, 70));
            }
        };
        this.pipeB = new Pipe(pipeBBlocks);//, Color.PINK);

        List<Block> pipeCBlocks = new ArrayList<Block>() {
            {
                this.add(new Block(30, 30, 40, 70, 0, 30, 70, 40, 30, 0, 40, 70, 30, 30, 70, 40));
                this.add(new Block(0, 30, 100, 40, 30, 0, 40, 100, 0, 30, 100, 40, 30, 0, 40, 100));
            }
        };
        this.pipeC = new Pipe(pipeCBlocks);//, Color.ORANGE);
    }

    private void updatePipePosition() {
        for (int i = 1; i < this.numColumns - 1; i++) {
            for (int j = 1; j < this.numRows - 1; j++) {
                System.out.println("i j " + i + " , " + j);
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

    public void draw(GraphicsContext ctx) {
        for (int i = 1; i < this.grid.size(); i++) {
            for (int j = 1; j < this.grid.get(1).size(); j++) {
                Pipe pipe = this.grid.get(i).get(j);
                pipe.draw(ctx, Color.PINK);
            }
        }
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
        for (int i = 0; i < ctx.getCanvas().getWidth(); i += 100) {
            ctx.fillRect(i, 0, 1, ctx.getCanvas().getHeight());
        }
        for (int j = 0; j < ctx.getCanvas().getHeight(); j += 100) {
            ctx.fillRect(0, j, ctx.getCanvas().getWidth(), 1);
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

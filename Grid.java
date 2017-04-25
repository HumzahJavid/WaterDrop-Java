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
        //issue with grid
        this.numColumns = numColumns;
        this.numRows = numRows;

        this.setupDefaultPipes();
        //System.out.println("DEBUG: num columns" + numColumns);
        List<List<Pipe>> grid2 = new ArrayList<List<Pipe>>(numColumns);

        for (int i = 0; i < numColumns; i++) {
            grid2.add(new ArrayList<Pipe>(numRows));
        }

        for (int i = 0; i < numColumns; i++) { //i can be 1
            for (int j = 0; j < numRows; j++) { //j can be 1
                grid2.get(i).add(pipeB.deepClone());
            }
        }

        this.grid = grid2;
        this.updatePipePosition();

    }

    public void setupDefaultPipes() {
        //set up the default pipes (each with their respective blocks)
        List<Block> pipeABlock = new ArrayList<Block>() {
            {
                this.add(new Block(0, 30, 100, 40, 30, 0, 40, 100, 0, 30, 100, 40, 30, 0, 40, 100));
            }
        };
        this.pipeA = new Pipe(pipeABlock);

        List<Block> pipeBBlocks = new ArrayList<Block>() {
            {
                this.add(new Block(30, 0, 40, 70, 30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40));
                this.add(new Block(30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40, 30, 0, 40, 70));
            }
        };
        this.pipeB = new Pipe(pipeBBlocks);

        List<Block> pipeCBlocks = new ArrayList<Block>() {
            {
                this.add(new Block(30, 0, 40, 70, 30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40));
                this.add(new Block(30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40, 30, 0, 40, 70));
            }
        };
        this.pipeC = new Pipe(pipeCBlocks);
    }

    private void updatePipePosition() {
        for (int i = 1; i < 13; i++) {
            for (int j = 1; j < 7; j++) {
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

        Pipe zPipe = this.grid.get(0).get(0);
        System.out.println("zpipe0 0  = " + zPipe);

        Pipe zPipe2 = this.grid.get(0).get(1);
        System.out.println("zpipe2 0 1" + zPipe2);
    }

    public void draw(GraphicsContext ctx) {
        for (List<Pipe> Column : this.grid) {
            for (Pipe pipe : Column) {
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
        for (int i = 0; i < ctx.getCanvas().getWidth(); i+=100) {
                ctx.fillRect(i, 0, 1, ctx.getCanvas().getHeight());
        }
        for (int j = 0; j < ctx.getCanvas().getHeight(); j+=100) {
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

        return string;
    }
}

package waterdrop;

/**
 *
 * @author humzah
 */
import java.util.List;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Grid {

    List<List<Pipe>> grid;
    int numColumns;
    int numRows;

    Pipe pipeA;
    Pipe pipeB;

    Grid(int numColumns, int numRows) {
        //issue with grid
        this.setupDefaultPipes();
        System.out.println("DEBUG: num columns" + numColumns);
        this.grid = new ArrayList<List<Pipe>>(numColumns);
        for (List<Pipe> column : this.grid) {
            column = new ArrayList<Pipe>(numRows);
            for (Pipe pipe : column) {
                pipe = this.pipeA;
            }
        }

        System.out.println("DEBUG: the pipe in position 0 4 is (should be pipeA)");
        Pipe extracted = this.grid.get(0).get(0);
        System.out.println(extracted);
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
    }

    private void updatePipePosition() {
        System.out.println("DEBUG: grid.size = (should be same as numColumns)" + this.grid.size());
        for (int i = 0; i < this.grid.size(); i++) {
            for (int j = 0; j < this.grid.get(0).size(); j++) {
                List<Block> blocks = grid.get(i).get(j).blocks;
                for (Block block : blocks) {
                    //for each blocks orientation update the value of ONE TWO THREE FOUR
                    block.orientation.changePositions(i, j);
                }
            }
        }
    }

    public void draw(GraphicsContext ctx) {
        for (List<Pipe> Column : this.grid) {
            for (Pipe pipe : Column) {
                pipe.draw(ctx, Color.BLUE);
            }
        }
    }
}

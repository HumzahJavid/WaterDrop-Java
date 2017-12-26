/**
 *
 * @author humzah
 */
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

//new cloning library (used to make deep copies of pipes)
//https://github.com/kostaskougios/cloning
import com.rits.cloning.*;
import javafx.scene.paint.Paint;

public class Pipe {

    int leftEdge;
    int rightEdge;
    int topEdge;
    int bottomEdge;
    List<Block> blocks;
    int orientation;
    Color colour;
    boolean leftConnected;
    boolean rightConnected;
    boolean topConnected;
    boolean bottomConnected;

    Pipe(List<Block> blocks, Color colour) {
        this.blocks = blocks;
        this.updatesEdges();
        this.orientation = 1;
        this.colour = colour;
        this.leftConnected = false;
        this.rightConnected = false;
        this.topConnected = false;
        this.bottomConnected = false;
    }

    //change order of blocks and colour
    Pipe(List<Block> blocks) {
        this.blocks = blocks;
        this.updatesEdges();
        this.orientation = 1;
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    public void setBlocks(List<Block> newBlocks) {
        this.blocks = newBlocks;
    }

    public void addBlock(Block block) {
        this.blocks.add(block);
    }

    public void draw(GraphicsContext ctx) {
        ctx.setFill(this.colour);
        for (Block block : this.getBlocks()) {
            block.draw(ctx);
        }
    }

    public void draw(GraphicsContext ctx, Color colour) {
        ctx.setFill(colour);
        for (Block block : this.getBlocks()) {
            block.draw(ctx);
        }
    }

    public void updatesEdges() {
        int[] main = this.blocks.get(0).orientation.currentOrientation;
        this.leftEdge = main[0];
        this.rightEdge = main[0] + main[2];
        this.topEdge = main[1];
        this.bottomEdge = main[1] + main[3];
        for (Block block : this.blocks) {
            int[] next = block.orientation.currentOrientation;

            if (next[0] < this.leftEdge) {
                this.leftEdge = next[0];
            } else {
                this.leftEdge = main[0];
            }

            if ((next[0] + next[2]) > this.rightEdge) {
                this.rightEdge = next[0] + next[2];
            } else {
                this.rightEdge = main[0] + main[2];
            }

            if (next[1] < this.topEdge) {
                this.topEdge = next[1];
            } else {
                this.topEdge = main[1];
            }

            if ((next[1] + next[3]) > this.topEdge) {
                this.bottomEdge = next[1] + next[3];
            } else {
                this.bottomEdge = main[1] + main[3];
            }
            block.update();
        }
    }

    public void rotate(GraphicsContext ctx, int direction) {
        for (Block block : this.blocks) {
            //erase oldshape
            this.draw(ctx, Color.WHITE);

            switch (direction) {
                case 1: {
                    if (this.orientation == 4) {
                        this.orientation = 1;
                        block.rotate();
                    } else {
                        this.orientation += direction;
                        block.rotate();
                    }
                }
                break;
                case -1: {
                    if (this.orientation == 1) {
                        this.orientation = 4;
                        block.rotate();
                    } else {
                        this.orientation += direction;
                        block.rotate();
                    }
                }
            }
            this.updatesEdges();
            //draw new shape
            this.draw(ctx);
        }
    }

    public void checkConnections(Grid grid, SinglyList list) {
        //i need to check if things are no longer connected LATER
        //check if multiple connections are made in this function by keeping a count, 
        //if it is, we need to clone the list and give it a fork
        int[] gridRef = grid.getGridReference(this);
        int x = gridRef[0];
        int y = gridRef[1];

        //left pipe
        Pipe leftPipe = grid.grid.get(x - 1).get(y);
        //right pipe
        Pipe rightPipe = grid.grid.get(x + 1).get(y);
        //top pipe
        Pipe topPipe = grid.grid.get(x).get(y - 1);
        //bottom pipe
        Pipe bottomPipe = grid.grid.get(x).get(y + 1);

        if ((this.leftEdge == leftPipe.rightEdge) && (this.leftConnected == false) && (!list.isInList(this))) {
            this.leftConnected = true;
        }

        if ((this.rightEdge == rightPipe.leftEdge) && (this.rightConnected == false) && (!list.isInList(this))) {
            this.rightConnected = true;
        }

        if ((this.topEdge == topPipe.bottomEdge) && (this.topConnected == false) && (!list.isInList(this))) {
            this.topConnected = true;
        }

        if ((this.bottomEdge == bottomPipe.topEdge) && (this.bottomConnected == false) && (!list.isInList(this))) {
            this.bottomConnected = true;
        }

        if (this.leftConnected || this.rightConnected || this.topConnected || this.bottomConnected) {
            System.out.println("list length before" + list.length);
            list.add(this);

            System.out.println("list length after" + list.length);
            System.out.println("pipe added = " + this);
        }
    }

    public void checkPipeNeedsRemoving(SinglyList list) {
        boolean isConnected = true;
        
        //get the previous pipe check if its no longer connected to it
        int indexOfPreviousPipe = list.getIndexOf(this) - 1;
        Pipe previousPipe = list.getNodeAt(indexOfPreviousPipe).data;

        if (this.leftEdge != previousPipe.rightEdge) {
            this.leftConnected = false;
        }

        if (this.rightEdge != previousPipe.leftEdge) {
            this.rightConnected = false;
        }

        if (this.bottomEdge != previousPipe.topEdge) {
            this.bottomConnected = false;
        }

        if (this.topEdge != previousPipe.bottomEdge) {
            this.topConnected = false;
        }
        
        if (!this.leftConnected && !this.rightConnected && !this.topConnected && !this.bottomConnected) {
            System.out.println("list length before" + list.length);
            list.remove(this);

            System.out.println("list length after" + list.length);
            System.out.println("pipe removed = " + this);
        }
    }

    public void checkPipeCanBeAddedToList(Grid grid, SinglyList list) {
        int[] gridRef = grid.getGridReference(this);
        int x = gridRef[0];
        int y = gridRef[1];

        //left pipe
        System.out.println("list length before = " + list.length);
        Pipe lastPipe = list.getNodeAt(list.length).data;

        if (this.leftEdge == lastPipe.rightEdge) {
            this.leftConnected = true;
        }

        if (this.rightEdge == lastPipe.leftEdge) {
            this.rightConnected = true;
        }

        if (this.bottomEdge == lastPipe.topEdge) {
            this.bottomConnected = true;
        }

        if (this.topEdge == lastPipe.bottomEdge) {
            this.topConnected = true;
        }

        if (this.leftConnected || this.rightConnected || this.topConnected || this.bottomConnected) {
            System.out.println("list length before" + list.length);
            list.add(this);

            System.out.println("list length after" + list.length);
            System.out.println("pipe added = " + this);
        }
    }

    @Override
    public String toString() {
        String pipeString = "";
        pipeString += (this.blocks.toString());
        String pipeString2 = "[]";

        return pipeString;
    }
}

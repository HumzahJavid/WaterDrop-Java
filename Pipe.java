/**
 *
 * @author humzah
 */
import java.util.ArrayList;
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
	int gridX;
	int gridY;
    String pipeType;

    Pipe(List<Block> blocks, Color colour) {
        this.blocks = blocks;
        this.updatesEdges();
        this.orientation = 1;
        this.colour = colour;
		this.gridX = 0;
		this.gridY = 0;
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

			if ((next[1] + next[3]) > this.bottomEdge) {
                this.bottomEdge = next[1] + next[3];
            } else {
                this.bottomEdge = main[1] + main[3];
            }
            block.update();
        }
    }

    public void rotate(GraphicsContext ctx, int direction) {
		//System.out.println("Before rotating left : " + this.leftEdge + " Right: " + this.rightEdge + " Top: " + this.topEdge + " Bottom: " + this.bottomEdge);
        for (Block block : this.blocks) {
            //erase oldshape
            this.draw(ctx, Color.WHITE);

            switch (direction) {
                case 1: {
                    if (this.orientation == 4) {
                        this.orientation = 1;
                        block.rotate(direction);
                    } else {
                        this.orientation += direction;
                        block.rotate(direction);
                    }
                }
                break;
                case -1: {
                    if (this.orientation == 1) {
                        this.orientation = 4;
                        block.rotate(direction);
                    } else {
                        this.orientation += direction;
                        block.rotate(direction);
                    }
                }
				break;
            }
            this.updatesEdges();
            //draw new shape
            this.draw(ctx);
        }
		//System.out.println("After rotating left: " + this.leftEdge + " Right: " + this.rightEdge + " Top: " + this.topEdge + " Bottom: " + this.bottomEdge);
    }
	
	public void setGridReference(int x, int y){
		this.gridX = x;
		this.gridY = y;
	}
	
	public int getGridX(){
		return this.gridX;
	}
	
	public int getGridY(){
		return this.gridY;
	}
	
	public String getGridReference(){
		return "(" + this.gridX + "," + this.gridY + ")";
	}
	
    @Override
    public String toString() {
        String pipeString = "";
        //pipeString += (this.blocks.toString());
		//pipeString += "Left: " + this.leftEdge + " Right: " + this.rightEdge + " Top: " + this.topEdge + " Bottom: " + this.bottomEdge;
		pipeString += this.getGridReference();
        pipeString = this.pipeType;

        return pipeString;
    }
}

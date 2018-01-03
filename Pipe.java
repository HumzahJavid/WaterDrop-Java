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
    boolean leftConnected;
    boolean rightConnected;
    boolean topConnected;
    boolean bottomConnected;
	Pipe parent;
	ArrayList<Pipe> children;
	boolean inTree;
	int gridX;
	int gridY;

    Pipe(List<Block> blocks, Color colour) {
        this.blocks = blocks;
        this.updatesEdges();
        this.orientation = 1;
        this.colour = colour;
        this.leftConnected = false;
        this.rightConnected = false;
        this.topConnected = false;
        this.bottomConnected = false;
		this.inTree = false;
		this.children = new ArrayList<Pipe>();
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
	
	public void addChild(Pipe child){
		this.children.add(child);
		child.setParent(this);
		//return child;
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
		//System.out.println("After rotating left: " + this.leftEdge + " Right: " + this.rightEdge + " Top: " + this.topEdge + " Bottom: " + this.bottomEdge);
    }
	
    public void checkConnections(Grid grid, GraphicsContext ctx) {
		System.out.println("running checkconnections");
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

		if (leftPipe == null){
			System.out.println("left pipe is null");
		} else if (leftPipe.children.size() > 1) {
			System.out.println("the left pipe has " + leftPipe.children.size() + " children");
			for(Pipe child : topPipe.children){
				System.out.println("Child: " + child);
			}
		} else {			
			if ((this.leftEdge == leftPipe.rightEdge) && (this.leftConnected == false) && (leftPipe.inTree())) {
				this.leftConnected = true;
				leftPipe.addChild(this);
				System.out.println("Connected to left : " + leftPipe);
			}
		}

		if (rightPipe == null){
			System.out.println("right pipe is null");
		} else if (rightPipe.children.size() > 1) {
			System.out.println("the rightPipe  has " + rightPipe.children.size() + " children");
			for(Pipe child : topPipe.children){
				System.out.println("Child: " + child);
			}
		} else {
			if ((this.rightEdge == rightPipe.leftEdge) && (this.rightConnected == false) && (rightPipe.inTree())) {
				this.rightConnected = true;
				rightPipe.addChild(this);
				System.out.println("Connected to right: " + rightPipe);
			}
		}

		if (topPipe == null){
			System.out.println("top pipe is null");
		} else if (topPipe.children.size() > 1) {
			System.out.println("the topPipe  has " + topPipe.children.size() + " children");
			for(Pipe child : topPipe.children){
				System.out.println("Child: " + child);
			}
		}  else {
			if ((this.topEdge == topPipe.bottomEdge) && (this.topConnected == false) && (topPipe.inTree())) {
				this.topConnected = true;
				topPipe.addChild(this);
				System.out.println("Connected to topPipe : " + topPipe);
			}
		}

		if (bottomPipe == null){
			System.out.println("bottom pipe is null");
		} else if (bottomPipe.children.size() > 1) {
			System.out.println("the bottom  has " + bottomPipe.children.size() + " children");
			for(Pipe child : topPipe.children){
				System.out.println("Child: " + child);
			}
		}  else {
			if ((this.bottomEdge == bottomPipe.topEdge) && (this.bottomConnected == false) && (bottomPipe.inTree())) {
				this.bottomConnected = true;
				bottomPipe.addChild(this);
				System.out.println("Connected to bottom : " + bottomPipe);
			}

			if (this.leftConnected || this.rightConnected || this.topConnected || this.bottomConnected) {
				System.out.println("pipe added = " + this);
				//System.out.println(this.leftConnected + " " + this.rightConnected + " " + this.topConnected + " " + this.bottomConnected);
				this.inTree = true;
			}
		}
    }

    public void checkPipeNeedsRemoving(Grid grid, GraphicsContext ctx) {
		//checks for disconnected adjacent pipes (which were previously connected)
		//if disconnected remove the parent child association
		System.out.println("running check pipe needs removing");

        boolean isConnected = true;
		int[] gridRef = grid.getGridReference(this);
        int x = gridRef[0];
        int y = gridRef[1];

        Pipe leftPipe = grid.grid.get(x - 1).get(y);
        Pipe rightPipe = grid.grid.get(x + 1).get(y);
        Pipe topPipe = grid.grid.get(x).get(y - 1);
        Pipe bottomPipe = grid.grid.get(x).get(y + 1);
		
		if (leftPipe == null){
			System.out.println("left pipe is null");
		} else {
			if ((this.leftEdge != leftPipe.rightEdge) /*&& (leftPipe.inTree()) && this.leftConnected && */&& (this.parent == leftPipe)) {
				System.out.println("left disconnected");
				System.out.println("left was parent");
				this.leftConnected = false;
				leftPipe.removeChild(this, grid, ctx);
			} else if ((this.leftEdge != leftPipe.rightEdge) && (leftPipe.getParent() == this)){
				//left child pipe disconnected
				System.out.println("left disconnected");
				System.out.println("left was child");
				this.leftConnected = false;
				this.removeChild(leftPipe, grid, ctx);
			}
		}

		if (rightPipe == null){
			System.out.println("right pipe is null");
		} else {
			if ((this.rightEdge != rightPipe.leftEdge) && (this.parent == rightPipe)) {
				System.out.println("right disconnected");
				this.rightConnected = false;
				rightPipe.removeChild(this, grid, ctx);
			} else if ((this.rightEdge != rightPipe.leftEdge) && (rightPipe.getParent() == this)){
				//right child pipe disconnected
				System.out.println("right disconnected");
				System.out.println("right was child");
				this.rightConnected = false;
				this.removeChild(leftPipe, grid, ctx);
			}
		}
		
		if (bottomPipe == null){
			System.out.println("bottom pipe is null");
		} else {
			if ((this.bottomEdge != bottomPipe.topEdge) && (this.parent == bottomPipe)) {
				System.out.println("bottom disconnected");
				this.bottomConnected = false;
				bottomPipe.removeChild(this, grid, ctx);
			} else if ((this.bottomEdge != bottomPipe.topEdge) && (bottomPipe.getParent() == this)){
				//bottom child pipe disconnected
				System.out.println("bottom disconnected");
				System.out.println("bottom was child");
				this.bottomConnected = false;
				this.removeChild(bottomPipe, grid, ctx);
			}
		}

		if (topPipe == null){
			System.out.println("top pipe is null");
		} else {
			if ((this.topEdge != topPipe.bottomEdge) && (this.parent == topPipe)) {
				System.out.println("top disconnected");
				this.topConnected = false;
				topPipe.removeChild(this, grid, ctx);
			} else if ((this.topEdge != topPipe.bottomEdge) && (topPipe.getParent() == this)){
				//top child pipe disconnected
				System.out.println("top disconnected");
				System.out.println("top was child");
				this.topConnected = false;
				this.removeChild(topPipe, grid, ctx);
			}
		}
        
        if (!this.leftConnected && !this.rightConnected && !this.topConnected && !this.bottomConnected) {
			this.setInTree(false);
			this.removeChildren();
            System.out.println("pipe removed = " + this);
        }
    }
	
	public boolean inTree(){
		return this.inTree;
	}
	
	public Pipe getParent() {
		return this.parent;
	}

	public void removeChildren(){	
		//remove the immediately disconnected pipe 
		//then loop through all of that pipes children and remove each child reference recursively
		Pipe nextPotentialPipe;
		ArrayList<Pipe> childrenToRemove = new ArrayList<Pipe>();
		if (this.children == null){
			System.out.println(" This pipe has no children: " + this);
		} else {
			for (Pipe child : this.children){
				childrenToRemove.add(child);
				child.removeParent();
				child.setInTree(false);
			}
			this.children.removeAll(childrenToRemove);
			//process each pipe and remove its children
			for (Pipe pipe : childrenToRemove){
				pipe.removeChildren();
			}
		}
	}
	
	public void removeChild(Pipe child, Grid grid, GraphicsContext ctx){
		System.out.println("\n\n\n");
		System.out.println("Running remove child");
		System.out.println("Parent of child : " + child.getParent());
		System.out.println("Children of parent " + this.children);
		
		child.removeParent();
		this.children.remove(child);
		child.checkConnections(grid, ctx);
		child.checkPipeNeedsRemoving(grid, ctx);

		System.out.println("end of remove child)");
		System.out.println("Parent of child : " + child.getParent());
		System.out.println("Children of parent " + this.children);
	}
	
	public void removeParent(){
		this.parent = null;
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
	
	public void setParent(Pipe newParent){
		this.parent = newParent;
	}
	
	public void setInTree(boolean newInTree){
		this.inTree = newInTree;
	}
	
    @Override
    public String toString() {
        String pipeString = "";
        //pipeString += (this.blocks.toString());
		//pipeString += "Left: " + this.leftEdge + " Right: " + this.rightEdge + " Top: " + this.topEdge + " Bottom: " + this.bottomEdge;
        //pipeString += "Connected Left: " + this.leftConnected + " Right: " + this.rightConnected + " Top: " + this.topConnected + " Bottom: " + this.bottomConnected;
		pipeString += this.getGridReference();
		String pipeString2 = "[]";

        return pipeString;
    }
}

/**
 *
 * @author humzah
 */
import java.util.List;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

//new cloning library (used to make deep copies of pipes)
//https://github.com/kostaskougios/cloning
import com.rits.cloning.*;

@SuppressWarnings("serial")
public class Grid {

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
    
    // the default unsolved version of a level 
    private List<List<Pipe>> gridDefault;

    Grid(int numColumns, int numRows) {
		Pipe addedPipe;
        this.numColumns = numColumns;
        this.numRows = numRows;
        //used to clone pipes
        Cloner cloner = new Cloner();
        //creates the blueprint of the playable pipe pieces (each with 4 orientations)
        this.setupDefaultPipes();

        List<List<Pipe>> grid2 = new ArrayList<List<Pipe>>(numColumns);
        //creates the spaces for the pipes
        for (int i = 0; i < numColumns; i++) { //< 14 (pos of right border)
            grid2.add(new ArrayList<Pipe>(numRows));
        }

        //fills in playable pipes
        for (int i = 1; i < (numColumns - 1); i++) {
            for (int j = 1; j < numRows; j++) {

                int randomNum = this.getRandomNumber(3);
                switch (randomNum) {
                    case 1: {
						addedPipe = cloner.deepClone(this.pipeA);
                    }
                    break;
                    case 2: {
						addedPipe = cloner.deepClone(this.pipeB);
                    }
                    break;

                    case 3: {
						addedPipe = cloner.deepClone(this.pipeC);
                    }
                    break;

                    default: {
						addedPipe = cloner.deepClone(this.pipeA);
                    }
                }
				
                grid2.get(i).add(addedPipe);
				addedPipe.setGridReference(i, (j-1));
            }
        }
		
		grid2 = this.applyNullBorder(grid2);
        this.randomStart = getRandomNumber(numColumns - 2);
        this.randomEnd = getRandomNumber(numColumns - 2);
        grid2.get(this.randomStart).set(0, this.pipeStart);
        grid2.get(this.randomEnd).set(this.numRows - 1, this.pipeEnd);
        this.grid = grid2;
        //moves each generated pipe to the correct grid square (using its grid reference)
        this.updatePipePosition();
        this.updateStartEndPipePosition();
        
        this.gridDefault = cloner.deepClone(this.grid);
    }

    //load a previous level (grid)
    Grid(List<List<Pipe>> level){
        this.numColumns = level.size();
        this.numRows = level.get(0).size();
        this.grid = level;

        //find and store random start and end from loaded level;
        loadRandomStartAndEnd();
        this.pipeStart = this.grid.get(this.randomStart).get(0);
        this.pipeEnd = this.grid.get(this.randomEnd).get(this.numRows - 1);
    }

    public List<List<Pipe>> getGrid() {
        return this.grid;
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
        startPipe.setGridReference(this.randomStart, 0);
        List<Block> blocks = startPipe.blocks;
        for (Block block : blocks) {
            //for each blocks orientation update the value of ONE TWO THREE FOUR
            block.orientation.changePositions(this.randomStart, 0);
            block.update();
        }
        startPipe.updatesEdges();

        Pipe endPipe = this.grid.get(this.randomEnd).get(this.numRows - 1);
		endPipe.setGridReference(this.randomEnd, this.numRows - 1);
        List<Block> blocks2 = endPipe.blocks;
        for (Block block : blocks2) {
            //for each blocks orientation update the value of ONE TWO THREE FOUR
            block.orientation.changePositions(this.randomEnd, (this.numRows - 1));
            block.update();
        }
        endPipe.updatesEdges();
    }

    public void displayText(GraphicsContext ctx, int level){
        ctx.setFont(Font.font("Arial Bold", 20));
        ctx.setFill(Color.WHITE);
        ctx.fillText("Level: " + level, 6, 17);
    }

    public void draw(GraphicsContext ctx) {
        //draw most of the pipes in the grid
		for (int j = 1; j < numRows - 1; j++) {
			for (int i = 1; i < numColumns - 1; i++) {
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
	
	public List<List<Pipe>> applyNullBorder(List<List<Pipe>> grid){
		//left column
		for (int i = 0; i < numRows; i++){
			grid.get(0).add(null);
		}
		
		//right column
		for (int i = 0; i < numRows; i++){
			grid.get(numColumns - 1).add(null);
		}
		
		//sets all the squares(pipes) in the top row to null
        for (int i = 0; i < numColumns - 1; i++) {
            grid.get(i).set(0, null);
        }
		
		//bottom row
        for (int i = 1; i < numColumns - 1; i++) {
            grid.get(i).add(null);
        }
		return grid;
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

    //Will be used in main class to store a completed (solveable) level
    public List<List<Pipe>> getDefaultLevelGrid(){
        return this.gridDefault;
    }

    public int[] getGridReference(Pipe pipe) {
        int[] gridRef = new int[2];
        for (List<Pipe> pipeList : this.grid) {
            for (Pipe matchingPipe : pipeList) {
                if (matchingPipe == null) {
                    //used to ignore any grid spaces which do not have pipe assigned
                } else if (matchingPipe == pipe) {
                    gridRef[0] = this.grid.indexOf(pipeList);
                    gridRef[1] = pipeList.indexOf(matchingPipe);
                }
            }
        }
        return gridRef;
    }

    public int[] getSize() {
        int[] size = { this.numColumns, this.numRows };
        return size;
    }

    //after level from existing grid of pipes, this class does not have values set for randomStart and randomEnd.
    private void loadRandomStartAndEnd(){
        for (int i = 0; i < numColumns - 1; i++) {
            if (grid.get(i).get(0) != null){
                this.randomStart = i;
            }
            if (grid.get(i).get(this.numRows - 1) != null){
                this.randomEnd = i;
            }
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
	
	public void printer(){
		System.out.println("state of grid");
		for (int i = 0; i < numColumns; i++){
			System.out.println("i = " + i);
            System.out.println(this.grid.get(i));//.set(i, null);
		}
    }
    
    // identical to the constructor (with same args)
    // very slow!!!
    public void reset(int numColumns, int numRows) {
        Pipe addedPipe;
        this.numColumns = numColumns;
        this.numRows = numRows;
        // used to clone pipes
        Cloner cloner = new Cloner();
        // creates the blueprint of the playable pipe pieces (each with 4 orientations)
        this.setupDefaultPipes();

        List<List<Pipe>> grid2 = new ArrayList<List<Pipe>>(numColumns);
        // creates the spaces for the pipes
        for (int i = 0; i < numColumns; i++) { // < 14 (pos of right border)
            grid2.add(new ArrayList<Pipe>(numRows));
        }

        // fills in playable pipes
        for (int i = 1; i < 13; i++) {
            for (int j = 1; j < numRows; j++) {

                int randomNum = this.getRandomNumber(3);
                switch (randomNum) {
                case 1: {
                    addedPipe = cloner.deepClone(this.pipeA);
                }
                    break;
                case 2: {
                    addedPipe = cloner.deepClone(this.pipeB);
                }
                    break;

                case 3: {
                    addedPipe = cloner.deepClone(this.pipeC);
                }
                    break;

                default: {
                    addedPipe = cloner.deepClone(this.pipeA);
                }
                }

                grid2.get(i).add(addedPipe);
                addedPipe.setGridReference(i, (j - 1));
            }
        }

        grid2 = this.applyNullBorder(grid2);
        this.randomStart = getRandomNumber(numColumns - 2);
        this.randomEnd = getRandomNumber(numColumns - 2);
        grid2.get(this.randomStart).set(0, this.pipeStart);
        grid2.get(this.randomEnd).set(this.numRows - 1, this.pipeEnd);
        this.grid = grid2;
        // moves each generated pipe to the correct grid square (using its grid
        // reference)
        this.updatePipePosition();
        this.updateStartEndPipePosition();
        System.out.println("Reset grid");
    }
}

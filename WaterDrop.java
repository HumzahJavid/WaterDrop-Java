//test to consider
/*
how many times can a user complete one level and skip it after making (0 or moves) ? 
above but connected to goal pipe but then disconnects to skip it 

check the size of levels list in these cases


//add a line to say if no levels have been saved and a user skips, then generate a new random level
//DOnt add levels which already exist in the array
do both of the above using the algorithm from waterdrop JS 
https://github.com/HumzahJavid/portfolio/blob/51d3ab63e975e8376311903027a6ce0bc1201c15/Websites/%233%20WaterDrop/myGame.js#L108-L185
    //COMPLETE

        //if the user completed a randomly generated level
            
                    //next level will be randomly generated



        //if the user completes a level from solveable levels array
            
                    //next level will be randomly generated


    //SKIPPED

        //skipped a randomly generated level 

                        //if no levels have been stored in localStorage
                        createRandomLevel = true;
                    Else

                createRandomLevel = false;



        //skipped a level from solveable level array
                    createRandomLevel = false;









*/
/**
 *
 * @author humzah
 */
import java.util.ArrayList;
import java.util.List;
//javafx standard imports?
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
//canvas 
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
//event handling
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
//colour of pipe
import javafx.scene.paint.Color;
//will be used in fonts when displaying level and score
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;

//loading a level
import java.lang.reflect.Type;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WaterDrop extends Application {
    Grid grid;
    Graph graph;
    Canvas canvas;
    GraphicsContext ctx;
    int level;
	boolean startPipeClicked;
    boolean endPipeClicked;
    
    Gson gson;
    Type type;
    ArrayList<String> savedLevels = new ArrayList<String>();
    boolean createRandomLevel = true;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        Group root = new Group();
        Scene theScene = new Scene(root);

        theStage.setTitle("WaterDrop prototype");
        theStage.setScene(theScene);

        canvas = new Canvas(1400, 700);
        ctx = canvas.getGraphicsContext2D();
        double height = ctx.getCanvas().getHeight();
        double width = ctx.getCanvas().getWidth();

        int numRows = (int) height / 100;
        int numColumns = (int) width / 100;

        int numberOfPipes = ((numColumns - 2) * (numRows - 2)) + 2;
        level = 1;
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(2);
		System.out.println("numColumns " + numColumns);
        System.out.println("numRows " + numRows);
        System.out.println("numPipes" + numberOfPipes);
        grid = new Grid(numColumns, numRows);

        gson = new Gson();
        type = new TypeToken<List<List<Pipe>>>() {}.getType();

        grid.drawBorders(ctx);
        grid.draw(ctx);
        grid.drawBoxes(ctx);
        grid.displayText(ctx, level);
		System.out.println("------------------------");
		graph = new Graph(grid, numberOfPipes);
        //Creating the mouse event handler 
        EventHandler<javafx.scene.input.MouseEvent> eventHandler
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                int direction;
                //if left click, rotate clockwise
				if (mouseEvent.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                    direction = 1;
                //else if right click, rotate anticlokwise
				} else if (mouseEvent.getButton() == javafx.scene.input.MouseButton.SECONDARY){
                    direction = -1;
                //unforseen circumstances, rotate clockwise
				} else {
					direction = 1;
				}
                rotatePipe(mouseEvent.getSceneX(), mouseEvent.getSceneY(), grid, ctx, direction);
                graph.calculateMatrix();
                if (graph.isLevelFinished() && endPipeClicked){
                    newLevel(theStage, canvas, ctx, numberOfPipes, false);
                } else if (!graph.isLevelFinished() && endPipeClicked){
                    // skipped level
                    newLevel(theStage, canvas, ctx, numberOfPipes, true);
                }
            }
        };

        //Adding the event handler 
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

        //Remove event handler
        //canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        root.getChildren().addAll(canvas);
        theStage.show();
    }

    public void rotatePipe(double x, double y, Grid grid, GraphicsContext ctx, int direction) {
        //loops through grid to find and rotate the pipe that was clicked
        List<List<Pipe>> pipeGrid = grid.getGrid();
		startPipeClicked = false;
		endPipeClicked = false;
        for (List<Pipe> pipeList : pipeGrid) {
            for (Pipe pipe : pipeList) {
                if (pipe == null) {
                    //used to ignore any grid spaces which do not have pipe assigned
                } else if (((x >= pipe.leftEdge) && (x <= pipe.rightEdge)) && ((y >= pipe.topEdge) && (y <= pipe.bottomEdge))) {
					if (pipe == grid.pipeStart){
						startPipeClicked = true;
					} else if (pipe == grid.pipeEnd){
						endPipeClicked = true;
					} else {
						pipe.rotate(ctx, direction);
					}
                }
            }
        }
    }

    public void newLevel(Stage theStage, Canvas canvas, GraphicsContext ctx, int numberOfPipes, Boolean skipped) {
        double height = canvas.getHeight();
        double width = canvas.getWidth();
        int numColumns, numRows;
        List<List<Pipe>> levelToSave = grid.getDefaultLevelGrid();
        
        // a level was completed WITHOUT skipping
        if (!skipped){
        
            System.out.print("Completed level ");
            // a randomly generated level was completed
            if (createRandomLevel) {
                System.out.println(" randomly generated");
                //save the level for future use
                saveLevel(levelToSave);
            } else {
                System.out.println(" loaded/solevable");
            }
            //generate a new random level
            height+=100;
            //increment canvas size by 100, to add a new row
            canvas.setHeight(height);
            level+=1;
            numRows = (int) height / 100;
            numColumns = (int) width / 100;
            grid = new Grid(numColumns, numRows);
            createRandomLevel = true;
            
            System.out.println("THE NEXT LEVEL WILL BE RG");
            //resetting grid takes a long time, faster to run the constructor again
            //grid.reset(numColumns, numRows);

        // a level was skipped 
        } else{
            System.out.print("Skipped level ");
            if (createRandomLevel){
                System.out.print("a randomly generated one ");
                //if no levels exists
                if(savedLevels.size() == 0){
                    System.out.println("No levels exist, so generating a new one");
                    //generate a new random level
                    height+=100;
                    //increment canvas size by 100, to add a new row
                    canvas.setHeight(height);
                    level+=1;
                    numRows = (int) height / 100;
                    numColumns = (int) width / 100;
                    grid = new Grid(numColumns, numRows);
                    createRandomLevel = true;
                } else { 
                    //load a previous one
                    System.out.println("Loading a previosuly completed one\n");
                    int loadLevelIndex = (int)(Math.random() * savedLevels.size());
                    List<List<Pipe>> loaded = gson.fromJson(savedLevels.get(loadLevelIndex), type);
                    System.out.println("Load level from index " + loadLevelIndex);
                    System.out.println(loaded);
                    grid = new Grid(loaded);
                    numColumns = grid.getSize()[0];
                    numRows = grid.getSize()[1];
                    createRandomLevel = false;
                }
            //a previously completed level (a solveable level) was skipped 
            } else {
                System.out.print("Skipped level ");
                //load a previous one
                System.out.print("a previosuly completed one\n ");
                int loadLevelIndex = (int)(Math.random() * savedLevels.size());
                List<List<Pipe>> loaded = gson.fromJson(savedLevels.get(loadLevelIndex), type);
                System.out.println("Load level from index " + loadLevelIndex);
                System.out.println(loaded);
                grid = new Grid(loaded);
                numColumns = grid.getSize()[0];
                numRows = grid.getSize()[1];
                createRandomLevel = false;
            }
        }
        ctx.clearRect(0, 0, width, height);
        numberOfPipes = ((numColumns - 2) * (numRows - 2)) + 2;
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(2);
        System.out.println("numColumns " + numColumns);
        System.out.println("numRows " + numRows);
        System.out.println("numPipes" + numberOfPipes);
        grid.drawBorders(ctx);
        grid.draw(ctx);
        grid.drawBoxes(ctx);
		System.out.println("Loading next level : " + level);
        grid.displayText(ctx, level);
        System.out.println("------------------------");
        graph.reset(grid, numberOfPipes);
    }

    public void saveLevel(List<List<Pipe>> defaultLevel){
        String levelToSave = gson.toJson(defaultLevel, type);
        savedLevels.add(levelToSave);
        System.out.println("Number of levels saved = " + savedLevels.size());
    }
}

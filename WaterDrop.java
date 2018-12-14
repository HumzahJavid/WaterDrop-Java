/**
 *
 * @author humzah
 */
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
    // temporary level (whilst testing)
    String level1;
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
        //temp save of first level
        level1 = gson.toJson(grid.grid, type);

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
        
        if (!skipped) {
            height+=100;
            //increment canvas size by 100, to add a new row
            canvas.setHeight(height);
            //canvas.setWidth(canvas.getWidth() + 100);
            level+=1;
            numRows = (int) height / 100;
            numColumns = (int) width / 100;
            grid = new Grid(numColumns, numRows);
            //resetting grid takes a long time, faster to run the constructor again
            //grid.reset(numColumns, numRows);
        } else{
            System.out.println("Skipped the level... so loading original level");
            List<List<Pipe>> loaded = gson.fromJson(level1, type);
            System.out.println(loaded);
            grid = new Grid(loaded);
            numColumns = grid.getSize()[0];
            numRows = grid.getSize()[1];
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
}

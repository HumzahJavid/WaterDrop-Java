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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
//colour of pipe
import javafx.scene.paint.Color;
//will be used in fonts when displaying level and score
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.GridPane;
import javafx.geometry.Orientation;
import java.lang.Math;

public class WaterDrop extends Application {
    Grid grid;
    Graph graph;
    Canvas canvas;
    GraphicsContext ctx;
    int level;
	boolean startPipeClicked;
    boolean endPipeClicked;
    double oldScrollValue = 0.0;
    double newScrollValue = oldScrollValue;
    boolean useDrag = false;
    ScrollBar vScroll;
    public static void main(String[] args) {
        launch(args);
    }
    public static ScrollBar createScrollBar(Orientation orientation, double fullSize, double canvasSize) {
        ScrollBar sb = new ScrollBar();
        sb.setOrientation(orientation);
        sb.setMax(fullSize - canvasSize);
        sb.setVisibleAmount(canvasSize);
        return sb;
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

        vScroll = createScrollBar(Orientation.VERTICAL, 10000, 300);


        System.out.println("getting increment " + vScroll.getUnitIncrement());

        ScrollBar hScroll = createScrollBar(Orientation.HORIZONTAL, 10000, 300);

        GridPane scrollPane = new GridPane();
        //scrollPane.addColumn(0, canvas, hScroll);
        //scrollPane.add(vScroll, 1, 0);
        scrollPane.addRow(0, canvas, vScroll);



        int numRows = (int) height / 100;
        int numColumns = (int) width / 100;
        
        // the scroll bar should not be scrollable when there are 7 rows
        int scrollBarHeight = (int) height;
        vScroll.setVisibleAmount(0);
        vScroll.setMax(0);
        vScroll.setUnitIncrement(100);
        vScroll.setBlockIncrement(200);
        System.out.println("the scrolling visible amount " + vScroll.getVisibleAmount());


        int numberOfPipes = ((numColumns - 2) * (numRows - 2)) + 2;
        level = 1;
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(2);
		System.out.println("numColumns " + numColumns);
        System.out.println("numRows " + numRows);
        System.out.println("numPipes" + numberOfPipes);
        grid = new Grid(numColumns, numRows);
        grid.drawBorders(ctx);
        grid.draw(ctx);
        grid.drawBoxes(ctx);
        grid.displayText(ctx, level);
		System.out.println("------------------------");
        graph = new Graph(grid, numberOfPipes);
        
        // keyboard event handler
        EventHandler<javafx.scene.input.KeyEvent> keyInputEventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent keyEvent){
                switch (keyEvent.getCode()) {
                    case S: //KeyCode.S
                        System.out.println("skipping level");
                        newLevel(theStage, canvas, ctx, numberOfPipes, false, numRows);
                        break;
                    case UP:
                        System.out.println("implement move view up by one row");
                        break;
                    case DOWN:
                        System.out.println("implement move view down by one row");
                        break;
                    default:
                        break;
                }
            }
        };

        //Creating the mouse event handler 
        EventHandler<javafx.scene.input.MouseEvent> eventHandler = new EventHandler<MouseEvent>() {
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
                    newLevel(theStage, canvas, ctx, numberOfPipes, false, numRows);
                } else if (!graph.isLevelFinished() && endPipeClicked){
                    // skipped level
                    newLevel(theStage, canvas, ctx, numberOfPipes, true, numRows);
                }
            }
        };

        //Adding the event handler
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        // allows the keyboard events to be detected
        canvas.setFocusTraversable(true);
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, keyInputEventHandler);
        // create and add the event handler for scrollbar
        vScroll.addEventHandler(MouseEvent.ANY, e-> {
            // System.out.println("event = " + e);
            if (e.getEventType() == MouseEvent.MOUSE_CLICKED){
                newScrollValue = vScroll.getValue();
                System.out.println("mouse CLICKED at value " + newScrollValue + " \nrounded value = " + roundScrollBar(newScrollValue));
                vScroll.setValue(roundScrollBar(newScrollValue));
                
                ctx.setFill(Color. WHITE);
                ctx.fillRect(0, 0, width, height);
                int viewPort = roundScrollBar(vScroll.getValue());
                boolean reachedBottomOfView = false;
                if (viewPort == vScroll.getMax()){
                    reachedBottomOfView = true;
                }
                //redundant second arg (could be vScroll.getValue()), but is defensive
                grid.draw(ctx, viewPort, reachedBottomOfView);
            } else if (e.getEventType() == MouseEvent.MOUSE_RELEASED){
                newScrollValue = vScroll.getValue();
                if (useDrag){
                    System.out.println(" the drag var was set to true = " + vScroll.getValue() + " -> rounded: " + roundScrollBar(vScroll.getValue()));
                    vScroll.setValue(roundScrollBar(vScroll.getValue()));
                } else {
                    System.out.println("it was currently RELEASAED at value " + newScrollValue);
                    System.out.println("draw grid beginning from " + roundScrollBar(newScrollValue));
                }
                
                System.out.println("setting drag to false... update view port, edit scrolling length to match one one row (increase in increments of numRows)");
                //grid.draw()
                
                //whether or not the mouse was dragged, set it to false (for next drag detection loop)
                useDrag = false;
                
                //reset canvas 
                ctx.setFill(Color.WHITE);
                ctx.fillRect(0, 0, width, height);
                int viewPort = roundScrollBar(vScroll.getValue());
                boolean reachedBottomOfView = false;
                if (viewPort == vScroll.getMax()){
                    reachedBottomOfView = true;
                }
                //redundant second arg (could be vScroll.getValue()), but is defensive
                grid.draw(ctx, viewPort, reachedBottomOfView);

            } else if (e.getEventType() == MouseEvent.DRAG_DETECTED){
                // System.out.println("mouse was dragged ******************************************************** setting drag to true");
                // System.out.println("drag val (rounded to nearest hundred )= " + vScroll.getValue() + " ( " + roundScrollBar(vScroll.getValue()) + " ) ");
                useDrag = true;
            }
        });

        //Remove event handler
        //canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        root.getChildren().addAll(scrollPane);
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
                    System.out.println("clicked pipe: " + pipe);
                }
            }
        }
    }

    public void newLevel(Stage theStage, Canvas canvas, GraphicsContext ctx, int numberOfPipes, Boolean skipped, int oldNumRows) {
        //may need oldNumRows later, to ensure a new row gets added to the grid
        double height = canvas.getHeight();
        double width = canvas.getWidth();

        ctx.clearRect(0, 0, width, (height + 100));

        int numRows = (int) height / 100;
        int numColumns = (int) width / 100;

        if (!skipped) {
            //> 10 rows would be a good number to begin actual scrolling (maximises canvas size on current monitor setup)
            //increment max scroll bar by 100 (for additional row of pipes)
            vScroll.setMax(vScroll.getMax() + 100);
            // if second level with 1 additional row then set the visible amount to be half of max scrollable value
            if (numRows == 8){
                vScroll.setVisibleAmount(50);
            } else if (numRows > 8) {
                //always leave a consistent 100 block scroll bar value (representing current row in level)
                vScroll.setVisibleAmount(100);
            }
            System.out.println("viewport max  = " + vScroll.getMax());
            //if the level was not skipped, then 
            numRows = grid.numRows + 1;
        }
        numberOfPipes = ((numColumns - 2) * (numRows - 2)) + 2;
        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(2);
        System.out.println("numColumns " + numColumns);
        System.out.println("numRows " + numRows);
        System.out.println("numPipes" + numberOfPipes);
        grid = new Grid(numColumns, numRows);
        //resetting grid takes a long time, faster to run the constructor again
        //grid.reset(numColumns, numRows);
        grid.drawBorders(ctx);
        grid.draw(ctx);
        grid.drawBoxes(ctx);
		System.out.println("Loading next level : " + level);
        grid.displayText(ctx, level);
        System.out.println("------------------------");
        graph.reset(grid, numberOfPipes);
    }

    // rounds the scroll bar value to the nearest 100
    public int roundScrollBar(double scrollbarValue){
        return (int) (Math.round(scrollbarValue/100)*100);
    }
}

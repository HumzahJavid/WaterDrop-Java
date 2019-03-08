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
        //Creating the mouse event handler 
        EventHandler<javafx.scene.input.MouseEvent> eventHandler
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                int direction;
                //if left click, rotate clockwise
				if (mouseEvent.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                    direction = 1;
                    
                    newLevel(theStage, canvas, ctx, numberOfPipes, false);
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

        /* not working at all https://openjfx.io/javadoc/11/javafx.graphics/javafx/scene/input/ScrollEvent.html
        EventHandler<javafx.scene.input.ScrollEvent> scrollingEventHandler
                = new EventHandler<javafx.scene.input.ScrollEvent>() {
            @Override
            public void handle(javafx.scene.input.ScrollEvent scrollEvent) {
                System.out.println("Scrolledevent captured");
            }
                };*/


        // 3 events:
        /*

        click to get initial true position of scroll bar (set to old value)
        drag (to set a drag variable to true )
        release (checks drag variable, if true, then get new position of scroll bar (set to new value))

*/

        //Creating the mouse event handler for scroll bar 

        EventHandler<javafx.scene.input.MouseEvent> scrollBarClick
        = new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                System.out.println("start --------------------------------------");
                
                oldScrollValue = vScroll.getValue(); 
                System.out.println("TRUE start drag value " + oldScrollValue);
            }
        };

        EventHandler<javafx.scene.input.MouseEvent> scrollBarScroll
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                System.out.println("setting drag to true ");
                mouseEvent.setDragDetect(true);
            }
        };

        EventHandler<javafx.scene.input.MouseEvent> scrollBarRelease
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
            
                System.out.println("was mouse dragged " + mouseEvent.isDragDetect());

                System.out.println("this is the current stats:\n");
                newScrollValue = vScroll.getValue();
                
                System.out.println("it was currently RELEASAED at value " + newScrollValue);
                System.out.println("the old value " + oldScrollValue);
                System.out.println("the difference is " + (newScrollValue - oldScrollValue) + " \n setting drag to false... update view port, edit scrolling length to match one one row (increase in increments of numRows)");
                mouseEvent.setDragDetect(false);
                System.out.println("------------------end-----------------------");
            }
        };


        

        //Adding the event handler 
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        //vScroll.addEventHandler(javafx.scene.input.ScrollEvent.ANY, scrollingEventHandler);
        // vScroll.addEventHandler(MouseEvent.ANY, scrollingEventHandler);

        //fix this to check the distance scrolled,

        // check distance in scales of 100



        vScroll.addEventHandler(MouseEvent.ANY, e-> {
            // System.out.println("event = " + e);
            System.out.println("event = type " + e.getEventType());
            if (e.getEventType() == MouseEvent.MOUSE_CLICKED){
                System.out.println("start --------------------------------------");
                
                // oldScrollValue = vScroll.getValue(); 
                // System.out.println("TRUE start drag value " + oldScrollValue);
                // System.out.println("drag? initiate click " + e.isDragDetect());
                
            } else if (e.getEventType() == MouseEvent.MOUSE_RELEASED){
                // System.out.println("this is the current stats:\n");
                newScrollValue = vScroll.getValue();
                if (useDrag){
                    System.out.println(" the drag var was set to true = " + vScroll.getValue() + " -> rounded: " + roundScrollBar(vScroll.getValue()));
                    vScroll.setValue(roundScrollBar(vScroll.getValue()));
                }

                
                System.out.println("dragged? " + useDrag);
                System.out.println("it was currently RELEASAED at value " + newScrollValue);
                System.out.println("the old value was " + oldScrollValue);
                System.out.println("the difference is " + (newScrollValue - oldScrollValue) + " \n setting drag to false... update view port, edit scrolling length to match one one row (increase in increments of numRows)");
                System.out.println("------------------end-----------------------");

                //whether or not the mouse was dragged, set it to false (for next drag detection loop)
                useDrag = false;

            } else if (e.getEventType() == MouseEvent.DRAG_DETECTED){
                // System.out.println("mouse was dragged ******************************************************** setting drag to true");
                // System.out.println("drag val (rounded to nearest hundred )= " + vScroll.getValue() + " ( " + roundScrollBar(vScroll.getValue()) + " ) ");
                useDrag = true;
            }
        });
        //vScroll.addEventHandler(MouseEvent.MOUSE_PRESSED, scrollBarClick);
        //vScroll.addEventHandler(MouseEvent.DRAG_DETECTED, scrollBarScroll);
        //vScroll.addEventHandler(MouseEvent.MOUSE_RELEASED, scrollBarRelease);

        //Remove event handler
        //canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        //root.getChildren().addAll(canvas);
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
                }
            }
        }
    }

    public void newLevel(Stage theStage, Canvas canvas, GraphicsContext ctx, int numberOfPipes, Boolean skipped) {
        if (!skipped) {
            //increment canvas size by 100, to add a new row
            // * note: why is this 200?
            canvas.setHeight(canvas.getHeight() + 100);
            //canvas.setWidth(canvas.getWidth() + 100);
            vScroll.setMax(vScroll.getMax() + 100);
            vScroll.setVisibleAmount(100);
            //vScroll.setVisibleAmount(vScroll.getVisibleAmount() + 100);
        } else{
            System.out.println("Skipped the level");
        }
        double height = canvas.getHeight();
        double width = canvas.getWidth();

        ctx.clearRect(0, 0, width, (height + 100));

        int numRows = (int) height / 100;
        int numColumns = (int) width / 100;
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

package waterdrop;

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

public class WaterDrop extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage theStage) {
        Group root = new Group();
        Scene theScene = new Scene(root);

        theStage.setTitle("WaterDrop prototype");
        theStage.setScene(theScene);

        Canvas canvas = new Canvas(1400, 700);
        GraphicsContext ctx = canvas.getGraphicsContext2D();
        double height = ctx.getCanvas().getHeight();
        double width = ctx.getCanvas().getWidth();

        int numRows = (int) height / 100;
        int numColumns = (int) width / 100;

        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(2);

        Grid grid = new Grid(numColumns, numRows);
        grid.drawBorders(ctx);
        grid.draw(ctx);
        grid.drawBoxes(ctx);
        SinglyList list = new SinglyList();
        list.add(grid.pipeStart);

        //Creating the mouse event handler 
        EventHandler<javafx.scene.input.MouseEvent> eventHandler
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                rotatePipe(mouseEvent.getSceneX(), mouseEvent.getSceneY(), grid, list, ctx);
            }
        };

        //Adding the event handler 
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

        //Remove event handler
        //canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        root.getChildren().addAll(canvas);
        theStage.show();
    }

    public void rotatePipe(double x, double y, Grid grid, SinglyList list, GraphicsContext ctx) {
        //loops through grid to find and rotate the pipe that was clicked
        boolean newConnection;
        List<List<Pipe>> pipeGrid = grid.grid;

        for (List<Pipe> pipeList : pipeGrid) {
            for (Pipe pipe : pipeList) {
                if (pipe == null) {
                    //used to ignore any grid spaces which do not have pipe assigned
                } else if (((x >= pipe.leftEdge) && (x <= pipe.rightEdge)) && ((y >= pipe.topEdge) && (y <= pipe.bottomEdge))) {
                    //System.out.println("(x,y) = (" + pipeGrid.indexOf(pipeList) + "," + pipeList.indexOf(pipe) + ")");
                    pipe.rotate(ctx, 1);
                   // int[] ref = grid.getGridReference(pipe);
                   // pipe.checkConnections(grid, list);
                    pipe.checkPipeCanBeAddedToList(grid, list);
                    //check for new connection here 
                    //newConnection = checkForConnection
                    //if newConnection list.addPipe
                }
            }
        }
    }
}

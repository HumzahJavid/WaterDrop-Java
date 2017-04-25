package waterdrop;

/**
 *
 * @author humzah
 */
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;

import javafx.stage.Stage;

import javafx.scene.Scene;

import javafx.scene.Group;

import javafx.scene.canvas.Canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import javafx.scene.paint.Color;

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
        /*canvas.setHeight(300);
        canvas.setWidth(300);
        ScrollBar vScroll = createScrollBar(Orientation.VERTICAL, 10000, 300);
        ScrollBar hScroll = createScrollBar(Orientation.HORIZONTAL, 10000, 300);
        GridPane scrollPane = new GridPane();
        scrollPane.addColumn(0, canvas, hScroll);
        scrollPane.add(vScroll, 1, 0);*/
        Color backgroundColour = Color.WHITE;
        Color pipeColour = Color.BLUE;

        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(2);

        Grid grid = new Grid(numColumns, numRows);
        System.out.println("grid num columns "+ grid.numColumns);
        System.out.println("grid num rows " + grid.numRows);
        grid.draw(ctx);
        grid.drawBorders(ctx);
        grid.drawBoxes(ctx);

        //Creating the mouse event handler 
        EventHandler<javafx.scene.input.MouseEvent> eventHandler
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                //System.out.println("Mouse event handler v3 (create then add created handler)");
                System.out.println("getSceneX " + mouseEvent.getSceneX());
                System.out.println("getSceneY " + mouseEvent.getSceneY());
                checkRotation(mouseEvent.getSceneX(), mouseEvent.getSceneY(), grid.grid, ctx);
            }
        };

        //Adding the event handler 
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

        //Remove event handler
        //canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        root.getChildren().addAll(canvas);
        theStage.show();
    }

    public void checkRotation(double x, double y, List<List<Pipe>> pipeGrid, GraphicsContext ctx) {
        for (List<Pipe> pipeList : pipeGrid) {
            for (Pipe pipe : pipeList) {
                if ((x >= pipe.leftEdge) && (x <= pipe.rightEdge)) {

                    System.out.println("left, right " + pipe.leftEdge + " " + pipe.rightEdge);
                    System.out.println("you are in pipes x range");
                    pipe.rotate(ctx, 1);
                }
            }
        }
    }

    public static ScrollBar createScrollBar(Orientation orientation, double fullSize, double canvasSize) {
        ScrollBar sb = new ScrollBar();
        sb.setOrientation(orientation);
        sb.setMax(fullSize - canvasSize);
        sb.setVisibleAmount(canvasSize);
        return sb;
    }

    public void pipeDemoDeepCopy() {
        List<Block> pipeABlock = new ArrayList<Block>();
        pipeABlock.add(new Block(0, 30, 100, 40, 30, 0, 40, 100, 0, 30, 100, 40, 30, 0, 40, 100));
        Pipe pipeA = new Pipe(pipeABlock);

        List<Block> pipeBBlock = new ArrayList<Block>();
        pipeBBlock.add(new Block(30, 0, 40, 70, 30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40));
        pipeBBlock.add(new Block(30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40, 30, 0, 40, 70));
        Pipe pipeB = new Pipe(pipeBBlock);

        System.out.println("pipe A = ");
        System.out.println(pipeA);
        Pipe pipeaclone = pipeA.deepClone();
        System.out.println("pipeA clone" + pipeaclone);
        System.out.println("***************************************************");
        pipeA.bottomEdge = 9999;
        System.out.println("pipeA = " + pipeA);

        System.out.println("pipeA clone" + pipeaclone);

        pipeaclone.blocks.get(0).orientation.changePositions(3, 3);

        pipeaclone.updatesEdges();
        pipeaclone.blocks.get(0).update();
        System.out.println("pipeAclone at 3 3");
        System.out.println(pipeaclone);

    }

}

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
        /*canvas.setHeight(300);
canvas.setWidth(300);
         */
 /*ScrollBar vScroll = createScrollBar(Orientation.VERTICAL, 10000, 300);

        ScrollBar hScroll = createScrollBar(Orientation.HORIZONTAL, 10000, 300);

        GridPane scrollPane = new GridPane();
        scrollPane.addColumn(0, canvas, hScroll);
        scrollPane.add(vScroll, 1, 0);*/

        Color backgroundColour = Color.WHITE;
        Color pipeColour = Color.BLUE;

        GraphicsContext ctx = canvas.getGraphicsContext2D();

        ctx.setStroke(Color.BLACK);
        ctx.setLineWidth(2);

        drawBorders(ctx);
        List<Block> pipeABlock = new ArrayList<Block>();
        pipeABlock.add(new Block(0, 30, 100, 40, 30, 0, 40, 100, 0, 30, 100, 40, 30, 0, 40, 100));
        Pipe pipeA = new Pipe(pipeABlock);

        List<Block> pipeBBlock = new ArrayList<Block>();
        pipeBBlock.add(new Block(30, 0, 40, 70, 30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40));
        pipeBBlock.add(new Block(30, 30, 70, 40, 30, 30, 40, 70, 0, 30, 70, 40, 30, 0, 40, 70));
        Pipe pipeB = new Pipe(pipeBBlock);
       // pipeA.draw(ctx, pipeColour);
        //pipeB.draw(ctx, pipeColour);

        /*Pipe pipe3 = new Pipe(30, 0, 40, 70, 30, 30, 70, 40);
        System.out.println("PIPE3 block1");
        pipe3.blocks.get(0).orientation.printVals();
        System.out.println("PIPE3 block2");
        pipe3.blocks.get(1).orientation.printVals();
         */
        //Creating the mouse event handler 
        EventHandler<javafx.scene.input.MouseEvent> eventHandler
                = new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                //System.out.println("Mouse event handler v3 (create then add created handler)");
                System.out.println("getSceneX " + mouseEvent.getSceneX());
                System.out.println("getSceneY " + mouseEvent.getSceneY());
                tester(mouseEvent.getSceneX(), mouseEvent.getSceneY(), pipeA, pipeB, ctx);
                //pass grid here
            }
        };
        //Adding the event handler 
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);

        //Remove event handler
        //canvas.removeEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        root.getChildren().addAll(canvas);
        theStage.show();

    }

    public void drawBorders(GraphicsContext ctx) {
        ctx.setFill(Color.BLACK);
        double height = ctx.getCanvas().getHeight();
        double width = ctx.getCanvas().getWidth();
        int numRows = (int) height / 100;
        int numColumns = (int) width / 100;
        System.out.println("num Columns = " + numColumns);
        System.out.println("numRows " + numRows);
        Grid grid = new Grid(numColumns, numRows);
        //grid.draw(ctx);
        System.out.println(grid);
        for (int i = 0; i < numColumns; i += (numColumns - 1)) {
            for (int j = 0; j < numRows; j++) {
                ctx.fillRect((i * 100), (j * 100), 100, 100);
            }
        }
    }

    public void tester(double x, double y, Pipe pipe, Pipe pipe3, GraphicsContext ctx) {
        System.out.println("left, right " + pipe.leftEdge + " " + pipe.rightEdge);

        if ((x >= pipe.leftEdge) && (x <= pipe.rightEdge)) {
            System.out.println("you are in pipeA x range");
            pipe.rotate(ctx, 1);
        }

        if ((x >= pipe3.leftEdge) && (x <= pipe3.rightEdge)) {
            System.out.println("you are in pipeB x range");
            pipe3.rotate(ctx, 1);
        }
    }

    public static ScrollBar createScrollBar(Orientation orientation, double fullSize, double canvasSize) {
        ScrollBar sb = new ScrollBar();
        sb.setOrientation(orientation);
        sb.setMax(fullSize - canvasSize);
        sb.setVisibleAmount(canvasSize);
        return sb;
    }

}

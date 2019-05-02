
/**
 *
 * @author humzah
 */

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<List<Pipe>> vertices;
    private int numRows;
    private int[][] adjMatrix;
    private int numberOfPipes;
    private Grid grid;
    private boolean pathToGoalPipe = false;

    public Graph(List<List<Pipe>> pipes) {
        this.vertices = pipes;
        this.populateAdjacencyMatrix();
        System.out.println("pipe sizes " + pipes.size() + " " + pipes.get(2).size());
    }

    public Graph(Grid grid, int numberOfPipes) {
        this.grid = grid;
        this.vertices = grid.getGrid();
        this.numRows = grid.numRows;
        this.numberOfPipes = numberOfPipes;
        this.populateAdjacencyMatrix();
    }

    // this recreates the matrix every pipe rotation (inefficient)
    public void calculateMatrix() {
        this.resetMatrix();
        this.populateAdjacencyMatrix();
        this.dfs();
    }

    // parent depth first search function
    public void dfs() {
        // similar to this.adjMatrix, but keeps track of visited edges
        int[][] visitedEdges = new int[numberOfPipes][numberOfPipes];
        // reset to false to prevent false level completion
        this.pathToGoalPipe = false;
        dfs(0, visitedEdges);
    }

    // actual depth first search function
    public void dfs(int numberPipeFrom, int[][] visitedEdges) {
        int[] pipeConnectionsTo = adjMatrix[numberPipeFrom];
        // for each potential edge
        for (int i = 0; i < pipeConnectionsTo.length; i++) {
            // there is an edge
            if (pipeConnectionsTo[i] == 1) {
                // if edge is connecting to goal pipe
                if (i == (numberOfPipes - 1)) {
                    System.out.println("GOAL PIPE REACHED");
                    this.pathToGoalPipe = true;
                } else {
                    // if the edge has not been visited
                    if (visitedEdges[numberPipeFrom][i] == 0) {
                        // System.out.println("visiting edge: pipe #" + numberPipeFrom + " is connected to " + i);
                        // update state of visited edges
                        visitedEdges[numberPipeFrom][i] = 1;
                        visitedEdges[i][numberPipeFrom] = 1;
                        // recursive call with updated state
                        dfs(i, visitedEdges);
                    }
                }
            }
        }
    }

    // may have potential use later
    // converts (1, 2) to #13
    public int gridRefToPipeNumber(int[] gridRef) {
        int x = gridRef[0];
        int y = gridRef[1];
        return ((x + 1) + ((y - 1) * 12));
    }

    // checks if pipeA and pipeB are connected
    public boolean isConnected(Pipe pipeA, Pipe pipeB) {
        boolean connected = false;
        int pipeAX = pipeA.getGridX();
        int pipeAY = pipeA.getGridY();

        int pipeBX = pipeB.getGridX();
        int pipeBY = pipeB.getGridY();
        boolean inSameColumn = (pipeAX == pipeBX);
        boolean inSameRow = (pipeAY == pipeBY);
        if ((pipeA.leftEdge == pipeB.rightEdge) && inSameRow) {
            // System.out.println("Connected to left : " + pipeA.leftEdge);
            connected = true;
        }

        if ((pipeA.rightEdge == pipeB.leftEdge) && inSameRow) {
            // System.out.println("Connected to right: " + pipeA.rightEdge);
            connected = true;
        }

        if ((pipeA.topEdge == pipeB.bottomEdge) && inSameColumn) {
            // System.out.println("Connected to top: " + pipeA.topEdge);
            connected = true;
        }

        if ((pipeA.bottomEdge == pipeB.topEdge) && inSameColumn) {
            // System.out.println("Connected to bottom: " + pipeA.bottomEdge);
            connected = true;
        }
        return connected;
    }

    public boolean isLevelFinished() {
        return pathToGoalPipe;
    }

    // converts #13 to (1, 2)
    public int[] pipeNumberToGridRef(int pipeNumber) {
        int x = pipeNumber % 12;
        int y = (pipeNumber / 12) + 1;
        // START and END pipes dont fit the above formula
        if (pipeNumber == 0) {
            x = grid.randomStart;
            y = 0;
        } else if (pipeNumber == numberOfPipes - 1) {
            x = grid.randomEnd;
            y = numRows - 1;
        } else {
            // the rightmost pipes, using the forumla above
            // produces a grid reference with the following offset (x-12, y+1)
            if (x == 0) {
                x = 12;
                y = y - 1;
            }
        }
        int[] reference = { x, y };
        return reference;
    }

    //
    public void populateAdjacencyMatrix() {
        this.adjMatrix = new int[numberOfPipes][numberOfPipes];
        Pipe pipeA;
        Pipe pipeB;
        for (int i = 0; i < numberOfPipes; i++) {
            int[] gridRefA = pipeNumberToGridRef(i);
            pipeA = vertices.get(gridRefA[0]).get(gridRefA[1]);
            for (int j = 0; j < numberOfPipes; j++) {
                int[] gridRefB = pipeNumberToGridRef(j);
                pipeB = vertices.get(gridRefB[0]).get(gridRefB[1]);
                if (isConnected(pipeA, pipeB)) {
                    adjMatrix[i][j] = 1;
                }
            }
        }
    }

    public void print() {
        System.out.println("printing adj matrix");
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix.length; j++) {
                System.out.print(adjMatrix[i][j] + " ");
            }
            System.out.println("");
        }
    }

    //resets the variables of the class
    public void reset(Grid grid, int numberOfPipes){
        this.grid = grid;
        this.vertices = grid.getGrid();
        this.numRows = grid.numRows;
        this.numberOfPipes = numberOfPipes;
        this.populateAdjacencyMatrix();
        this.pathToGoalPipe = false;
    }

    public void resetMatrix() {
        for (int i = 0; i < adjMatrix.length; i++) {
            for (int j = 0; j < adjMatrix.length; j++) {
                adjMatrix[i][j] = 0;
            }
        }
    }

    public void setVertices(List<List<Pipe>> gridOfPipes) {
        this.vertices = gridOfPipes;
    }
}

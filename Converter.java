import java.lang.System.Logger.Level;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.rits.cloning.*;
import java.util.List;
import java.util.ArrayList;

public class Converter {

    String level = "[[{\"pipeType\":\"pipeBorder\",\"values\":[0,0,100,100]},{\"pipeType\":\"pipeBorder\",\"values\":[0,100,100,100]},{\"pipeType\":\"pipeBorder\",\"values\":[0,200,100,100]},{\"pipeType\":\"pipeBorder\",\"values\":[0,300,100,100]}],[{\"pipeType\":\"DEFAULT\"},{\"pipeType\":\"pipeA\",\"values\":[100,130,100,40],\"leftEdge\":100,\"rightEdge\":200,\"topEdge\":130,\"bottomEdge\":170,\"connected\":false},{\"pipeType\":\"pipeB\",\"values\":[[130,200,40,70],[130,230,70,40]],\"leftEdge\":130,\"rightEdge\":200,\"topEdge\":200,\"bottomEdge\":270,\"connected\":false},{\"pipeType\":\"DEFAULT\"}],[{\"pipeType\":\"DEFAULT\"},{\"pipeType\":\"pipeB\",\"values\":[[230,100,40,70],[230,130,70,40]],\"leftEdge\":230,\"rightEdge\":300,\"topEdge\":100,\"bottomEdge\":170,\"connected\":false},{\"pipeType\":\"pipeA\",\"values\":[200,230,100,40],\"leftEdge\":200,\"rightEdge\":300,\"topEdge\":230,\"bottomEdge\":270,\"connected\":false},{\"pipeType\":\"DEFAULT\"}],[{\"pipeType\":\"DEFAULT\"},{\"pipeType\":\"pipeA\",\"values\":[300,130,100,40],\"leftEdge\":300,\"rightEdge\":400,\"topEdge\":130,\"bottomEdge\":170,\"connected\":false},{\"pipeType\":\"pipeB\",\"values\":[[330,200,40,70],[330,230,70,40]],\"leftEdge\":330,\"rightEdge\":400,\"topEdge\":200,\"bottomEdge\":270,\"connected\":false},{\"pipeType\":\"DEFAULT\"}],[{\"pipeType\":\"DEFAULT\"},{\"pipeType\":\"pipeB\",\"values\":[[430,100,40,70],[430,130,70,40]],\"leftEdge\":430,\"rightEdge\":500,\"topEdge\":100,\"bottomEdge\":170,\"connected\":false},{\"pipeType\":\"pipeB\",\"values\":[[430,200,40,70],[430,230,70,40]],\"leftEdge\":430,\"rightEdge\":500,\"topEdge\":200,\"bottomEdge\":270,\"connected\":false},{\"pipeType\":\"DEFAULT\"}],[{\"pipeType\":\"DEFAULT\"},{\"pipeType\":\"pipeB\",\"values\":[[530,100,40,70],[530,130,70,40]],\"leftEdge\":530,\"rightEdge\":600,\"topEdge\":100,\"bottomEdge\":170,\"connected\":false},{\"pipeType\":\"pipeB\",\"values\":[[530,200,40,70],[530,230,70,40]],\"leftEdge\":530,\"rightEdge\":600,\"topEdge\":200,\"bottomEdge\":270,\"connected\":false},{\"pipeType\":\"DEFAULT\"}],[{\"pipeType\":\"DEFAULT\"},{\"pipeType\":\"pipeA\",\"values\":[600,130,100,40],\"leftEdge\":600,\"rightEdge\":700,\"topEdge\":130,\"bottomEdge\":170,\"connected\":false},{\"pipeType\":\"pipeB\",\"values\":[[630,200,40,70],[630,230,70,40]],\"leftEdge\":630,\"rightEdge\":700,\"topEdge\":200,\"bottomEdge\":270,\"connected\":false},{\"pipeType\":\"DEFAULT\"}],[{\"pipeType\":\"pipeStart\",\"values\":[730,0,40,100],\"leftEdge\":730,\"rightEdge\":770,\"topEdge\":0,\"bottomEdge\":100,\"connected\":false},{\"pipeType\":\"pipeA\",\"values\":[700,130,100,40],\"leftEdge\":700,\"rightEdge\":800,\"topEdge\":130,\"bottomEdge\":170,\"connected\":false},{\"pipeType\":\"pipeB\",\"values\":[[730,200,40,70],[730,230,70,40]],\"leftEdge\":730,\"rightEdge\":800,\"topEdge\":200,\"bottomEdge\":270,\"connected\":false},{\"pipeType\":\"DEFAULT\"}],[{\"pipeType\":\"DEFAULT\"},{\"pipeType\":\"pipeB\",\"values\":[[830,100,40,70],[830,130,70,40]],\"leftEdge\":830,\"rightEdge\":900,\"topEdge\":100,\"bottomEdge\":170,\"connected\":false},{\"pipeType\":\"pipeB\",\"values\":[[830,200,40,70],[830,230,70,40]],\"leftEdge\":830,\"rightEdge\":900,\"topEdge\":200,\"bottomEdge\":270,\"connected\":false},{\"pipeType\":\"pipeEnd\",\"values\":[830,300,40,100],\"leftEdge\":830,\"rightEdge\":870,\"topEdge\":300,\"bottomEdge\":400,\"connected\":false}],[{\"pipeType\":\"pipeBorder\",\"values\":[900,0,100,100]},{\"pipeType\":\"pipeBorder\",\"values\":[900,100,100,100]},{\"pipeType\":\"pipeBorder\",\"values\":[900,200,100,100]},{\"pipeType\":\"pipeBorder\",\"values\":[900,300,100,100]}]]";
    Object obj = JSONValue.parse(level);
    JSONArray array = (JSONArray) obj;
    JSONParser parser = new JSONParser();

    String[][] pipeTypeGrid;
    int numColumns = 0;
    int numRows = 0;
    Grid grid;

    public Converter() {
        this.numColumns = this.array.size();
        this.numRows = ((JSONArray) this.array.get(0)).size();
        generatePipeTypeGrid(this.array);
    }

    public Converter(String newLevel) {
        this.level = newLevel;
        this.obj = JSONValue.parse(level);
    }

    public int[] getGridSize() {
        int[] reference = { numColumns, numRows };
        return reference;
    }

    public String[][] getPipeTypeGrid() {
        return this.pipeTypeGrid;
    }

    public void generatePipeTypeGrid(JSONArray array) {
        String[][] pipeTypeGrid = new String[numColumns][numRows];
        for (int i = 0; i < this.numColumns; i++) {
            /*
             * System.out.println(this.array.get(i).getClass()); System.out.println("---");
             */
            JSONArray currentRow = (JSONArray) this.array.get(i);
            for (int j = 0; j < this.numRows; j++) {
                // System.out.println("the row " + currentRow.get(j).getClass());
                JSONObject pipeObject = (JSONObject) currentRow.get(j);
                // System.out.println("the pipe" + pipeObject);
                String pipeType = pipeObject.get("pipeType").toString();
                pipeTypeGrid[i][j] = pipeType;
            }
        }
        this.pipeTypeGrid = pipeTypeGrid;
    }

    public void exploreLevel() {
        for (int i = 0; i < this.numColumns; i++) {
            System.out.println(this.array.get(i).getClass());
            System.out.println("---");
            JSONArray currentRow = (JSONArray) this.array.get(i);
            for (int j = 0; j < this.numRows; j++) {
                System.out.println("the row " + currentRow.get(j).getClass());
                JSONObject pipeObject = (JSONObject) currentRow.get(j);
                System.out.println("the pipe" + pipeObject);
                String pipeType = pipeObject.get("pipeType").toString();
                System.out.println("type = " + pipeType);
            }
        }
    }

    public void print() {
        System.out.println("the array size for level is " + this.array.size());
        String string = "";
        for (int i = 0; i < this.array.size(); i++) {
            string += "\nColumn " + i + ":\n";
            string += this.array.get(i).toString();
        }
        System.out.println(string);
    }

    public String toString() {
        return obj.toString();
    }

}

// https://www.tutorialspoint.com/json_simple/json_simple_quick_guide.htm
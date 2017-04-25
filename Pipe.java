package waterdrop;

/**
 *
 * @author humzah
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import waterdrop.Block;

import java.io.Serializable;

public class Pipe implements Cloneable, Serializable {

    int leftEdge;
    int rightEdge;
    int topEdge;
    int bottomEdge;
    List<Block> blocks;
    int orientation;

    /*Pipe(int x, int y, int w, int h) {
        this.blocks = new ArrayList<Block>();
        Block block = new Block(x, y, w, h);
        this.blocks.add(block);

        this.leftEdge = x;
        this.rightEdge = (x + w);
        this.topEdge = y;
        this.bottomEdge = (y + h);
        this.orientation = 1;
    }

    Pipe(int x, int y, int w, int h, int x2, int y2, int w2, int h2) {
        this.blocks = new ArrayList<Block>();
        Block block = new Block(x, y, w, h);
        Block block2 = new Block(x2, y2, w2, h2);
        this.blocks.add(block);
        this.blocks.add(block2);

        if (x < x2) {
            this.leftEdge = x;
        } else {
            this.leftEdge = x2;
        }

        if ((x + w) > (x + w2)) {
            this.rightEdge = (x + w);
        } else {
            this.rightEdge = (x2 + w2);
        }

        if (y < y2) {
            this.topEdge = y;
        } else {
            this.topEdge = y2;
        }

        if ((y + h) > (y2 + h2)) {
            this.bottomEdge = (y + h);
        } else {
            this.bottomEdge = (y + h2);
        }

        this.orientation = 1;
    }*/
    Pipe(List<Block> blocks) {
        this.blocks = blocks;
        this.updatesEdges();
        this.orientation = 1;
    }

    Pipe(Block block) {
        int x = block.x;
        int y = block.y;
        int w = block.width;
        int h = block.height;
        this.blocks = new ArrayList<Block>();
        blocks.add(block);

        this.leftEdge = x;
        this.rightEdge = (x + w);
        this.topEdge = y;
        this.bottomEdge = (y + h);

    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    public void setBlocks(List<Block> newBlocks) {
        this.blocks = newBlocks;
    }

    public void addBlock(Block block) {
        this.blocks.add(block);
    }

    public void draw(GraphicsContext ctx, Color colour) {
        ctx.setFill(colour);
        for (Block block : this.getBlocks()) {
            block.draw(ctx);
        }
    }

    public void updatesEdges() {
        int[] main = this.blocks.get(0).orientation.currentOrientation;
        this.leftEdge = main[0];
        this.rightEdge = main[0] + main[2];
        this.topEdge = main[1];
        this.bottomEdge = main[1] + main[3];
        for (Block block : this.blocks) {
            int[] next = block.orientation.currentOrientation;

            if (next[0] < this.leftEdge) {
                this.leftEdge = next[0];
            } else {
                this.leftEdge = main[0];
            }

            if ((next[0] + next[2]) > this.rightEdge) {
                this.rightEdge = next[0] + next[2];
            } else {
                this.rightEdge = main[0] + main[2];
            }

            if (next[1] < this.topEdge) {
                this.topEdge = next[1];
            } else {
                this.topEdge = main[1];
            }

            if ((next[1] + next[3]) > this.topEdge) {
                this.bottomEdge = next[1] + next[3];
            } else {
                this.bottomEdge = main[1] + main[3];
            }
            block.update();
        }
    }

    public void rotate(GraphicsContext ctx, int direction) {
        for (Block block : this.blocks) {
            //erase oldshape
            this.draw(ctx, Color.WHITE);

            switch (direction) {
                case 1: {
                    if (this.orientation == 4) {
                        this.orientation = 1;
                        block.rotate();
                    } else {
                        this.orientation += direction;
                        block.rotate();
                    }
                }
                break;
                case -1: {
                    if (this.orientation == 1) {
                        this.orientation = 4;
                        block.rotate();
                    } else {
                        this.orientation += direction;
                        block.rotate();
                    }
                }
            }
            this.updatesEdges();
            //draw new shape
            this.draw(ctx, Color.BLUE);
        }
    }

    public Pipe clone() {
        try {
            return (Pipe) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public Pipe deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Pipe) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        String pipeString = "";
        for (Block block : this.blocks) {
            pipeString += block.toString() + "\n....pipe orientation : " + this.orientation + " l, r, t, b " + this.leftEdge + " " + this.rightEdge + " " + this.topEdge + " " + this.bottomEdge;
            pipeString += "\n";
        }
        String pipeString2 = "[]";

        return pipeString;
    }
}

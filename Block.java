package waterdrop;

/**
 *
 * @author humzah
 */
import javafx.scene.canvas.GraphicsContext;
//old cloning library imports requirements
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Block implements Cloneable, Serializable {

    int x;
    int y;
    int width;
    int height;
    Orientation orientation;

    Block(int x, int y, int w, int h, int x2, int y2, int w2, int h2, int x3, int y3, int w3, int h3, int x4, int y4, int w4, int h4) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.orientation = new Orientation(new int[]{x, y, w, h, x2, y2, w2, h2, x3, y3, w3, h3, x4, y4, w4, h4});
        this.update();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

    public void setWidth(int newWidth) {
        this.width = newWidth;
    }

    public void setHeight(int newHeight) {
        this.height = newHeight;
    }

    public void draw(GraphicsContext ctx) {
        ctx.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public void rotate() {
        this.orientation.currentOrientation = this.orientation.next();
        this.update();
    }

    public void update() {
        this.x = this.orientation.currentOrientation[0];
        this.y = this.orientation.currentOrientation[1];
        this.width = this.orientation.currentOrientation[2];
        this.height = this.orientation.currentOrientation[3];
    }

    public Block clone() {
        try {
            return (Block) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public Block deepClone() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (Block) ois.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "X : " + this.x + " Y: " + this.y + " Width: " + this.width + " Height" + this.height + " orientation" + this.orientation;
    }

}

import java.awt.*;
import java.util.Random;

public class ColouredRectangle extends Rectangle implements ColoredShape {
    private final Color color;
    private Player owner;

    public ColouredRectangle(int x, int y, int width, int height) {
        this(x,y,width,height, Color.blue);
    }

    public ColouredRectangle(int x, int y, int width, int height, Color color) {
        super(x,y,width,height);
        this.color = color;
    }

    public ColouredRectangle(int x, int y, int width, int height, Player owner) {
        super(x,y,width,height);
        this.owner = owner;
        Random rnd = new Random();
        this.color = new Color(rnd.nextFloat(),rnd.nextFloat(), rnd.nextFloat());
    }


    @Override
    public Color getColor() {
        return color;
    }
}

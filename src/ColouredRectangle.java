import java.awt.*;

/**
 * Created by Lukas Normal on 20.06.2015.
 */
public class ColouredRectangle extends Rectangle implements ColoredShape {
    private final Color color;

    public ColouredRectangle(int x, int y, int width, int height) {
        this(x,y,width,height, Color.blue);
    }

    public ColouredRectangle(int x, int y, int width, int height, Color color) {
        super(x,y,width,height);
        this.color = color;
    }


    @Override
    public Color getColor() {
        return color;
    }
}

import java.awt.*;

/**
 * Created by Lukas Normal on 20.06.2015.
 */
public class ColouredRectangle extends Rectangle implements ColoredShape {
    private final Color color = Color.blue;

    public ColouredRectangle(int x, int y, int width, int height) {
        super(x,y,width,height);
    }


    @Override
    public Color getColor() {
        return color;
    }
}

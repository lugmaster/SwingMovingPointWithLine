import java.awt.*;

public class ColouredRectangle extends Rectangle.Float implements ColoredShape {
    private final Color color;

    public ColouredRectangle(float x, float y, float width, float height) {
        this(x,y,width,height, RandomColorGenerator.generateRandomColor());
    }

    public ColouredRectangle(float x, float y, float width, float height, Color color) {
        super(x,y,width,height);
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

}

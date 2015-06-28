import java.awt.*;

public class ColoredPolygon extends Polygon implements ColoredShape{
    private final Color color;

    public ColoredPolygon(int[] intsX, int[] intsY, Color color){
        super(intsX, intsY, intsX.length);
        this.color = color;
    }

    public ColoredPolygon(int[] intsX, int[] intsY){
        this(intsX, intsY, RandomColorGenerator.generateRandomColor());

    }

    @Override
    public Color getColor() {
        return color;
    }
}
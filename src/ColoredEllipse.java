import java.awt.*;
import java.awt.geom.Ellipse2D;

public class ColoredEllipse extends Ellipse2D.Float implements ColoredShape  {
    private final Color color;

    public ColoredEllipse(float x, float y, float width, float height, Color color){
        super(x, y, width, height);
        this.color = color;
    }

    public ColoredEllipse(float x, float y, float width, float height){
        this(x, y, width, height, RandomColorGenerator.generateRandomColor());
    }

    @Override
    public Color getColor(){
        return color;
    }

}

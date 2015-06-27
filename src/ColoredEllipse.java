import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class ColoredEllipse extends Ellipse2D.Float implements ColoredShape  {
    final Color color;

    public ColoredEllipse(float x, float y, float width, float height, Color color){
        super(x, y, width, height);
        this.color = color;
    }

    @Override
    public Color getColor(){
        return color;
    }




}

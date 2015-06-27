import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Path2D;

/**
 * Created by Lukas Normal on 27.06.2015.
 */
public class ColoredPath extends Path2D.Float implements ColoredShape {
    private Color color;

    public ColoredPath(Path2D.Float path2D){
        super(path2D);
        color = Color.blue;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
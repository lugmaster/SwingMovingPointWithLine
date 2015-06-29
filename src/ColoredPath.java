import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Lukas Normal on 29.06.2015.
 */
public class ColoredPath extends Path2D.Float implements ColoredShape {

    private ArrayList<Point2D.Float> points;
    private Color color;

    public ColoredPath(){}

    public ColoredPath(ColoredShape coloredShape){
        super(coloredShape);
        this.color = coloredShape.getColor();
    }

    public ColoredPath(Shape s){
        super(s);
        this.color = RandomColorGenerator.generateRandomColor();
    }

    public ColoredPath(ArrayList<Point2D.Float> points, Color color){
        for (int i = 0; i < points.size(); i++) {
            if(i == 0) this.moveTo(points.get(i).getX(), points.get(i).getY());
            else this.lineTo(points.get(i).getX(), points.get(i).getY());
        }
        this.closePath();
        this.color = color;
    }

    public ColoredPath(ArrayList<Point2D.Float> points){
        this(points, RandomColorGenerator.generateRandomColor());
    }




    @Override
    public Color getColor() {
        return color;
    }
}

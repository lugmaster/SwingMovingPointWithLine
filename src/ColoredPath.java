import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ColoredPath extends Path2D.Float implements ColoredShape {

    private ArrayList<Point2D.Float> points;
    private Color color;

    public ColoredPath(){
        super();
    }

    public ColoredPath(ColoredShape coloredShape){
        super(coloredShape);
        this.color = coloredShape.getColor();
    }

    public ColoredPath(Color color){
        super();
        this.color = color;
    }

    public ColoredPath(Shape s){
        super(s);
        this.color = RandomColorGenerator.generateRandomColor();
    }

    public ColoredPath(ArrayList<Point2D.Float> points, Color color, boolean closed){
        for (int i = 0; i < points.size(); i++) {
            if(i == 0) this.moveTo(points.get(i).getX(), points.get(i).getY());
            else this.lineTo(points.get(i).getX(), points.get(i).getY());
        }
        if(closed) this.closePath();
        this.color = color;
    }

    public ColoredPath(ArrayList<Point2D.Float> points, boolean closed){
        this(points, RandomColorGenerator.generateRandomColor(), closed);
    }

    public void setNewPath(ArrayList<Point2D.Float> newPoints){
        points.clear();
        for (int i = 0; i < newPoints.size(); i++) {
            if(i==0)this.moveTo(newPoints.get(i).getX(), newPoints.get(i).getY());
            else this.lineTo(newPoints.get(i).getX(), newPoints.get(i).getY());
            points.add(new Point2D.Float((float) points.get(i).getX(), (float) points.get(i).getY()));
        }
    }

    public void setNewPath(ArrayList<Point2D.Float> newPoints, Player player){
        setNewPath(newPoints);
        Point2D.Float p2d = new Point2D.Float((float) player.getPosition().getX(), (float) player.getPosition().getY());
        this.lineTo(p2d.getX(),p2d.getY());
        points.add(p2d);
    }


    @Override
    public Color getColor() {
        return color;
    }
}

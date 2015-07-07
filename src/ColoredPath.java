import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ColoredPath extends Path2D.Float implements ColoredShape {

    private ArrayList<Point> pathPoints;
    private Color color;

    public ColoredPath(Color color){
        super();
        this.color = color;
        this.pathPoints = new ArrayList<>();
    }

    public ColoredPath(Area a, Color color){
        super(a);
        this.color = color;
        this.pathPoints = new ArrayList<>();
    }

    public ColoredPath(ColoredPath path, boolean closed){
        this(path.pathPoints, path.color, closed);
    }

    public ColoredPath(ColoredPath path, Color color, boolean closed){
        this(path.pathPoints, color, closed);
    }

    public ColoredPath(ArrayList<Point> pathPoints, Color color, boolean closed){
        super();
        this.pathPoints = new ArrayList<>();
        setNewPath(pathPoints, closed);
        this.color = color;
    }

    public ArrayList<Point> getPathPoints() {
        return pathPoints;
    }

    public ColoredPath(ArrayList<Point> pathPoints, boolean closed){
        this(pathPoints, Color.YELLOW, closed);
    }

    public void setNewPath(ArrayList<Point> newPoints, boolean closed){
        this.reset();
        if(!pathPoints.isEmpty()) pathPoints.clear();
        if(newPoints.isEmpty()){
            moveTo(0,0);
            pathPoints.add(new Point(0, 0));
        }
        else{
            for (int i = 0; i < newPoints.size(); i++) {
                if(i==0)this.moveTo(newPoints.get(i).getX(), newPoints.get(i).getY());
                else this.lineTo(newPoints.get(i).getX(), newPoints.get(i).getY());
                pathPoints.add(new Point(newPoints.get(i)));
            }
        }
        if(closed)this.closePath();

    }

    @Override
    public Color getColor() {
        return color;
    }

    public String toString(){
        String s = "";
        for (Point pathPoint : pathPoints) {
            s += pathPoint + "\n";
        }
        return s;
    }
}

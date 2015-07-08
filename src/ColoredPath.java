import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/*
 * ColoredPath extends the Path2D.Float class.
 * It stores a path, which can be a line if the path is NOT closed or
 * it can be a shape if the path is closed.
 * The path coordinates, also referred as shape outline are stored as points
 * in an arrayList
 * The path can be created by an arrayList defining its path or its outline, by another path,
 * by an area (NOTE: no outline is stored by an area!), or it can be created
 * empty handed, only with a color(NOTE: as long as no path is set, nothing will be drawn!)
 * A ColoredPath can as well adopt a new shape or line by setting a new path.
 */

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

    public ColoredPath(ArrayList<Point> pathPoints, boolean closed){
        this(pathPoints, Color.YELLOW, closed);
    }

    public ArrayList<Point> getPathPoints() {
        return pathPoints;
    }

    /*
     * sets a new path by a given list of points.
     * @param closed    decides whether the path is closed or not
     */
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
}

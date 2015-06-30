import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ColoredPath extends Path2D.Float implements ColoredShape {

    private ArrayList<Point2D.Float> pathPoints;
    private Color color;

    public ColoredPath(Color color){
        super();
        this.color = color;
        this.pathPoints = new ArrayList<>();
    }

    public ColoredPath(Shape s){
        super(s);
        this.color = RandomColorGenerator.generateRandomColor();
        this.pathPoints = new ArrayList<>();
    }

    public ColoredPath(ArrayList<Point2D.Float> pathPoints, Color color, boolean closed){
        super();
        this.pathPoints = new ArrayList<>();
        setNewPath(pathPoints, closed);
        this.color = color;
    }


    public ColoredPath(ArrayList<Point2D.Float> pathPoints, boolean closed){
        this(pathPoints, RandomColorGenerator.generateRandomColor(), closed);
    }

    public void setNewPath(ArrayList<Point2D.Float> newPoints, boolean closed){
        if(!pathPoints.isEmpty()) pathPoints.clear();
        if(newPoints.isEmpty()){
            moveTo(0,0);
            pathPoints.add(new Point2D.Float(0, 0));
        }
        else{
            for (int i = 0; i < newPoints.size(); i++) {
                if(i==0)this.moveTo(newPoints.get(i).getX(), newPoints.get(i).getY());
                else this.lineTo(newPoints.get(i).getX(), newPoints.get(i).getY());
                pathPoints.add(new Point2D.Float((float) newPoints.get(i).getX(), (float) newPoints.get(i).getY()));
            }
        }
        if(closed)this.closePath();

    }

    public ColoredPath[] splitpath(ArrayList<Point2D.Float> splitPath){
        ColoredPath[] coloredPaths = new ColoredPath[2];
        if(splitPath.isEmpty()) coloredPaths[0] = this;
        else {
            ArrayList<Point2D.Float> pathA = new ArrayList<>();
            ArrayList<Point2D.Float> pathB = new ArrayList<>();
            boolean finishedPathA = false;
            boolean finishedPathB = true;
            int pointsBetween = 0;
            for (int i = 0; i < this.pathPoints.size(); i++) {
                Point2D.Float p1 = this.pathPoints.get(i);
                Point2D.Float p2 = null;
                if(i == pathPoints.size()-1){
                    p2 = pathPoints.get(0);
                }
                else {
                    p2 = this.pathPoints.get(i+1);
                }
                if(p2 != null){
                    Line2D.Float line = new Line2D.Float(p1, p2);
                    if(!finishedPathA) pathA.add(p1);
                    if(!finishedPathB) pathB.add(p1);
                    if(line.contains(splitPath.get(i))){
                        pathA.add(splitPath.get(i));
                        for (int i1 = 1; i1 < splitPath.size(); i1++) {
                            pathA.add(splitPath.get(i));
                            finishedPathA = true;
                            finishedPathB = false;
                        }
                    }
                    if(line.contains(splitPath.get(splitPath.size()-1))){
                        pathA.add(splitPath.get(splitPath.size()));
                        finishedPathA = false;
                        finishedPathB = true;
                        for (int i1 = splitPath.size()-1; i1 > 0; i1--) {
                            pathB.add(splitPath.get(i));
                        }
                    }
                }

            }
            for (Point2D.Float aFloat : pathB) {
                System.out.println("bl" + aFloat);
            }
            coloredPaths[0] = new ColoredPath(pathA, true);
            coloredPaths[1] = new ColoredPath(pathB, true);
        }
        for (Point2D.Float aFloat : splitPath) {
            System.out.println("bl" + aFloat);
        }
        System.out.println(coloredPaths[0]);
        System.out.println(coloredPaths[1]);
        return coloredPaths;
    }


    @Override
    public Color getColor() {
        return color;
    }

    public String toString(){
        String s = "";
        for (Point2D.Float pathPoint : pathPoints) {
            s += pathPoint + "\n";
        }
        return s;
    }
}

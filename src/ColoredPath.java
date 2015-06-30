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
        ArrayList<Point2D.Float> pathA = new ArrayList<>();
        ArrayList<Point2D.Float> pathB = new ArrayList<>();
        boolean finishedPathA = false;
        boolean finishedPathB = true;
        boolean firstPointFound= false;
        boolean secondPointFound = false;
        boolean isReversed = false;
        for (int i = 0; i < this.pathPoints.size(); i++) {
            //getPoints
            Point2D.Float p1 = this.pathPoints.get(i);
            Point2D.Float p2 = null;
            if(i == pathPoints.size()-1){
                p2 = pathPoints.get(0);
            }
            else {
                p2 = this.pathPoints.get(i+1);
            }
            //checkPoints
            if(p2 != null) {
                if (!finishedPathA) pathA.add(p1);
                if (!finishedPathB) pathB.add(p1);
                //both points on same line
                if(!firstPointFound && !secondPointFound && pointIsInLine(p1,p2,splitPath.get(0)) && pointIsInLine(p1,p2,splitPath.get(splitPath.size()-1))){
                    if(p1.distance(splitPath.get(0)) < p1.distance(splitPath.get(splitPath.size()-1))){
                       connectPath(pathA, splitPath);
                    }
                    else connectPathReverse(pathA, splitPath);
                    coloredPaths[1] = new ColoredPath(splitPath,true);
                    firstPointFound = true;
                    secondPointFound = true;
                }

                //points on different lines
                if (!firstPointFound && pointIsInLine(p1, p2, splitPath.get(0))) {
                    connectPath(pathA, splitPath);
                    firstPointFound = true;
                    finishedPathA = true;
                    finishedPathB = false;
                }
                if (!firstPointFound && pointIsInLine(p1, p1, splitPath.get(splitPath.size() - 1))) {
                    connectPathReverse(pathA, splitPath);
                    firstPointFound = true;
                    finishedPathA = true;
                    finishedPathB = false;
                    isReversed = true;
                }


                if (!secondPointFound && (pointIsInLine(p1, p2, splitPath.get(0)) || pointIsInLine(p1, p1, splitPath.get(splitPath.size() - 1)))) {
                    secondPointFound = true;
                    if(i == pathPoints.size()-1){
                        pathA.add(p2);
                    }
                    else {
                        finishedPathA = false;
                    }
                    finishedPathB = true;
                    if(isReversed){
                        connectPath(pathB,splitPath);
                    }
                    else connectPathReverse(pathB,splitPath);
                }
            }
        }
        coloredPaths[0] = new ColoredPath(pathA,true);
        if(coloredPaths[1] == null) coloredPaths[1] = new ColoredPath(pathB,true);

        System.out.println(coloredPaths[0]);
        System.out.println(coloredPaths[1]);
        return coloredPaths;
    }

    private void connectPathReverse(ArrayList<Point2D.Float> originalPath, ArrayList<Point2D.Float> extension){
        for(int i = extension.size()-1; i >= 0; i--){
            originalPath.add(extension.get(i));
        }
    }
    private void connectPath(ArrayList<Point2D.Float> originalPath, ArrayList<Point2D.Float> extension){
        for (int i = 0; i < extension.size(); i++) {
            originalPath.add(extension.get(i));
        }
    }

    private boolean pointIsInLine(Point2D.Float p1, Point2D.Float p2, Point2D.Float between){
        if(p1.getX() == between.getX()){
            int y1 = (int)p1.getY();
            int y2 = (int) between.getY();
            int y3 = (int) p2.getY();
            if(Math.abs(y1-y2) + Math.abs(y2-y3) == Math.abs(y1-y3)){
                return true;
            }
        }
        if(p1.getY() == between.getY()){
            int x1 = (int) p1.getX();
            int x2 = (int) between.getX();
            int x3 = (int) p2.getX();
            if(Math.abs(x1-x2) + Math.abs(x2-x3) == Math.abs(x1-x3)){
                return true;
            }
        }
        return false;
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

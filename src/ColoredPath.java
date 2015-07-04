import java.awt.*;
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

    public ColoredPath(Shape s){
        super(s);
        this.color = Color.GRAY;
        this.pathPoints = new ArrayList<>();
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

    public void setNewPath(ArrayList<Point> newPoints, boolean closed){
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

    public ColoredPath[] splitpath(ArrayList<Point> splitPath){
        ColoredPath[] coloredPaths = new ColoredPath[2];
        ArrayList<Point> pathA = new ArrayList<>();
        ArrayList<Point> pathB = new ArrayList<>();
        boolean finishedPathA = false;
        boolean finishedPathB = true;
        boolean firstPointFound= false;
        boolean secondPointFound = false;
        boolean isReversed = false;
        for (int i = 0; i < this.pathPoints.size(); i++) {
            //getPoints
            Point p1 = this.pathPoints.get(i);
            Point p2 = null;
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
                if (!firstPointFound && pointIsInLine(p1, p2, splitPath.get(splitPath.size() - 1))) {
                    //System.out.println("Found" + splitPath.get(splitPath.size()-1));
                    connectPathReverse(pathA, splitPath);
                    firstPointFound = true;
                    finishedPathA = true;
                    finishedPathB = false;
                    isReversed = true;
                }


                if (!secondPointFound) {
                    if(isReversed){
                        if(pointIsInLine(p1,p2,splitPath.get(0))){
                            secondPointFound = true;
                            connectPath(pathB,splitPath);
                            if(i == pathPoints.size()-1){
                                pathA.add(p2);
                            }
                            else {
                                finishedPathA = false;
                                finishedPathB = true;
                            }
                        }
                    }
                    else {
                        if(pointIsInLine(p1,p2,splitPath.get(splitPath.size()-1))){
                            secondPointFound = true;
                            connectPathReverse(pathB, splitPath);
                            if(i == pathPoints.size()-1){
                                pathA.add(p2);
                            }
                            else {
                                finishedPathA = false;
                                finishedPathB = true;
                            }
                        }
                    }
                }
            }
        }
        coloredPaths[0] = new ColoredPath(pathA,true);
        if(coloredPaths[1] == null) coloredPaths[1] = new ColoredPath(pathB,true);
        /*for (Point aFloat : splitPath) {
            System.out.println("splitPath: " + aFloat);
        }
        System.out.println("path0: \n" + coloredPaths[0]);
        System.out.println("path1: \n" + coloredPaths[1]);*/
        return coloredPaths;
    }

    private void connectPathReverse(ArrayList<Point> originalPath, ArrayList<Point> extension){
        for(int i = extension.size()-1; i >= 0; i--){
            originalPath.add(extension.get(i));
        }
    }
    private void connectPath(ArrayList<Point> originalPath, ArrayList<Point> extension){
        for (int i = 0; i < extension.size(); i++) {
            originalPath.add(extension.get(i));
        }
    }

    private boolean pointIsInLine(Point p1, Point p2, Point between){
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
        for (Point pathPoint : pathPoints) {
            s += pathPoint + "\n";
        }
        return s;
    }
}

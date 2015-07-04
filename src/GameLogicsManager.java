import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

public class GameLogicsManager {

    private static GameLogicsManager gameLogicsManager;

    private byte[][] totalAreaAdded;

    private Player player;
    private AIPlayer aiPlayer;

    private ColoredPath innerShape;
    private ColoredPath outerShape;
    private final ColoredPath outerShapeTemplate;
    private final ColoredPath innerShapeTemplate;

    private int totalAreaInPoints = 0;
    private final int winningCondition;

    private GameLogicsManager(){
        winningCondition = 80;
        totalAreaAdded = new byte[Board.WIDTH][Board.HEIGHT];

        player = Initializer.getInstance().getPlayer();
        aiPlayer = Initializer.getInstance().getAiPlayer();

        outerShape = new ColoredPath(Initializer.getInstance().getOuterPoints(), true);
        innerShape = new ColoredPath(Initializer.getInstance().getInnerPoints(), true);
        outerShapeTemplate = new ColoredPath(outerShape, true);
        innerShapeTemplate = new ColoredPath(innerShape, true);

    }

    public static GameLogicsManager getInstance(){
        if(gameLogicsManager == null)
            return new GameLogicsManager();
        return gameLogicsManager;
    }

    public void updateGame(){
        player.update(innerShape, outerShape);
        aiPlayer.update();
        updateTotalAreaAdded();
        if(maxTotalAreaReached()){
            //finish game;
            System.out.println("WON THE FUCKING GAME!");
        }
    }

    private void updateTotalAreaAdded(){
        for (int i = 0; i < totalAreaAdded.length; i++) {
            for(int j = 0; j < totalAreaAdded[i].length; j++){
                if(totalAreaAdded[i][j] != 1 && outerShape.contains(new Point(i,j)) ){
                    totalAreaAdded[i][j] = 1;
                    totalAreaInPoints++;
                }
            }
        }
    }

    private float calculateTotalAreaPercent(){
        float f = totalAreaInPoints/((Board.HEIGHT * Board.WIDTH)/100);
        return (float)(Math.round(f * 100))/100;
    }

    private boolean maxTotalAreaReached(){
        return calculateTotalAreaPercent() >= winningCondition;
    }

    public void splitInnerShape(ArrayList<Point> splitPoints) {
        ColoredPath[] coloredPath = splitpath(innerShape, splitPoints);
        if(coloredPath[0].contains(aiPlayer.getPosition())){
            innerShape = coloredPath[0];
            ShapeContainer.getInstance().addColoredShape(new ColoredPath(coloredPath[1], RandomColorGenerator.generateRandomColor(), true));
        }
        else{
            innerShape = coloredPath[1];
            ShapeContainer.getInstance().addColoredShape(new ColoredPath(coloredPath[1], RandomColorGenerator.generateRandomColor(), true));
        }
        Area a0 = new Area(outerShapeTemplate);
        Area a1 = new Area(innerShape);
        a0.subtract(a1);
        outerShape = new ColoredPath(a0);
    }

    private ColoredPath[] splitpath(ColoredPath coloredPath, ArrayList<Point> splitPath){
        ColoredPath[] coloredPaths = new ColoredPath[2];
        ArrayList<Point> pathA = new ArrayList<>();
        ArrayList<Point> pathB = new ArrayList<>();
        ArrayList<Point> shapeToSplit = coloredPath.getPathPoints();
        boolean finishedPathA = false;
        boolean finishedPathB = true;
        boolean firstPointFound= false;
        boolean secondPointFound = false;
        boolean isReversed = false;
        for (int i = 0; i < shapeToSplit.size(); i++) {
            //getPoints
            Point p1 = shapeToSplit.get(i);
            Point p2 = null;
            if(i == shapeToSplit.size()-1){
                p2 = shapeToSplit.get(0);
            }
            else {
                p2 = shapeToSplit.get(i+1);
            }
            //checkPoints
            if(p2 != null) {
                if (!finishedPathA) pathA.add(p1);
                if (!finishedPathB) pathB.add(p1);
                //both points on same line
                if(!firstPointFound && !secondPointFound && pointIsInLine(p1,p2,shapeToSplit.get(0)) && pointIsInLine(p1,p2,shapeToSplit.get(shapeToSplit.size()-1))){
                    if(p1.distance(shapeToSplit.get(0)) < p1.distance(shapeToSplit.get(shapeToSplit.size()-1))){
                        connectPath(pathA, shapeToSplit);
                    }
                    else connectPathReverse(pathA, shapeToSplit);
                    coloredPaths[1] = new ColoredPath(shapeToSplit,true);
                    firstPointFound = true;
                    secondPointFound = true;
                }

                //points on different lines
                if (!firstPointFound && pointIsInLine(p1, p2, shapeToSplit.get(0))) {
                    connectPath(pathA, shapeToSplit);
                    firstPointFound = true;
                    finishedPathA = true;
                    finishedPathB = false;
                }
                if (!firstPointFound && pointIsInLine(p1, p2, shapeToSplit.get(shapeToSplit.size() - 1))) {
                    //System.out.println("Found" + splitPath.get(splitPath.size()-1));
                    connectPathReverse(pathA, shapeToSplit);
                    firstPointFound = true;
                    finishedPathA = true;
                    finishedPathB = false;
                    isReversed = true;
                }


                if (!secondPointFound) {
                    if(isReversed){
                        if(pointIsInLine(p1, p2, shapeToSplit.get(0))){
                            secondPointFound = true;
                            connectPath(pathB,shapeToSplit);
                            if(i == shapeToSplit.size()-1){
                                pathA.add(p2);
                            }
                            else {
                                finishedPathA = false;
                                finishedPathB = true;
                            }
                        }
                    }
                    else {
                        if(pointIsInLine(p1,p2,shapeToSplit.get(shapeToSplit.size()-1))){
                            secondPointFound = true;
                            connectPathReverse(pathB, shapeToSplit);
                            if(i == shapeToSplit.size()-1){
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

    public static boolean pointIsInLine(Point p1, Point p2, Point between){
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

}

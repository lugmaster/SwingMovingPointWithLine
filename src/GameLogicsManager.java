import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

public class GameLogicsManager {

    private static GameLogicsManager gameLogicsManager = new GameLogicsManager();

    private byte[][] totalAreaAdded;

    private Player player;
    private AIPlayer aiPlayer;

    private ColoredPath innerPath;
    private ColoredPath outerPath;

    public final ColoredPath outerPathTemplate;
    public final ColoredPath innerPathTemplate;

    private ArrayList<ColoredPath> splitShapes;

    private int totalAreaInPoints = 0;
    private int totalAreaOffsetX;
    private int totalAreaOffsetY;
    private final int winningCondition;
    private boolean gameIsRunning = true;
    private boolean gameIsWon = false;
    private boolean gameIsLost = false;

    private GameLogicsManager(){
        winningCondition = 80;
        totalAreaOffsetX = Initializer.getInstance().INNERSHAPEDELTAX;
        totalAreaOffsetY = Initializer.getInstance().INNERSHAPEDELTAY;
        int x = Initializer.getInstance().INNERSHAPEWIDTH-totalAreaOffsetX;
        int y = Initializer.getInstance().INNERSHAPEHEIGHT-totalAreaOffsetY;
        totalAreaAdded = new byte[x][y];

        player = Initializer.getInstance().getPlayer();
        ShapeContainer.getInstance().addPlayer(player);
        aiPlayer = Initializer.getInstance().getAiPlayer();
        ShapeContainer.getInstance().addAiPlayer(aiPlayer);

        outerPath = new ColoredPath(Initializer.getInstance().getOuterShape(), true);
        innerPath = new ColoredPath(Initializer.getInstance().getInnerShape(), true);
        outerPathTemplate = new ColoredPath(outerPath, true);
        innerPathTemplate = new ColoredPath(innerPath, true);
        outerPath = subtractPath(outerPathTemplate, innerPath);
        splitShapes = new ArrayList<>();

        ShapeContainer.getInstance().addColoredShape(outerPath);
        ShapeContainer.getInstance().addColoredShape(innerPath);

    }

    public static GameLogicsManager getInstance(){
        if(gameLogicsManager == null)
            gameLogicsManager = new GameLogicsManager();
        return gameLogicsManager;
    }

    public void updateGame(){
        if(gameIsLost || gameIsWon){
            gameIsRunning = false;
            player.setGameToFinished();
            aiPlayer.setGameToFinished();
        }
        if(gameIsRunning){
            player.update(innerPath, outerPath);
            aiPlayer.update(innerPath, outerPath);
            detectPlayerCollision();
            detectPlayerPathCollision();
            updateTotalAreaAdded();
            compareTotalAreaReached();
        }
    }

    public float getAreaLeft(){
        return (100f - calculateTotalAreaPercent());
    }

    private void updateTotalAreaAdded(){
        for (int i = 0; i < totalAreaAdded.length; i++) {
            for(int j = 0; j < totalAreaAdded[i].length; j++){
                if(totalAreaAdded[i][j] != 1 && outerPath.contains(new Point(i+totalAreaOffsetX,j+totalAreaOffsetY)) ){
                    totalAreaAdded[i][j] = 1;
                    totalAreaInPoints++;
                }
            }
        }
    }

    private float calculateTotalAreaPercent(){
        float f = totalAreaInPoints/(((float)totalAreaAdded.length * (float)totalAreaAdded[0].length)/100);
        return (float)(Math.round(f * 100))/100;
    }

    private void compareTotalAreaReached(){
        if(calculateTotalAreaPercent() >= winningCondition) gameIsWon = true;
    }

    public boolean gameLost(){
        return gameIsLost;
    }

    public boolean gameWon(){
        return gameIsWon;
    }

    public void splitInnerShape(ArrayList<Point> splitPoints) {
        ColoredPath[] coloredPath = splitpath(innerPath, splitPoints);
        removeOldShapes();
        ColoredPath subPath = null;
        if(coloredPath[0].contains(aiPlayer.getPosition())){
            innerPath = new ColoredPath(coloredPath[0], innerPathTemplate.getColor(), true);
            subPath = new ColoredPath(coloredPath[1], RandomColorGenerator.generateRandomColor(), true);
        }
        else{
            innerPath = new ColoredPath(coloredPath[1], innerPathTemplate.getColor(), true);
            subPath = new ColoredPath(coloredPath[0], RandomColorGenerator.generateRandomColor(), true);
        }
        outerPath = subtractPath(outerPathTemplate, innerPath);
        splitShapes.add(subPath);
        setNewShapes();
    }

    public static ColoredPath subtractPath(ColoredShape outerShape, ColoredShape innerShape){
        Area a0 = new Area(outerShape);
        Area a1 = new Area(innerShape);
        a0.subtract(a1);
        return new ColoredPath(a0, outerShape.getColor());
    }

    private void removeOldShapes(){
        ShapeContainer.getInstance().removeColoredShape(innerPath);
        ShapeContainer.getInstance().removeColoredShape(outerPath);
        for (int i = 0; i < splitShapes.size(); i++) {
            ShapeContainer.getInstance().removeColoredShape(splitShapes.get(i));
        }
    }

    private void setNewShapes(){
        ShapeContainer.getInstance().addColoredShape(outerPath);
        ShapeContainer.getInstance().addColoredShape(innerPath);
        for (ColoredPath splitShape : splitShapes) {
            ShapeContainer.getInstance().addColoredShape(splitShape);
        }
    }

    private ColoredPath[] splitpath(ColoredPath coloredPath, ArrayList<Point> splitPoints){
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
                if(!firstPointFound && !secondPointFound && pointIsInLine(p1, p2, splitPoints.get(0)) && pointIsInLine(p1,p2,splitPoints.get(splitPoints.size()-1))){
                    if(p1.distance(splitPoints.get(0)) < p1.distance(splitPoints.get(splitPoints.size()-1))){
                        connectPath(pathA, splitPoints);
                    }
                    else connectPathReverse(pathA, splitPoints);
                    coloredPaths[1] = new ColoredPath(splitPoints,true);
                    firstPointFound = true;
                    secondPointFound = true;
                }

                //points on different lines
                if (!firstPointFound && pointIsInLine(p1, p2, splitPoints.get(0))) {
                    connectPath(pathA, splitPoints);
                    firstPointFound = true;
                    finishedPathA = true;
                    finishedPathB = false;
                }
                if (!firstPointFound && pointIsInLine(p1, p2, splitPoints.get(splitPoints.size() - 1))) {
                    //System.out.println("Found" + splitPoints.get(splitPoints.size()-1));
                    connectPathReverse(pathA, splitPoints);
                    firstPointFound = true;
                    finishedPathA = true;
                    finishedPathB = false;
                    isReversed = true;
                }


                if (!secondPointFound) {
                    if(isReversed){
                        if(pointIsInLine(p1, p2, splitPoints.get(0))){
                            secondPointFound = true;
                            connectPath(pathB,splitPoints);
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
                        if(pointIsInLine(p1,p2,splitPoints.get(splitPoints.size()-1))){
                            secondPointFound = true;
                            connectPathReverse(pathB, splitPoints);
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
        if(pathB.isEmpty()) coloredPaths[1] = new ColoredPath(splitPoints,true);
        else coloredPaths[1] = new ColoredPath(pathB,true);
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

    private void detectPlayerPathCollision(){
        if(player.getPlayerPath() != null){
            ArrayList<Point> points = player.getPlayerPath().getPathPoints();
            if(points != null && points.size() >= 2){
                for (int i = 0; i < points.size()-1; i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(i+1);
                    if(GameLogicsManager.pointIsInLine(p1,p2,aiPlayer.getPosition())){
                        gameIsLost = true;
                    }
                }
            }
        }
    }

    private void detectPlayerCollision(){
        if(player.isVulnerable() && aiPlayer.intersects(player.getBounds2D())){
            gameIsLost = true;
        }
    }

    public Player getPlayer(){
        return player;
    }

    public AIPlayer getAiPlayer(){
        return aiPlayer;
    }

}

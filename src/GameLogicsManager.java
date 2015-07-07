import java.awt.Point;
import java.awt.geom.Area;
import java.util.ArrayList;

public class GameLogicsManager {

    private static GameLogicsManager gameLogicsManager = new GameLogicsManager();

    private byte[][] totalAreaAdded;

    private Player player;
    private AIPlayer aiPlayer;

    private ColoredPath innerShape;
    private ColoredPath outerShape;

    public final ColoredPath outerShapeTemplate;
    public final ColoredPath innerShapTemplate;

    private ArrayList<ColoredShape> coloredShapes;

    private int totalAreaInPoints;
    private int totalAreaOffsetX;
    private int totalAreaOffsetY;
    private final int WINNINGCONDITION;
    private boolean gameIsRunning = true;
    private boolean gameIsWon = false;
    private boolean gameIsLost = false;

    private GameLogicsManager(){
        WINNINGCONDITION = Initializer.getInstance().getWinningCondition();
        totalAreaOffsetX = Initializer.getInstance().getInnerShapeDeltaX();
        totalAreaOffsetY = Initializer.getInstance().getInnerShapeDeltaY();
        int x = Initializer.getInstance().getInnerShapeWidth() -totalAreaOffsetX;
        int y = Initializer.getInstance().getInnerShapeHeight() -totalAreaOffsetY;
        totalAreaAdded = new byte[x][y];

        player = Initializer.getInstance().getPlayer();
        ShapeContainer.getInstance().addPlayer(player);
        aiPlayer = Initializer.getInstance().getAiPlayer();
        ShapeContainer.getInstance().addMoveableShape(aiPlayer);

        outerShape = new ColoredPath(Initializer.getInstance().getOuterShape(), true);
        innerShape = new ColoredPath(Initializer.getInstance().getInnerShape(), true);
        outerShapeTemplate = new ColoredPath(outerShape, true);
        innerShapTemplate = new ColoredPath(innerShape, true);
        outerShape = subtractPath(outerShapeTemplate, innerShape);
        coloredShapes = new ArrayList<>();

        ShapeContainer.getInstance().addColoredShape(outerShape);
        ShapeContainer.getInstance().addColoredShape(innerShape);
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
            player.update(innerShape, outerShape);
            aiPlayer.update(innerShape, outerShape);
            detectPlayerCollision();
            updateTotalAreaAdded();
            compareTotalAreaReached();
        }
    }

    public float getAreaLeft(){
        return (100f - (float)(Math.round(calculateTotalAreaPercent() * 100))/100);
    }

    private void updateTotalAreaAdded(){
        for (int i = 0; i < totalAreaAdded.length; i++) {
            for(int j = 0; j < totalAreaAdded[i].length; j++){
                if(totalAreaAdded[i][j] != 1 && outerShape.contains(new Point(i+totalAreaOffsetX,j+totalAreaOffsetY)) ){
                    totalAreaAdded[i][j] = 1;
                    totalAreaInPoints++;
                }
            }
        }
    }

    private float calculateTotalAreaPercent(){
        float f = totalAreaInPoints/(((float)totalAreaAdded.length * (float)totalAreaAdded[0].length)/100);
        return f;
    }

    private void compareTotalAreaReached(){
        if(calculateTotalAreaPercent() >= WINNINGCONDITION) gameIsWon = true;
    }

    public boolean gameLost(){
        return gameIsLost;
    }

    public boolean gameWon(){
        return gameIsWon;
    }

    public void splitInnerShape(ArrayList<Point> splitPoints) {
        ColoredPath[] coloredPath = splitpath(innerShape, splitPoints);
        removeOldShapes();
        ColoredPath subPath;
        if(coloredPath[0].contains(aiPlayer.getPosition())){
            innerShape = new ColoredPath(coloredPath[0], innerShapTemplate.getColor(), true);
            subPath = new ColoredPath(coloredPath[1], RandomColorGenerator.generateRandomColor(), true);
        }
        else{
            innerShape = new ColoredPath(coloredPath[1], innerShapTemplate.getColor(), true);
            subPath = new ColoredPath(coloredPath[0], RandomColorGenerator.generateRandomColor(), true);
        }
        outerShape = subtractPath(outerShapeTemplate, innerShape);
        coloredShapes.add(subPath);
        setNewShapes();
    }

    public static ColoredPath subtractPath(ColoredShape outerShape, ColoredShape innerShape){
        Area a0 = new Area(outerShape);
        Area a1 = new Area(innerShape);
        a0.subtract(a1);
        return new ColoredPath(a0, outerShape.getColor());
    }

    private void removeOldShapes(){
        ShapeContainer.getInstance().removeColoredShape(innerShape);
        ShapeContainer.getInstance().removeColoredShape(outerShape);
        for (int i = 0; i < coloredShapes.size(); i++) {
            ShapeContainer.getInstance().removeColoredShape(coloredShapes.get(i));
        }
    }

    private void setNewShapes(){
        ShapeContainer.getInstance().addColoredShape(outerShape);
        ShapeContainer.getInstance().addColoredShape(innerShape);
        for (ColoredShape coloredShape : coloredShapes) {
            ShapeContainer.getInstance().addColoredShape(coloredShape);
        }
    }

    private ColoredPath[] splitpath(ColoredPath coloredPath, ArrayList<Point> splitLinePoints){
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
                if(!firstPointFound && !secondPointFound && pointIsInLine(p1, p2, splitLinePoints.get(0)) && pointIsInLine(p1,p2,splitLinePoints.get(splitLinePoints.size()-1))){
                    if(p1.distance(splitLinePoints.get(0)) < p1.distance(splitLinePoints.get(splitLinePoints.size()-1))){
                        connectPath(pathA, splitLinePoints);
                    }
                    else connectPathReverse(pathA, splitLinePoints);
                    coloredPaths[1] = new ColoredPath(splitLinePoints,true);
                    firstPointFound = true;
                    secondPointFound = true;
                }

                //points on different lines
                if (!firstPointFound && pointIsInLine(p1, p2, splitLinePoints.get(0))) {
                    connectPath(pathA, splitLinePoints);
                    firstPointFound = true;
                    finishedPathA = true;
                    finishedPathB = false;
                }
                if (!firstPointFound && pointIsInLine(p1, p2, splitLinePoints.get(splitLinePoints.size() - 1))) {
                    //System.out.println("Found" + splitPoints.get(splitPoints.size()-1));
                    connectPathReverse(pathA, splitLinePoints);
                    firstPointFound = true;
                    finishedPathA = true;
                    finishedPathB = false;
                    isReversed = true;
                }


                if (!secondPointFound) {
                    if(isReversed){
                        if(pointIsInLine(p1, p2, splitLinePoints.get(0))){
                            secondPointFound = true;
                            connectPath(pathB,splitLinePoints);
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
                        if(pointIsInLine(p1,p2,splitLinePoints.get(splitLinePoints.size()-1))){
                            secondPointFound = true;
                            connectPathReverse(pathB, splitLinePoints);
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
        if(pathB.isEmpty()) coloredPaths[1] = new ColoredPath(splitLinePoints,true);
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

    private void detectPlayerCollision(){
        if(aiPlayer.getPlayerCollisionFound()) gameIsLost = true;
    }

    public Player getPlayer(){
        return player;
    }

    public AIPlayer getAiPlayer(){
        return aiPlayer;
    }

    public ColoredPath getInnerShape(){
        return innerShape;
    }

    public ColoredPath getOuterShape(){
        return outerShape;
    }

    public ArrayList<ColoredShape> coloredShapes(){
        return coloredShapes;
    }

}

import java.awt.Point;
import java.awt.geom.Area;
import java.util.ArrayList;

/*
 * GameLogicsManager contains the game logic.
 * It contains the inner and outer shape, it stores the amount of the "cut" shapes in pixels(absolute) and in percentage
 * (relative to the inner shape) and the winning conditions (percentage amount of cut shape that needs to be reached).
 * It contains the update logic which is applied every timer tick ("update cycle") and contains a list with all shapes that need to be drawn
 * by the ShapeContainer Class.
 * The GameLogicsManager also has a method to split a ColoredPath object along a list of points.
 *
 */

public class GameLogicsManager {

    private static GameLogicsManager gameLogicsManager = new GameLogicsManager();

    // storing pixels from shapes added through a cut
    // possible values:
    // 1 ... outerShape contains Pixel
    // 0 ... outerShape NOT contains Pixel
    private byte[][] totalAreaAdded;

    private Player player;
    private AIPlayer aiPlayer;

    private ColoredPath innerShape;
    private ColoredPath outerShape;

    //used to recreate all shapes after a cut
    private final ColoredPath outerShapeTemplate;
    private final ColoredPath innerShapeTemplate;

    //contains all shapes that need to be drawn by ShapeContainer
    private ArrayList<ColoredShape> coloredShapes;

    //absolute value of outerShape
    private int totalAreaInPoints;
    //the offset applied to each position in byte[][] totalAreaAdded(see updateTotalArea())
    private int totalAreaOffsetX;
    private int totalAreaOffsetY;

    private final int WINNINGCONDITION;

    private boolean gameIsRunning = true;
    private boolean gameIsWon = false;
    private boolean gameIsLost = false;

    /*
     * all necessary parameters are taken from Initializer Class
     */

    private GameLogicsManager(){
        WINNINGCONDITION = Initializer.getInstance().getWinningCondition();

        //Byte 2dArray
        totalAreaOffsetX = Initializer.getInstance().getInnerShapeDistanceToBorderX();
        totalAreaOffsetY = Initializer.getInstance().getInnerShapeDistanceToBorderY();

        // creates a sub coordinate system from the inner shape template
        int x = Initializer.getInstance().getInnerShapeWidth() -totalAreaOffsetX;
        int y = Initializer.getInstance().getInnerShapeHeight() -totalAreaOffsetY;
        totalAreaAdded = new byte[x][y];

        player = Initializer.getInstance().getPlayer();
        aiPlayer = Initializer.getInstance().getAiPlayer();

        outerShape = new ColoredPath(Initializer.getInstance().getOuterShape(), true);
        innerShape = new ColoredPath(Initializer.getInstance().getInnerShape(), true);
        outerShapeTemplate = new ColoredPath(outerShape, true);
        innerShapeTemplate = new ColoredPath(innerShape, true);
        outerShape = subtractShapes(outerShapeTemplate, innerShape);
        coloredShapes = new ArrayList<>();
    }

    public static GameLogicsManager getInstance(){
        if(gameLogicsManager == null)
            gameLogicsManager = new GameLogicsManager();
        return gameLogicsManager;
    }

    /*
     * this method is applied each update cycle
     */

    public void update(){
        if(gameIsLost || gameIsWon){
            gameIsRunning = false;
            player.setGameToFinished();
            aiPlayer.setGameToFinished();
        }
        if(gameIsRunning){
            player.update(innerShape, outerShape);
            aiPlayer.update(outerShape);
            detectPlayerCollision();
            updateTotalAreaAdded();
            compareTotalAreaReached();
        }
    }

    public AIPlayer getAiPlayer() {
        return aiPlayer;
    }

    public Player getPlayer() {
        return player;
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

    /*
     * @return float value of area added from inner to outer shape in percent (00.00f)
     */

    public float getAreaLeftPercent(){
        return (100f - (float)(Math.round(calculateTotalAreaPercent() * 100))/100);
    }

    public boolean gameIsLost(){
        return gameIsLost;
    }

    public boolean gameIsWon(){
        return gameIsWon;
    }

    /*
     * InnerShape is split by a given list of points, which form a line drawn by the player object.
     * InnerShape will be split into 2 pieces, the one NOT containing the aiPlayer is added to the outer shape
     * and will be counted in totalAreaAdded[][]
     * The new shapes will be added to the coloredShapes arrayList
     * The splitting will be made by the method splitShapes
     * @param splitLinePoints a list containing all points forming a line to split the inner shape
     */
    public void splitInnerShape(ArrayList<Point> splitLinePoints) {
        ColoredPath[] coloredShape = splitShapes(innerShape, splitLinePoints);
        ColoredPath subShape;
        if(coloredShape[0].contains(aiPlayer.getPosition())){
            innerShape = new ColoredPath(coloredShape[0], innerShapeTemplate.getColor(), true);
            subShape = new ColoredPath(coloredShape[1], RandomColorGenerator.generateRandomColor(), true);
        }
        else{
            innerShape = new ColoredPath(coloredShape[1], innerShapeTemplate.getColor(), true);
            subShape = new ColoredPath(coloredShape[0], RandomColorGenerator.generateRandomColor(), true);
        }
        outerShape = subtractShapes(outerShapeTemplate, innerShape);
        coloredShapes.add(subShape);
    }


    /*
     * checks is if a point lies between to points (p1,p2) which form a line
     * @param p1 first point of the line
     * @param p2 second point of the line
     * @param between point to check if inline with line(p1,p2)
     */
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


    /*
     * Detect if aiPlayer found a collision
     */
    private void detectPlayerCollision(){
        if(aiPlayer.getPlayerCollisionFound()) gameIsLost = true;
    }

    /*
     * each point the cut and added shapes contain is marked with 1
     * totalAreaInPoints stores the absolute value of points in shapes that have been added to the outer shape, means
     * as well, the original outer shape is not counted!
     */
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

    /*
     * returns an percentage amount of added shapes through cuts
     */
    private float calculateTotalAreaPercent(){
        return totalAreaInPoints/(((float)totalAreaAdded.length * (float)totalAreaAdded[0].length)/100);
    }

    private void compareTotalAreaReached(){
        if(calculateTotalAreaPercent() >= WINNINGCONDITION) gameIsWon = true;
    }

    /*
     * creates a area by two given ColoredShapes and subtracts them and
     * returns the difference as new ColoredPath
     */
    private ColoredPath subtractShapes(ColoredShape minuend, ColoredShape subtrahend){
        Area a0 = new Area(minuend);
        Area a1 = new Area(subtrahend);
        a0.subtract(a1);
        return new ColoredPath(a0, minuend.getColor());
    }

    /*
     * Splits a closed rectangular coloredPath object along a line
     * The outline of a rectangular shape can be seen es a graph, where the nodes represent the corners of the shape
     * A rectangular shape can always be split by a line which starts between two nodes on the graph representation and
     * ends between two nodes (the last node is connected to the first node!).
     * The splitShape method iterates over all points that outline a given rectangular closed Path.
     * - Each point will be added to a new arrayList called pathA.
     *   In each step it takes a point and its successor from the shape outline and checks if the first or the last point of the splitPath
     *   lie between those 2 points.
     * - If a point is found, the splitPath will be completely added to pathA.
     * - After adding the splitPath to pathA, a new arrayList pathB is created. To pathB all remaining points from the outline shape iteration will be
     *   added until the second point from the splitPath sitting between to points of the shape outline is found.
     * - Now its switched again and pathA gets the remaining points from the shape outline again.
     * - To pathB splitPath is also added, but in the opposite direction as to pathA
     * - Now there are to two new rectangular closed paths.
     *
     * If a point is found, it needs to be determined, if the split line needs to be appended
     * forward or reverse.
     * There are 2 possible cases:
     *  1.) Start and endpoint of the splitLine lie between the same two points of the path outline
     *  2.) Startpoint lies between two points of the path and endpoint between another pair.
     *  In all other cases the shape can not be split.
     *
     */

    private ColoredPath[] splitShapes(ColoredPath oldShape, ArrayList<Point> splitLinePoints){
        ColoredPath[] newShapes = new ColoredPath[2];
        ArrayList<Point> pathA = new ArrayList<>();
        ArrayList<Point> pathB = new ArrayList<>();
        ArrayList<Point> shapeToSplit = oldShape.getPathPoints();
        boolean finishedPathA = false;
        boolean finishedPathB = true;
        boolean firstPointFound= false;
        boolean secondPointFound = false;
        boolean isReversed = false;
        for (int i = 0; i < shapeToSplit.size(); i++) {
            //getPoints
            Point p1 = shapeToSplit.get(i);
            Point p2;
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
                    newShapes[1] = new ColoredPath(splitLinePoints,true);
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
        newShapes[0] = new ColoredPath(pathA,true);
        if(pathB.isEmpty())
            newShapes[1] = new ColoredPath(splitLinePoints,true);
        else
            newShapes[1] = new ColoredPath(pathB,true);
        return newShapes;
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

}

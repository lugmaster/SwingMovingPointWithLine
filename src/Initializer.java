import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;

/*
 * Initializer holds all Data that needs to be initialized on different objects.
 * Information on the initialized objects is found in der their classes.
 */
public class Initializer {

    private static Initializer initializer = new Initializer();

    private ColoredPath outerShape;
    private ColoredPath innerShape;

    private Player player;
    private AIPlayer aiPlayer;

    private int innerShapeWidth;
    private int innerShapeHeight;
    private int innerShapeDistanceToBorderX;
    private int innerShapeDistanceToBorderY;

    private int boardWidth;
    private int boardHeight;

    private int timerDelay;

    private int winningCondition;

    private Initializer(){
        init();
    }

    public static Initializer getInstance(){
        if(initializer == null)
            initializer = new Initializer();
        return initializer;
    }

    private void init(){
        //Size of Board and amount of addedArea needed to win
        boardWidth = 200;
        boardHeight = 200;
        winningCondition = 80;
        timerDelay = 10;

        //player and ai
        player = new Player(194,100,3,1,Color.blue);
        aiPlayer = new AIPlayer(50,160,9,2,Color.red,player,100);


        //outerShape
        Point p0 = new Point(-3,-3);
        Point p1 = new Point(boardWidth+3,-3);
        Point p2 = new Point(boardWidth+3,boardHeight+3);
        Point p3 = new Point(-3,boardHeight+3);
        ArrayList<Point> outerPoints = new ArrayList<>();
        outerPoints.add(p0);
        outerPoints.add(p1);
        outerPoints.add(p2);
        outerPoints.add(p3);
        outerShape = new ColoredPath(outerPoints, Color.DARK_GRAY, true);

        //innerShape
        ArrayList<Point> innerPoints = new ArrayList<>();
        innerShapeWidth = boardWidth-10;
        innerShapeHeight = boardHeight-10;
        innerShapeDistanceToBorderX = 9;
        innerShapeDistanceToBorderY = 9;
        Point p4 = new Point(innerShapeDistanceToBorderX, innerShapeHeight);
        Point p5 = new Point(innerShapeDistanceToBorderX, innerShapeDistanceToBorderY);
        Point p6 = new Point(innerShapeWidth, innerShapeDistanceToBorderY);
        Point p7 = new Point(innerShapeWidth, innerShapeHeight);
        innerPoints.add(p4);
        innerPoints.add(p5);
        innerPoints.add(p6);
        innerPoints.add(p7);
        innerShape = new ColoredPath(innerPoints, Color.BLACK, true);
    }

    public int getInnerShapeWidth() {
        return innerShapeWidth;
    }

    public int getInnerShapeHeight() {
        return innerShapeHeight;
    }

    public int getInnerShapeDistanceToBorderX() {
        return innerShapeDistanceToBorderX;
    }

    public int getInnerShapeDistanceToBorderY() {
        return innerShapeDistanceToBorderY;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getWinningCondition() {
        return winningCondition;
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

    public int getTimerDelay(){
        return timerDelay;
    }
}

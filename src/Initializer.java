import java.awt.*;
import java.util.ArrayList;

public class Initializer {

    private static Initializer initializer = new Initializer();

    private ArrayList<Point> innerPoints = new ArrayList<>();
    private ArrayList<Point> outerPoints = new ArrayList<>();

    private Player player;
    private AIPlayer aiPlayer;

    private Initializer(){
        player = new Player(194, 150, 3, 3, 1f, Color.blue);
        aiPlayer = new AIPlayer(50,50,3,3,1.0f, Color.red);

        //outer
        Point p0 = new Point(-3,-3);
        Point p1 = new Point(Board.WIDTH+3,-3);
        Point p2 = new Point(Board.WIDTH+3,Board.HEIGHT+3);
        Point p3 = new Point(-3,Board.HEIGHT+3);
        outerPoints.add(p0);
        outerPoints.add(p1);
        outerPoints.add(p2);
        outerPoints.add(p3);

        //inner
        Point p4 = new Point(9,190);
        Point p5 = new Point(9,9);
        Point p6 = new Point(190,9);
        Point p7 = new Point(190,190);
        innerPoints.add(p4);
        innerPoints.add(p5);
        innerPoints.add(p6);
        innerPoints.add(p7);
    }

    public static Initializer getInstance(){
        if(initializer == null)
            initializer = new Initializer();
        return initializer;
    }

    public Player getPlayer(){
        return player;
    }

    public AIPlayer getAiPlayer(){
        return aiPlayer;
    }

    public ArrayList<Point> getInnerPoints(){
        return innerPoints;
    }

    public ArrayList<Point> getOuterPoints(){
        return outerPoints;
    }

}

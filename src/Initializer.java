import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;

public class Initializer {

    private static Initializer initializer = new Initializer();

    private ArrayList<Point> innerPoints = new ArrayList<>();
    private ArrayList<Point> outerPoints = new ArrayList<>();

    private ColoredPath outerShape;
    private ColoredPath innerShape;

    private Player player;
    private AIPlayer aiPlayer;

    public final int INNERSHAPEWIDTH;
    public final int INNERSHAPEHEIGHT;
    public final int INNERSHAPEDELTAX;
    public final int INNERSHAPEDELTAY;

    private Initializer(){
        player = new Player(194,100,3,1,Color.blue);
        aiPlayer = new AIPlayer(50,160,9,2,Color.red,player);
        INNERSHAPEWIDTH = Board.WIDTH-10;
        INNERSHAPEHEIGHT = Board.HEIGHT-10;
        INNERSHAPEDELTAX = 9;
        INNERSHAPEDELTAY = 9;
        //outer
        Point p0 = new Point(-3,-3);
        Point p1 = new Point(Board.WIDTH+3,-3);
        Point p2 = new Point(Board.WIDTH+3,Board.HEIGHT+3);
        Point p3 = new Point(-3,Board.HEIGHT+3);
        outerPoints.add(p0);
        outerPoints.add(p1);
        outerPoints.add(p2);
        outerPoints.add(p3);
        outerShape = new ColoredPath(outerPoints, Color.DARK_GRAY, true);

        //inner
        Point p4 = new Point(INNERSHAPEDELTAX,INNERSHAPEHEIGHT);
        Point p5 = new Point(INNERSHAPEDELTAX,INNERSHAPEDELTAY);
        Point p6 = new Point(INNERSHAPEWIDTH,INNERSHAPEDELTAY);
        Point p7 = new Point(INNERSHAPEWIDTH,INNERSHAPEHEIGHT);
        innerPoints.add(p4);
        innerPoints.add(p5);
        innerPoints.add(p6);
        innerPoints.add(p7);
        innerShape = new ColoredPath(innerPoints, Color.BLACK, true);
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

    public ColoredPath getInnerShape(){
        return innerShape;
    }

    public ColoredPath getOuterShape(){
        return outerShape;
    }

}

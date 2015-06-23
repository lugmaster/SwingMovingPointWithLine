import java.awt.*;
import java.util.ArrayList;

public class AIPlayer extends ColoredEllipse implements MoveableShape{
    private ArrayList<Point> lines = new ArrayList<>();
    private boolean moveable;
    protected float dx, dy;


    public AIPlayer (int x, int y, int width, int height, Color color){
        super(x,y,width,height,color);
    }

    @Override
    public void move() {

    }

    @Override
    public void stopMoving() {

    }

    @Override
    public ArrayList<Point> getLines() {
        return null;
    }
}

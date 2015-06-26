import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Lukas Normal on 23.06.2015.
 */
public abstract class MoveableEllipse extends ColoredEllipse implements MoveableShape {

    protected float dx, dy;
    protected float moveSpeed;

    public MoveableEllipse(int x, int y, int width, int height, float moveSpeed){
        this(x, y, width, height, moveSpeed, Color.blue);
    }

    public MoveableEllipse(int x, int y, int width, int height, float moveSpeed, Color color){
        super(x, y, width, height, color);
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void move() {

        if(x + dx < 0) {
            x = 0;
        }
        else if(x + dx > Board.WIDTH-1) {
            x = Board.WIDTH-1;
        }
        else x += dx;

        if(y + dy < 0) {
            y = 0;
        }
        else if (y + dy > Board.HEIGHT-1) {
            y = Board.HEIGHT-1;
        }
        else y+= dy;
        //System.out.println("X: " + x + ", Y: " +y);
    }





}

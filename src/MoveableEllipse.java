import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Lukas Normal on 23.06.2015.
 */
public abstract class MoveableEllipse extends ColoredEllipse implements MoveableShape {


    protected float moveSpeed;

    public MoveableEllipse(float x, float y, float width, float height, float moveSpeed){
        this(x, y, width, height, moveSpeed, Color.blue);
    }

    public MoveableEllipse(float x, float y, float width, float height, float moveSpeed, Color color){
        super(x, y, width, height, color);
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void move(float dx, float dy) {

        if(super.x + dx < 0) {
            super.x = 0;
        }
        else if(super.x + dx > Board.WIDTH-1) {
            super.x = Board.WIDTH-1;
        }
        else super.x += dx;

        if(super.y + dy < 0) {
            super.y = 0;
        }
        else if (super.y + dy > Board.HEIGHT-1) {
            super.y = Board.HEIGHT-1;
        }
        else super.y+= dy;
    }







}

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

        if(super.x + dx < (int) (width/2)) {
            super.x = (int) (width/2);
        }
        else if(super.x + dx > Board.WIDTH-(int)(width)) {
            super.x = Board.WIDTH-(int) (width);
        }
        else super.x += dx;

        if(super.y + dy < (int) (height/2)) {
            super.y = (int) (height/2);
        }
        else if (super.y + dy > Board.HEIGHT-(int) (height)) {
            super.y = Board.HEIGHT- (int) (height);
        }
        else super.y+= dy;
    }







}

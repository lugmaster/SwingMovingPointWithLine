import java.awt.*;
import java.awt.geom.Ellipse2D;

public abstract class ColoredEllipse extends Ellipse2D.Float implements MoveableShape  {

    protected float moveSpeed;
    private final Color color;

    public ColoredEllipse(float x, float y, float width, float height, float moveSpeed, Color color){
        super(x, y, width, height);
        this.color = color;
        this.moveSpeed = moveSpeed;
    }

    @Override
    public Color getColor(){
        return color;
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

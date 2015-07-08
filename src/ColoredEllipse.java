import java.awt.Color;
import java.awt.geom.Ellipse2D;

/*
 * This class lays the foundation of player and ai movement
 * @param int x,y   the coordinates of the object
 * @param diameter    height and width of the ellipse
 */

public abstract class ColoredEllipse extends Ellipse2D.Float implements ColoredShape  {

    private final Color color;
    private int diameter;

    public ColoredEllipse(int x, int y, int diameter, Color color){
        super(x, y, diameter, diameter);
        this.color = color;
        this.diameter = diameter;
    }

    @Override
    public Color getColor(){
        return color;
    }

    /*
     * translates the x and y value from superclass Ellipse2D by
     * given deltaX, deltaY.
     * dx,dy should only have following values {-1,0,1}.
     * If the moving object would pass the upper or lower Boarder, it will prohibited to move further
     * and x and y will be the last reachable point inside the gameWindow
     */

    public void move(int dx, int dy) {

        if(super.x + dx < diameter /2) {
            super.x = diameter /2;
        }
        else if(super.x + dx > Board.WIDTH - diameter) {
            super.x = Board.WIDTH - diameter;
        }
        else super.x += dx;

        if(super.y + dy < diameter) {
            super.y = diameter;
        }
        else if (super.y + dy > Board.HEIGHT - diameter) {
            super.y = Board.HEIGHT - diameter;
        }
        else super.y+= dy;
    }

}

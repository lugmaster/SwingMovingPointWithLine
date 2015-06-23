import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public abstract class ColoredEllipse extends Ellipse2D.Float implements MoveableShape  {
    final Color color;
    private ArrayList<Point> lines = new ArrayList<>();
    private boolean moveable;
    protected float dx, dy;

    public ColoredEllipse(int x, int y, int width, int height, Color color){
        super(x, y, width, height);
        this.color = color;
    }

    @Override
    public Color getColor(){
        return color;
    }

    @Override
    public void move() {

        if(x > 0 && x < 200) {
            moveable = true;
            x += dx;
        }
        else if(x < 1) {
             x = 1;
        }
        else if(x > 199) {
            x = 199;
        }

        if(y > 0 && y < 200) {
            moveable = true;
            y += dy;
        }
        else if(y < 1) {
            y = 1;
        }
        else if(y > 199) {
            y = 199;
        }
        //System.out.println("X: " + x + ", Y: " +y);

        add(x,y);
    }

    private void add(double x, double y) {
        lines.add(new Point((int) x, (int) y));
    }

    @Override
    public ArrayList<Point> getLines() {
        return lines;
    }

    @Override
    public void stopMoving() {
        moveable = false;
        lines.clear();
    }


}

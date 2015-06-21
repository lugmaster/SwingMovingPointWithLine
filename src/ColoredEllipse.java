import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public abstract class ColoredEllipse extends Ellipse2D.Double implements MoveableShape  {
    final Color color;
    private ArrayList<Point> lines = new ArrayList<>();
    private boolean moveable;

    public ColoredEllipse(int x, int y, int width, int height, Color color){
        super(x, y, width, height);
        this.color = color;
    }

    @Override
    public Color getColor(){
        return color;
    }

    @Override
    public void move(int dx, int dy) {
        moveable = true;
        x += dx;
        y += dy;
        add(x,y);
    }

    private void add(double x, double y) {
        lines.add(new Point((int) x, (int) y));
    }

    @Override
    public ArrayList<Point> getLines() {
        return lines;
    }

    public void stopMoving() {
        moveable = false;
        lines.clear();
    }

}

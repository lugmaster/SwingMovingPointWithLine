import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Player extends Ellipse2D.Double implements MoveableShape  {
    final Color color;
    private boolean moveable;
    private ArrayList<Point> lines = new ArrayList<>();
    int dx, dy;

    public Player(int x, int y, int width, int height){
        super(x, y, width, height);
        moveable = false;
        dx = 0;
        dy = 0;
        color = Color.blue;
    }

    public Player(int x, int y, int width, int height, Color color){
        super(x, y, width, height);
        moveable = false;
        dx = 0;
        dy = 0;
        this.color = color;
    }

    @Override
    public Color getColor(){
        return color;
    }

    public void move() {
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

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            dx = -1;
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            dx = 1;
        }

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            dy = -1;
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            dy = 1;
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }


}

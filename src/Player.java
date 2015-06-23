import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Player extends ColoredEllipse implements MoveableShape {

    private ArrayList<Point> lines = new ArrayList<>();
    private boolean moveable;
    protected float dx, dy;

    private float moveSpeed;

    public Player(int x, int y, int width, int height, float moveSpeed){
        this(x, y, width, height, moveSpeed, Color.blue);
    }

    public Player(int x, int y, int width, int height, float moveSpeed, Color color){
        super(x, y, width, height, color);
        this.moveSpeed = moveSpeed;
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



    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            dx = -moveSpeed;
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            dx = moveSpeed;
        }

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            dy = -moveSpeed;
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            dy = moveSpeed;
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

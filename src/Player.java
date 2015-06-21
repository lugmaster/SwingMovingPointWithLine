import java.awt.Color;
import java.awt.event.KeyEvent;

public class Player extends ColoredEllipse  {

    public Player(int x, int y, int width, int height){
        super(x, y, width, height, Color.blue);
    }

    public Player(int x, int y, int width, int height, Color color){
        super(x, y, width, height, color);
    }



    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            move(-1,0);
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            move(1,0);
        }

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            move(0,-1);
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            move(0,1);
        }
    }

    public void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            moveZero();
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            moveZero();
        }

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            moveZero();
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            moveZero();
        }
    }

    private void moveZero(){
        move(0,0);
    }


}

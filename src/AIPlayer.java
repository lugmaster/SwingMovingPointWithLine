import java.awt.*;
import java.util.Random;

public class AIPlayer extends ColoredEllipse{

    private Point position;
    private boolean gameIsRunning = true;
    private int dx,dy;
    private int coolDown;
    private final int RESETCOOLDOWN;
    Random random;

    public AIPlayer (int x, int y, int width, int height, float moveSpeed, Color color){
        super(x,y,width,height, moveSpeed, color);
        position = new Point(x,y);
        random = new Random();
        RESETCOOLDOWN = 150;
        coolDown = RESETCOOLDOWN;
        initMovement();
    }

    public Point getPosition(){
        return position;
    }

    private void updatePosition(){
        position.setLocation(this.x, this.y);
    }

    public void update(ColoredPath inner, ColoredPath outer){
        if(gameIsRunning){
            detectCollision(inner, outer);
            move();
            updatePosition();
            randomMovementChange();
        }
    }

    public void move(){
        super.move(dx,dy);
    }

    public void setGameToFinished(){
        gameIsRunning = false;
    }

    private void initMovement(){
        dx = random.nextInt(2) - random.nextInt(2);
        dy = random.nextInt(2) - random.nextInt(2);
        while (dx == 0 && dy == 0) {
            dx = random.nextInt(2) - random.nextInt(2);
            dy = random.nextInt(2) - random.nextInt(2);
        }
        System.out.println("dx:" + dx + " ,dy:" +dy);


    }

    private void revertMovement(){
        if(dx != 0 && dy != 0){
            dy = -dy;
        }
        else {
            dx = -dx;
            dy = -dy;
        }
    }

    private void randomMovementChange(){
        if(coolDown == 0){
            int chance = random.nextInt(100);
            if(chance == 1){
                coolDown = 150;
                initMovement();
            }
        }else{
            coolDown--;
        }
    }

    private void detectCollision(ColoredPath inner, ColoredPath outer){
        if(outer.intersects(this.getBounds())){
            revertMovement();
        }
    }

}

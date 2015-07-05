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
        dx = 0;
        dy = 0;
        while (dx == 0 && dy == 0) {
            dx = random.nextInt(2) - random.nextInt(2);
            dy = random.nextInt(2) - random.nextInt(2);
        }
        dx *= moveSpeed;
        dy *= moveSpeed;
        System.out.println("dx:" + dx + " ,dy:" +dy);


    }

    private void revertMovement(){
        if( (Math.abs(dx / moveSpeed) == 1 && dy == 0) || (Math.abs(dy / moveSpeed) == 1 && dx == 0) ){
            dx = -dx;
            dy = -dy;
            return;
        }
        if(dx < 0) {
            if(dy < 0) {
                dx = -dx;
                return;
            }
            if(dy > 0) {
                dy = -dy;
                return;
            }
        }
        if(dx > 0) {
            if(dy < 0) {
                dy = -dy;
                return;
            }
            if(dy > 0) {
                dx = -dx;
                return;
            }
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

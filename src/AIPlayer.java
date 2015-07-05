import java.awt.*;
import java.util.Random;

public class AIPlayer extends ColoredEllipse{

    private Point position;
    private boolean gameIsRunning = true;
    private int dx,dy;
    private int coolDown;

    public AIPlayer (int x, int y, int width, int height, float moveSpeed, Color color){
        super(x,y,width,height, moveSpeed, color);
        position = new Point(x,y);
        coolDown = 0;
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
            initMovement();
        }
    }

    public void move(){
        super.move(dx,dy);
    }

    public void setGameToFinished(){
        gameIsRunning = false;
    }

    private void initMovement(){
        if(coolDown == 0){
            Random r = new Random();
            int colorIndex = r.nextInt(100);
            if(colorIndex == 1) coolDown = 150;
        }else{
            coolDown--;
            System.out.println(coolDown);
        }


    }

    private void revertMovment(){

    }

    private void randomMovementChange(){

    }

    private void detectCollision(ColoredPath inner, ColoredPath outer){
        if(outer.intersects(this.getBounds())){
            //revertMovement();
        }
    }

}

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AIPlayer extends ColoredEllipse{

    private Point position;
    private boolean gameIsRunning = true;
    private boolean playerCollisionFound = false;
    private int dx,dy;
    private int coolDown;
    private int moveSpeed;
    private Player player;
    private Random random;

    public AIPlayer (int x, int y, int width, int height, int moveSpeed, Color color, Player player){
        super(x,y,width,height, color);
        position = new Point(x,y);
        random = new Random();
        coolDown = 100;
        this.moveSpeed = moveSpeed;
        this.player = player;
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
            int updateSteps = moveSpeed;
            while (updateSteps > 0) {
                move();
                updatePosition();
                randomMovementChange();
                detectCollision(inner, outer);
                detectPlayerCollision();
                detectPlayerPathCollision();
                updateSteps--;
            }

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
        System.out.println("dx:" + dx + " ,dy:" +dy);


    }

    private void revertMovement(){
        if( (Math.abs(dx) == 1 && dy == 0) || (Math.abs(dy) == 1 && dx == 0) ){
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
                coolDown = 100;
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

    private void detectPlayerPathCollision(){
        if(player.getPlayerPath() != null){
            ArrayList<Point> points = player.getPlayerPath().getPathPoints();
            if(points != null && points.size() >= 2){
                for (int i = 0; i < points.size()-1; i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(i+1);
                    if(GameLogicsManager.pointIsInLine(p1,p2,this.getPosition())){
                        playerCollisionFound = true;
                    }
                }
            }
        }
    }

    private void detectPlayerCollision(){
        if(player.isVulnerable() && this.intersects(player.getBounds2D())){
            playerCollisionFound = true;
        }
    }

    public boolean getPlayerCollisionFound(){
        return playerCollisionFound;
    }
}

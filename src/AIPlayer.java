import java.awt.Point;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/*
 * This class generates the aiPlayer, which is initialized with a random direction.
 * Each timer tick the aiPlayer object is updated and moves around by adding dx,dy to its x and y coordinates from superclass
 * The aiPlayer is only allowed to move in the innerShape
 * @param x,y Coordinates of the aiPlayer, stored in position
 * @param radius radius of the ColoredEllipseShape the aiPlayer is extending
 * @param moveSpeed sets how often update is applied per timer tick
 * @param color when drawn
 * @param player reference to the player object for detecting collisions
 * @param coolDown the amount of timer ticks needed to apply a random movement change after occurrence
 */

public final class AIPlayer extends ColoredEllipse{

    private Point position;
    private boolean gameIsRunning = true; // as long as true, the game is running
    private boolean playerCollisionFound = false; //once set true on collision, the game will stop
    private int dx,dy; //delta values for changing position with each timer tick
    private int coolDown;
    private int moveSpeed;
    private Player player;
    private Random random;

    public AIPlayer (int x, int y, int radius, int moveSpeed, Color color, Player player, int coolDown){
        super(x,y,radius,color);
        position = new Point(x,y);
        random = new Random();
        this.coolDown = coolDown;
        this.moveSpeed = moveSpeed;
        this.player = player;
        initMovement();
    }

    public Point getPosition(){
        return position;
    }

    /*
     * This method is applied every timer tick (see class board)
     * @param outer this coloredPath is used for detecting Collision
     */
    public void update(ColoredPath outer){
        if(gameIsRunning){
            int updateSteps = moveSpeed;
            while (updateSteps > 0) {
                randomMovementChange();
                move();
                updatePosition();
                detectCollision(outer);
                detectPlayerCollision();
                detectPlayerPathCollision();
                updateSteps--;
            }

        }
    }

    public boolean getPlayerCollisionFound(){
        return playerCollisionFound;
    }


    /*
     * Applies dx and dy to superclass move, both can be 0 or +/-1
     * @param dx,dy
     */
    private void move(){
        super.move(dx,dy);
    }


    /*
     * is Applied when winning or lost conditions of the game are met
     */
    public void setGameToFinished(){
        gameIsRunning = false;
    }

    /*
     * initialises delta values (dx,dy) with random numbers between -1,0,1
     */
    private void initMovement(){
        dx = 0;
        dy = 0;
        while (dx == 0 && dy == 0) {
            dx = random.nextInt(2) - random.nextInt(2);
            dy = random.nextInt(2) - random.nextInt(2);
        }
    }

    /*
     * reverts delta values (dx,dy), depending on former movingDirection
     */
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
            }
        }
    }

    /*
     *  changes the actual moving direction, if coolDown is 0 and a 1% chance is met
     *  coolDown is only restarted if 1% chance is met
     */
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

    /*
     * detects if aiPlayer object is colliding with outerShape
     */
    private void detectCollision(ColoredPath outer){
        if(outer.intersects(this.getBounds())){
            revertMovement();
            //coolDown = 100;
        }
    }

    /*
     * detects if aiPlayer object is colliding with player object
     */
    private void detectPlayerCollision(){
        if(player.isVulnerable() && this.intersects(player.getBounds2D())){
            playerCollisionFound = true;
        }
    }

    /*
     * detects if aiPlayer object is colliding with drawn line from player object
     * by creating a line from 2 playerPath points and checking if aiPlayers center lies between
     */
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

    private void updatePosition(){
        position.setLocation(this.x, this.y);
    }

}

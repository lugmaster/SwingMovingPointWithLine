import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Player extends ColoredEllipse{

    private final int LEFT = KeyEvent.VK_A;
    private final int RIGHT = KeyEvent.VK_D;
    private final int UP = KeyEvent.VK_W;
    private final int DOWN = KeyEvent.VK_S;

    private boolean isColliding = false;
    private boolean isFirstCollision = false;
    private boolean isDrawingLines = false;
    private boolean isFirstMove = true;
    private boolean isMoving = false;

    private int direction = -1;
    private int lastDirection = -1;
    private int startDirectionAfterCol = -1;
    private int lastKeyPressed = -1;
    private int lastKeyReleased = -1;
    private int angularSum = 0;


    private float dx,dy;

    private Point position;
    private ColoredPath path;

    private ArrayList<Point> points;


    public Player(float x, float y, float width, float height, float moveSpeed, Color color){
        super(x, y, width, height, moveSpeed, color);
        position = new Point((int)x,(int)y);
        points = new ArrayList<>();
        points.add(new Point(position));
        path = new ColoredPath(color);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(isValidKey(key)) {
            lastKeyPressed = key;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(isValidKey(key)){
            if(key == lastKeyPressed){
                lastKeyPressed = -1;
            }
        }
    }

    public void updatePlayer(ColoredPath inner, ColoredPath outer){
        move();
        updatePosition();
        detectCollisionShapes(inner, outer);
    }

    public ColoredPath getPlayerPath(){
        if(isDrawingLines){
            ArrayList<Point> playerPath = new ArrayList<>(points);
            /*for (Point point : playerPath) {
                System.out.println("Ppath: " + point);
            }*/
            playerPath.add(new Point(position));
            path.setNewPath(playerPath, false);
            return path;
        }
        return null;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    private void detectCollisionShapes(ColoredPath inner, ColoredPath outer) {
        if(!isColliding && inner.contains(position)){
            onCollisionEnterColoredShape();
            isColliding = true;
            //System.out.println("newShape: ");
        }
        if(isColliding && outer.contains(position)){
            onCollisionExitColoredShape();
            isColliding = false;
        }
    }

    private void onCollisionExitColoredShape(){
        addAdjustedPoint(direction);
        ShapeContainer.getInstance().splitInnerShape(points);
        isDrawingLines = false;
        resetPlayerStats();
    }

    private void onCollisionEnterColoredShape(){
        resetPlayerStats();
        isDrawingLines = true;
        addAdjustedPoint(direction);
        //calculateAngularSum(direction);
        startDirectionAfterCol = direction;
    }

    private void addPoint(Point position) {
        points.add(new Point(position));
        System.out.println(angularSum);
    }

    private void addAdjustedPoint(int direction){
        int ax = 0;
        int ay = 0;
        if(direction == LEFT) ax++;
        if(direction == UP) ay++;
        points.add(new Point((int) this.x + ax, (int) this.y + ay));
    }

    private void clearPoints() {
        points.clear();
        path.reset();
    }

    private void move(){
        if(lastKeyPressed == -1){
            if(direction != -1) lastDirection = direction;
            direction = -1;
        }
        else {
            if(direction == -1 && lastDirection == -1){
                lastDirection = lastKeyPressed;
                direction = lastKeyPressed;
            }
            else if(direction == -1 && !isOpositeDirection(lastDirection, lastKeyPressed)){
                direction = lastKeyPressed;
                calculateAngularSum(direction);
                addPoint(position);

            }
            else if(direction != -1 && lastKeyPressed != direction && !isOpositeDirection(direction, lastKeyPressed)){
                lastDirection = direction;
                direction = lastKeyPressed;
                calculateAngularSum(direction);
                addPoint(position);
            }
        }
        adjustMovement(direction);
        super.move(dx,dy);
    }

    private boolean isValidKey(int key){
        return (key == LEFT || key == RIGHT || key == UP || key == DOWN );
    }

    private boolean isValidDirection(int key){
        return isValidKey(key);
    }

    private void stopMovement(){
        dx = 0;
        dy = 0;
    }

    private void adjustMovement(int direction) {
        stopMovement();
        if(direction == LEFT) dx = -moveSpeed;
        if(direction == RIGHT) dx = moveSpeed;
        if(direction == UP) dy = -moveSpeed;
        if(direction == DOWN) dy = moveSpeed;
    }

    private void calculateAngularSum(int direction) {
        int keyLeft = -1;
        int keyRight = -1;
        int keyUp = -1;
        int keyDown = -1;
        switch(startDirectionAfterCol){
            case LEFT :
                keyLeft = LEFT;
                keyRight = RIGHT;
                keyUp = UP;
                keyDown = DOWN;
                break;
            case RIGHT :
                keyLeft = DOWN;
                keyRight = UP;
                keyUp = RIGHT;
                keyDown = LEFT;
                break;
            case UP :
                keyLeft = UP;
                keyRight = DOWN;
                keyUp = LEFT;
                keyDown = RIGHT;
                break;
            case DOWN :
                keyLeft = LEFT;
                keyRight = RIGHT;
                keyUp = DOWN;
                keyDown = UP;
                break;
        }
        int tmp = 0;
        if(Math.abs(angularSum)%4 == 0) angularSum = 0;

        if(angularSum == 0) {
            if(direction == keyLeft) tmp--;
            if(direction == keyRight) tmp++;
        }
        if(angularSum > 0) {
            if(angularSum == 1){
                if(direction == keyDown) tmp++;
                if(direction == keyUp) tmp--;
            }
            if(angularSum == 2) {
                if(direction == keyLeft) tmp++;
                if(direction == keyRight) tmp--;
            }
            if(angularSum == 3) {
                if(direction == keyDown) tmp--;
                if(direction == keyUp) tmp++;
            }
        }
        if(angularSum < 0) {
            if(angularSum == -1){
                if(direction == keyDown) tmp--;
                if(direction == keyUp) tmp++;
            }
            if(angularSum == -2) {
                if(direction == keyLeft) tmp++;
                if(direction == keyRight) tmp--;
            }
            if(angularSum == -3) {
                if(direction == keyDown) tmp++;
                if(direction == keyUp) tmp--;
            }
        }
        angularSum += tmp;
    }

    private void resetAngularSum(){
        angularSum = 0;
    }

    private boolean isOpositeDirection(int oldDirection, int newDirection){
        return  (oldDirection == LEFT && newDirection == RIGHT) || (oldDirection == RIGHT && newDirection == LEFT) ||
        (oldDirection == UP && newDirection == DOWN) || (oldDirection == DOWN && newDirection == UP);
    }

    private boolean isHorizontalDirection(int direction){
        return direction == RIGHT || direction == LEFT;
    }

    private boolean isVerticalDirection(int direction){
        return direction == UP || direction == DOWN;
    }

    private void updatePosition(){
        position.setLocation(x,y);
    }

    private void resetPlayerStats(){
        clearPoints();
        resetAngularSum();
    }

    private void resetDirections(){
        direction = -1;
        lastDirection = -1;
        lastKeyPressed = -1;
    }
}

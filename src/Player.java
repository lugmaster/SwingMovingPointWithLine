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
    private boolean isDrawingLines = true;
    private boolean isFirstMove = true;
    private boolean isMoving = false;

    private int direction;
    private int startDirectionaferStop;
    private int startDirectionAfterCol;
    private int lastKeyPressed;
    private int lastKeyReleased;
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
            isMoving = true;
            lastKeyPressed = key;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(isValidKey(key)){
            if(key == lastKeyPressed){
                isMoving = false;
                lastKeyPressed = -1;
            }
        }
    }

    public void updatePlayer(ColoredPath inner, ColoredPath outer){
        if(isMoving){
            move();
        }
        else stopMovement();
        updatePosition();
        detectCollisionShapes(inner, outer);
    }

    public ColoredPath getPlayerPath(){
        if(isDrawingLines){
            ArrayList<Point> playerPath = new ArrayList<>(points);
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
        resetAngularSum();
        isDrawingLines = false;
        clearPoints();
    }

    private void onCollisionEnterColoredShape(){
        resetAngularSum();
        clearPoints();
        isDrawingLines = true;
        addAdjustedPoint(direction);
    }

    private void addPoint(Point position) {
        points.add(new Point(position));
    }

    private void addAdjustedPoint(int direction){
        int ax = 0;
        int ay = 0;
        if(direction == LEFT) ax++;
        if(direction == UP) ay++;
        points.add(new Point((int) this.x+ax, (int) this.y+ay));
    }

    private void clearPoints() {
        points.clear();
        path.reset();
    }

    private void move(){
        if(isFirstMove){
            isFirstMove = false;
            direction = lastKeyPressed;
            startDirectionaferStop = lastKeyPressed;
            adjustMovement(direction);
        }
        if(!isFirstMove && lastKeyPressed != direction && !isOpositeDirection(lastKeyPressed)){
            direction = lastKeyPressed;
            if(isColliding){
                addPoint(position);
                calculateAngularSum(direction);
            }
            adjustMovement(direction);
        }
        super.move(dx,dy);
    }

    private boolean isValidKey(int key){
        return (key == LEFT || key == RIGHT || key == UP || key == DOWN );
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

    private boolean isOpositeDirection(int newDirection){
        return  (direction == LEFT && newDirection == RIGHT) || (direction == RIGHT && newDirection == LEFT) ||
        (direction == UP && newDirection == DOWN) || (direction == DOWN && newDirection == UP);
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
}

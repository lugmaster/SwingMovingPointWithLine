import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Player extends ColoredEllipse{

    private final int WEST = KeyEvent.VK_A;
    private final int EAST = KeyEvent.VK_D;
    private final int NORTH = KeyEvent.VK_W;
    private final int SOUTH = KeyEvent.VK_S;

    private boolean isColliding = false;
    private boolean isFirstCollision = false;
    private boolean isDrawingLines = false;
    private boolean isFirstMove = true;
    private boolean isMoving = false;

    private int direction = -1;
    private int lastDirection = -1;
    private int startDirectionAfterCol = -2;
    private int lastKeyPressed = -1;
    private int lastKeyReleased = -1;
    private int angularSum = 0;


    private float dx,dy;

    private Point position;
    private Point lastPosition;
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
        detectSelfCollision();
        updatePlayerPath();
        detectCollisionShapes(inner, outer);
    }

    public ColoredPath getPlayerPath(){
        if(isDrawingLines){
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
        startDirectionAfterCol = -2;
        resetPlayerStats();
    }

    private void onCollisionEnterColoredShape(){
        resetPlayerStats();
        isDrawingLines = true;
        addAdjustedPoint(direction);
        startDirectionAfterCol = direction;
        calculateAngularSum(direction);
    }

    private void addPoint(Point position) {
        points.add(new Point(position));
        //System.out.println(angularSum);
    }

    private void addAdjustedPoint(int direction){
        int ax = 0;
        int ay = 0;
        if(direction == WEST) ax++;
        if(direction == NORTH) ay++;
        points.add(new Point((int) this.x + ax, (int) this.y + ay));
    }

    private void clearPoints() {
        points.clear();
        path.reset();
    }

    private boolean positionHasChanged(){
        return !position.equals(lastPosition);
    }

    private void updatePlayerPath(){
        if(positionHasChanged()){
            ArrayList<Point> playerPath = new ArrayList<>(points);
            for (Point point : playerPath) {
                System.out.println("Ppath: " + point);
            }
            playerPath.add(new Point(position));
            path.setNewPath(playerPath, false);
        }
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
        //System.out.println(direction);
        //System.out.println(startDirectionAfterCol + "\n");
        adjustMovement(direction);
        super.move(dx,dy);
    }

    private boolean isValidKey(int key){
        return (key == WEST || key == EAST || key == NORTH || key == SOUTH);
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
        if(direction == WEST) dx = -moveSpeed;
        if(direction == EAST) dx = moveSpeed;
        if(direction == NORTH) dy = -moveSpeed;
        if(direction == SOUTH) dy = moveSpeed;
    }

    private void calculateAngularSum(int direction) {
        int keyLeft = -2;
        int keyRight = -2;
        int keyUp = -2;
        int keyDown = -2;
        switch(startDirectionAfterCol){
            case NORTH :
                keyLeft = WEST;
                keyRight = EAST;
                keyUp = NORTH;
                keyDown = SOUTH;
                break;
            case WEST :
                keyLeft = SOUTH;
                keyRight = NORTH;
                keyUp = WEST;
                keyDown = EAST;
                break;
            case EAST :
                keyLeft = NORTH;
                keyRight = SOUTH;
                keyUp = EAST;
                keyDown = WEST;
                break;
            case SOUTH :
                keyLeft = EAST;
                keyRight = WEST;
                keyUp = SOUTH;
                keyDown = NORTH;
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
        return  (oldDirection == WEST && newDirection == EAST) || (oldDirection == EAST && newDirection == WEST) ||
        (oldDirection == NORTH && newDirection == SOUTH) || (oldDirection == SOUTH && newDirection == NORTH);
    }

    private void updatePosition(){
        position.setLocation(x,y);
    }

    private void resetPlayerStats(){
        clearPoints();
        resetAngularSum();
        //resetDirections();
    }

    private void resetDirections(){
        direction = -1;
        lastDirection = -1;
        lastKeyPressed = -1;
    }

    private void detectSelfCollision(){
        boolean foundSelfCollision = false;
        if(points.size() >= 2){
            for (int i = 0; i < points.size(); i++) {
                if(points.isEmpty() || i < 0)return;
                if(foundSelfCollision){
                    points.remove(i);
                    i--;
                }
                else{
                    if(i+1 < points.size()){
                        Point p1 = points.get(i);
                        Point p2 = points.get(i+1);
                        if(ColoredPath.pointIsInLine(p1,p2,position)){
                            foundSelfCollision = true;
                            System.out.println("found selfcoll: \n" + p1 + "\n" + p2);
                        }
                    }
                }
            }
        }
        if(foundSelfCollision){
            for (Point point : points) {
                System.out.println("points:" + point);
            }
            System.out.println("pos: " + position);
            addPoint(position);
        }
    }

}

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public final class Player extends ColoredEllipse{

    private final int WEST = KeyEvent.VK_A;
    private final int EAST = KeyEvent.VK_D;
    private final int NORTH = KeyEvent.VK_W;
    private final int SOUTH = KeyEvent.VK_S;

    private boolean isColliding = false;
    private boolean isDrawingLines = false;
    private boolean isVulnerable = false;

    private int direction = -1;
    private int lastDirection = -1;
    private int lastKeyPressed = -1;

    private Point position;
    private Point lastPosition;

    private ColoredPath path;

    private ArrayList<Point> points;


    public Player(float x, float y, float width, float height, float moveSpeed, Color color){
        super(x, y, width, height, moveSpeed, color);
        position = new Point((int)x,(int)y);
        lastPosition = new Point(position);
        points = new ArrayList<>();
        points.add(new Point(position));
        path = new ColoredPath(color);
    }

    public ColoredPath getPlayerPath(){
        if(isDrawingLines){
            return path;
        }
        return null;
    }

    //Debug INFO remove before FINISH
        /*
        if(key == KeyEvent.VK_ENTER){
            for (Point point : points) {
                System.out.println("NOW:" + point);
            }
            System.out.println("NOWPOS:" + position);
            System.out.println("\n");
            updatePlayerPath();
        }*/

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

    public void update(ColoredPath inner, ColoredPath outer){
        move();
        updatePosition();
        detectSelfCollision();
        updatePlayerPath();
        detectCollisionShapes(inner, outer);
    }

    public boolean isVulnerable(){
        return isVulnerable;
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
                if(direction != lastDirection){
                    addPoint(position);
                }


            }
            else if(direction != -1 && lastKeyPressed != direction && !isOpositeDirection(direction, lastKeyPressed)){
                lastDirection = direction;
                direction = lastKeyPressed;
                addPoint(position);
            }
        }
        float[] deltaPos = adjustMovement(direction);
        super.move(deltaPos[0],deltaPos[1]);
    }

    private void updatePosition(){
        position.setLocation(x,y);
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
                        if(GameLogicsManager.pointIsInLine(p1,p2,position)){
                            foundSelfCollision = true;
                        }
                    }
                }
            }
        }
        if(foundSelfCollision){
            addPoint(position);
        }
    }

    private void updatePlayerPath(){
        if(positionHasChanged()){
            lastPosition.setLocation(position);
            ArrayList<Point> playerPath = new ArrayList<>(points);
            playerPath.add(new Point(position));
            path.setNewPath(playerPath, false);
        }
    }



    private void detectCollisionShapes(ColoredPath inner, ColoredPath outer) {
        if(!isColliding && inner.contains(position)){
            onCollisionEnterColoredShape();
            isColliding = true;
        }
        if(isColliding && outer.contains(position)){
            onCollisionExitColoredShape();
            isColliding = false;
        }
    }

    private void onCollisionExitColoredShape(){
        addAdjustedPoint(direction);
        GameLogicsManager.getInstance().splitInnerShape(points);
        isDrawingLines = false;
        isVulnerable = false;
        clearPoints();
    }

    private void onCollisionEnterColoredShape(){
        clearPoints();
        isDrawingLines = true;
        isVulnerable = true;
        addAdjustedPoint(direction);
    }

    private void addPoint(Point position) {
        points.add(new Point(position));
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

    private boolean isValidKey(int key){
        return (key == WEST || key == EAST || key == NORTH || key == SOUTH);
    }

    private float[] adjustMovement(int direction) {
        float[] deltaPos = {0,0};
        if(direction == WEST) deltaPos[0] = -moveSpeed;
        if(direction == EAST) deltaPos[0] = moveSpeed;
        if(direction == NORTH) deltaPos[1] = -moveSpeed;
        if(direction == SOUTH) deltaPos[1] = moveSpeed;
        return deltaPos;
    }

    private boolean isOpositeDirection(int oldDirection, int newDirection){
        return  (oldDirection == WEST && newDirection == EAST) || (oldDirection == EAST && newDirection == WEST) ||
        (oldDirection == NORTH && newDirection == SOUTH) || (oldDirection == SOUTH && newDirection == NORTH);
    }

}

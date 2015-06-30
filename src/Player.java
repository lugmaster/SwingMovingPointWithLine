import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Player extends MoveableEllipse implements MoveableShape, LineDrawingShape{

    private boolean isColliding = false;
    private boolean isDrawingLines = true;
    private boolean isFirstMove = true;
    private boolean beforeFirstCollision = true;

    private int direction;
    private int startDirection;
    private int angularSum = 0;

    private float dx,dy;

    private Point position;
    private ColoredPath path;

    private ArrayList<Point> points;



    public Player(float x, float y, float width, float height, float moveSpeed){
        this(x, y, width, height, moveSpeed, Color.BLUE);
    }

    public Player(float x, float y, float width, float height, float moveSpeed, Color color){
        super(x, y, width, height, moveSpeed, color);
        position = new Point((int)x,(int)y);
        points = new ArrayList<>();
        points.add(createNewPoint());
        path = new ColoredPath(color);
        //path = getPlayerPath();
    }

    private void onCollisionExitColoredShape(){
        //System.out.println("Exit:" + getPosition());
        addAdjustedPoint(direction);
        resetAngularSum();
        //createNewShape();
        ShapeContainer.getInstance().splitInnerShape(points);
        clearPoints();

    }

    private void createNewShape() {
        for (Point point : points) {
            System.out.println("newShape: " + point);
        }
        ShapeContainer.getInstance().createColoredPath(points);
    }

    private void onCollisionEnterColoredShape(){
        resetAngularSum();
        clearPoints();
        addAdjustedPoint(direction);
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

    private void addPoint() {
        points.add(createNewPoint());
    }

    private void addAdjustedPoint(int direction){
        if(direction == KeyEvent.VK_A) points.add(createPointOffset(1,0));
        if(direction == KeyEvent.VK_D) points.add(createPointOffset(0,0));
        if(direction == KeyEvent.VK_W) points.add(createPointOffset(0,1));
        if(direction == KeyEvent.VK_S) points.add(createPointOffset(0,0));
    }

    private void addPointOnCollEnter(int direction){
        if(direction == KeyEvent.VK_A) points.add(createPointOffset(2,0));
        if(direction == KeyEvent.VK_D) points.add(createPointOffset(-2,0));
        if(direction == KeyEvent.VK_W) points.add(createPointOffset(0,2));
        if(direction == KeyEvent.VK_S) points.add(createPointOffset(0,-2));
    }

    private void addPointOnCollExit(int direction){
        if(direction == KeyEvent.VK_A) points.add(createPointOffset(2,0));
        if(direction == KeyEvent.VK_D) points.add(createPointOffset(-2,0));
        if(direction == KeyEvent.VK_W) points.add(createPointOffset(0,2));
        if(direction == KeyEvent.VK_S) points.add(createPointOffset(0,-2));
    }


    private Point createNewPoint(){
        return new Point((int)this.x, (int)this.y);
    }

    private Point createPointOffset(int x, int y){
        Point p = new Point((int)this.x + x, (int)this.y + y);
        System.out.println(p);
        return p;
    }
    public ArrayList<Point> getPoints() {
        return points;
    }

    private void clearPoints() {
        points.clear();
        path.reset();
    }

    public void updatePlayer(ColoredPath inner, ColoredPath outer){
        move();
        updatePosition();
        detectCollisionShapes(inner, outer);
    }

    private void move(){
       super.move(dx,dy);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(isValidKey(key)) {
            if(isFirstMove){
                isFirstMove = false;
                direction = key;
                startDirection = key;
            }
            if(key != direction && !isOpositeDirection(key)){
                direction = key;
                if(isColliding) addPoint();
                calculateAngularSum(direction);
            }
            adjustMovement();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == direction){
            //stopMovement();
        }
    }

    private boolean isValidKey(int key){
        return (key == KeyEvent.VK_A || key == KeyEvent.VK_D || key == KeyEvent.VK_W || key == KeyEvent.VK_S );
    }

    private void stopMovement(){
        dx = 0;
        dy = 0;
    }

    private void adjustMovement() {
        stopMovement();
        if(direction == KeyEvent.VK_A) dx = -moveSpeed;
        if(direction == KeyEvent.VK_D) dx = moveSpeed;
        if(direction == KeyEvent.VK_W) dy = -moveSpeed;
        if(direction == KeyEvent.VK_S) dy = moveSpeed;
    }

    private void calculateAngularSum(int direction) {
        int keyLeft = -1;
        int keyRight = -1;
        int keyUp = -1;
        int keyDown = -1;
        switch(startDirection){
            case KeyEvent.VK_W :
                keyLeft = KeyEvent.VK_A;
                keyRight = KeyEvent.VK_D;
                keyUp = KeyEvent.VK_W;
                keyDown = KeyEvent.VK_S;
                break;
            case KeyEvent.VK_A :
                keyLeft = KeyEvent.VK_S;
                keyRight = KeyEvent.VK_W;
                keyUp = KeyEvent.VK_A;
                keyDown = KeyEvent.VK_D;
                break;
            case KeyEvent.VK_D :
                keyLeft = KeyEvent.VK_W;
                keyRight = KeyEvent.VK_S;
                keyUp = KeyEvent.VK_D;
                keyDown = KeyEvent.VK_A;
                break;
            case KeyEvent.VK_S :
                keyLeft = KeyEvent.VK_D;
                keyRight = KeyEvent.VK_A;
                keyUp = KeyEvent.VK_S;
                keyDown = KeyEvent.VK_W;
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
        return  (direction == KeyEvent.VK_A && newDirection == KeyEvent.VK_D) || (direction == KeyEvent.VK_D && newDirection == KeyEvent.VK_A) ||
        (direction == KeyEvent.VK_W && newDirection == KeyEvent.VK_S) || (direction == KeyEvent.VK_S && newDirection == KeyEvent.VK_W);
    }

    private boolean isHorizontalDirection(int direction){
        return direction == KeyEvent.VK_A || direction == KeyEvent.VK_D;
    }

    private boolean isVerticalDirection(int direction){
        return direction == KeyEvent.VK_W || direction == KeyEvent.VK_S;
    }

    private void updatePosition(){
        position.setLocation(x,y);
    }

    public Point getPosition(){
        updatePosition();
        return position;
    }

    public ColoredPath getPlayerPath(){
        ArrayList<Point> playerPath = new ArrayList<>(points);
        playerPath.add(createNewPoint());
        path.setNewPath(playerPath, false);
        return path;
    }
}

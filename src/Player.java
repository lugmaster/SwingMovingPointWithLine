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

    private Point2D.Float position;
    private ColoredPath path;

    private ArrayList<Point2D.Float> points;



    public Player(float x, float y, float width, float height, float moveSpeed){
        this(x, y, width, height, moveSpeed, Color.BLUE);
    }

    public Player(float x, float y, float width, float height, float moveSpeed, Color color){
        super(x, y, width, height, moveSpeed, color);
        position = new Point2D.Float(x,y);
        points = new ArrayList<>();
        points.add(createNewPoint());
        path = new ColoredPath(color);
        path.moveTo(0,0);
    }

    private void onCollisionExitColoredShape(){
        addPoint();
        resetAngularSum();
        createNewShape();
        clearPoints();
    }

    private void createNewShape() {
        ShapeContainer.getInstance().createColoredPath(points);
    }

    private void onCollisionEnterColoredShape(){
        resetAngularSum();
        Point2D.Float p2d = points.get(points.size()-1);
        clearPoints();
        addPoint(p2d);
        addPoint();
    }



    private void detectCollisionShapes(ColoredPath inner, ColoredPath outer) {
        if(!isColliding && !outer.contains(position)){
            onCollisionEnterColoredShape();
            isColliding = true;
            for (Point2D.Float point : points) {
                System.out.println(point);
            }
        }
        if(isColliding && !inner.contains(position)){
            onCollisionExitColoredShape();
            isColliding = false;
        }
    }

    private void addPoint() {
        points.add(createNewPoint());
    }

    private void addPoint(Point2D.Float p2d) {
        points.add(p2d);
    }

    private Point2D.Float createNewPoint(){
        return new Point2D.Float(this.x, this.y);
    }

    @Override
    public ArrayList<Point2D.Float> getPoints() {
        return points;
    }

    private void clearPoints() {
        points.clear();
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
                addPoint();
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

    public Point2D.Float getPosition(){
        return position;
    }

    public ColoredPath getPlayerPath(){
        if(!points.isEmpty()) path.setNewPath(points,this);
        return path;
    }
}

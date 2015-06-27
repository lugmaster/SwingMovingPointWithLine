import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Stack;

public class Player extends MoveableEllipse implements MoveableShape, LineDrawingShape{

    private boolean isColliding = false;
    private boolean isDrawingLines = false;
    private boolean isfirstMove = true;
    private int onCollisionExitDirection;
    private int direction;
    private int lastdirection;
    private ArrayList<Point> lines = new ArrayList<>();
    private Stack<java.lang.Float[]> stack = new Stack<>();
    private float dx,dy;

    public Player(float x, float y, float width, float height, float moveSpeed){
        this(x, y, width, height, moveSpeed, Color.GREEN);
    }

    public Player(float x, float y, float width, float height, float moveSpeed, Color color){
        super(x, y, width, height, moveSpeed, color);
    }

    public void onCollisionEnterColoredShape(){
        pushPoint();
        System.out.println("cEnter");
        isDrawingLines = false;
        clearLines();
        if(!stack.isEmpty()) {
            createNewShape();
            stack.clear();
        }

    }

    private void createNewShape() {
        if(onCollisionExitDirection != direction) {
            Path2D.Float path2D = new Path2D.Float();
            for (int i = 0; i < stack.size(); i++) {
                java.lang.Float[] floats = stack.pop();
                if(i == 0) {
                    path2D.moveTo(floats[0].intValue(), floats[1].intValue());
                }
                else path2D.lineTo(floats[0].intValue(), floats[1].intValue());
            }
            path2D.closePath();
            ShapeContainer.getInstance().createPath2D(path2D);
            //not relevant ATM
            /*float x1 = roundfloat(position[0]);
            float x2 = roundfloat(this.x);
            float y1 = roundfloat(position[1]);
            float y2 = roundfloat(this.y);
            if(x1 > x2) {
                float tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            if(y1 > y2) {
                float tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            ShapeContainer.getInstance().createRectangle(x1,y1,(x2-x1),(y2-y1));*/
        }
    }

    private static float roundfloat(float f){
        return Math.round(f*100)/100;
    }

    public void onCollisionExitColoredShape(){
        onCollisionExitDirection = direction;
        System.out.println("cEX");
        pushPoint();
        isDrawingLines = true;
    }

    public void onCollisionEnterMoveableShape(){
        //simple
    }

    private void pushPoint(){
        stack.push(new java.lang.Float[]{this.x, this.y});
        System.out.println("\n #######################################");
        for(java.lang.Float[] floats : stack){
            System.out.println("X:" + floats[0] + ",Y:" +floats[1]);
        }
        //stack.push(new Integer[]{((int) (this.x > (Board.WIDTH/2) ? this.x-5:this.x+5) ), ((int) this.y)});
    }

    public void detectCollisionShapes(ArrayList<ColoredShape> coloredShapes) {
        for(int i = 0; i < coloredShapes.size(); i++) {
            if (this != coloredShapes.get(i) && coloredShapes.get(i).intersects(this.getBounds2D())) {
                if(!isColliding) {
                    isColliding = true;
                    onCollisionEnterColoredShape();
                }
                return;
            }
        }
        if(isColliding) {
            onCollisionExitColoredShape();
        }
        isColliding = false;
    }

    @Override
    public void addLines() {
        if(isDrawingLines) {
            lines.add(new Point((int) this.x, (int) this.y));
        }
    }

    @Override
    public ArrayList<Point> getLines() {
        return lines;
    }

    @Override
    public void clearLines() {
        lines.clear();
    }

    public void updatePlayer(ArrayList<ColoredShape> coloredShapes, ArrayList<MoveableShape> moveableShapes){
        move();
        detectCollisionShapes(coloredShapes);
        addLines();
    }

    public void move(){
       super.move(dx,dy);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(isValidKey(key)) {
            if(key != direction){
                pushPoint();
            }
            direction = key;
            adjustMovement();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == direction){
            stopMovement();
        }
    }

    private boolean isValidKey(int key){
        return (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) || (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) ||
                (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) || (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN);
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
}

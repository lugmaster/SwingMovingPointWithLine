import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;

public class Player extends MoveableEllipse implements MoveableShape, LineDrawingShape{

    private boolean isColliding = false;
    private boolean isDrawingLines = false;
    private boolean isfirstMove = true;
    private int direction;
    private ArrayList<Point> lines = new ArrayList<>();
    private Stack<Integer[]> stack = new Stack<>();
    private float dx,dy;

    public Player(float x, float y, float width, float height, float moveSpeed){
        this(x, y, width, height, moveSpeed, Color.GREEN);
    }

    public Player(float x, float y, float width, float height, float moveSpeed, Color color){
        super(x, y, width, height, moveSpeed, color);
    }

    public void onCollisionEnterColoredShape(){
        System.out.println("cEnter");
        isDrawingLines = false;
        clearLines();
        if(!stack.isEmpty()) {
            createNewShape(stack.pop());
        }
    }

    private void createNewShape(Integer[] position) {
        int x1 = position[0]+1;
        int x2 = (int)this.x;
        int y1 = position[1];
        int y2 = (int)this.y;
        if(x1 > x2) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if(y1 > y2) {
            int tmp = y1;
            y1 = y2;
            y2 = tmp;
        }
        ShapeContainer.getInstance().createRectangle(x1,y1,(x2-x1),(y2-y1));
    }

    public void onCollisionExitColoredShape(){
        System.out.println("cEX");
        stack.push(new Integer[]{((int) (this.x > (Board.WIDTH/2) ? this.x-5:this.x+5) ), ((int) this.y)});
        isDrawingLines = true;
    }

    public void onCollisionEnterMoveableShape(){
        //simple
    }

    public void detectCollisionShapes(ArrayList<ColoredShape> coloredShapes) {
        for(int i = 0; i < coloredShapes.size(); i++) {
            if (this != coloredShapes.get(i) && coloredShapes.get(i).intersects(this.getBounds())) {
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

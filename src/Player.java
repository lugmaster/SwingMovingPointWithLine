import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;

public class Player extends MoveableEllipse implements MoveableShape, LineDrawingShape{

    LineDrawing lineDrawing = LineDrawing.IsNotDrawingLines;
    private boolean isColliding = true;
    private ArrayList<Point> lines = new ArrayList<>();
    private Stack<Integer[]> stack = new Stack<>();
    private int[] onCollisionEnter;
    private int[] getOnCollisionExit;

    public Player(int x, int y, int width, int height, float moveSpeed){
        this(x, y, width, height, moveSpeed, Color.blue);
    }

    public Player(int x, int y, int width, int height, float moveSpeed, Color color){
        super(x, y, width, height, moveSpeed, color);
    }

    private enum LineDrawing{
        IsDrawingLines,
        IsNotDrawingLines
    }

    public void keyPressed(KeyEvent e) {

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            dx = -moveSpeed;
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            dx = moveSpeed;
        }

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            dy = -moveSpeed;
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            dy = moveSpeed;
        }
    }

    public void keyReleased(KeyEvent e) {


        int key = e.getKeyCode();

        if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
            dy = 0;
        }

        if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
    }

    public void onCollisionEnterColoredShape(int x1, int y1){
        isColliding = true;
        if(!stack.isEmpty()){
            Integer[] integers = stack.pop();
            int x2 = integers[0];
            int y2 = integers[1];
            int swap;
            if(x1 < x2) {
                swap = x2;
                x1 = x2;
                x2 = x1;
            }
            if(y1 < y2){
                swap = y2;
                y1 = y2;
                y2 = y1;
            }
            int xfinal = x2;
            int yfinal = y2;
            int w = x1-x2;
            int h = y1-y2;
            System.out.println("P: (" + xfinal + ", " + yfinal +") w:" + w +" h:" + h);
            ShapeContainer.getInstance().addColoredShape(new ColouredRectangle(xfinal, yfinal, w, h));
        }
        disableLineDrawing();
        clearLines();
    }

    public void onCollisionExitColoredShape(int x, int y){
        enableLineDrawing();
        Integer[] integers = {x,y};
        stack.push(integers);
        isColliding = false;
    }

    public void onCollisionEnterMoveableShape(){

    }

    public void onCollisionExitMoveableShape(){

    }

    public void detectCollisionShapes(ArrayList<ColoredShape> coloredShapes) {
        for(int i = 0; i < coloredShapes.size(); i++) {
            if (this != coloredShapes.get(i) && coloredShapes.get(i).intersects(this.getBounds())) {
                if(onCollisionEnter == null) {
                    onCollisionEnter = new int[] {(int) this.x, (int) this.y};
                    onCollisionEnterColoredShape(onCollisionEnter[0], onCollisionEnter[1]);
                }
                return;
            }
        }
        onCollisionEnter = null;
    }

    @Override
    public void addLines() {
        if(isDrawingLines()) {
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

    private boolean isDrawingLines(){
        return lineDrawing == LineDrawing.IsDrawingLines;
    }

    private void enableLineDrawing(){
        lineDrawing = LineDrawing.IsDrawingLines;
    }

    private void disableLineDrawing(){
        lineDrawing = LineDrawing.IsNotDrawingLines;
    }


}

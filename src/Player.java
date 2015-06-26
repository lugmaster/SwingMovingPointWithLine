import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;

public class Player extends MoveableEllipse implements MoveableShape, LineDrawingShape{

    LineDrawing lineDrawing = LineDrawing.IsNotDrawingLines;
    private boolean isColliding = true;
    private ArrayList<Point> lines = new ArrayList<>();
    private Stack<Integer[]> stack = new Stack<>();


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
        if(!stack.isEmpty()){
            Integer[] integers = stack.pop();
            int x2 = integers[0];
            int y2 = integers[1];
            int swap;
            /*if(x1 < x2 && y1 < y2) {
                //trivial
            }*/
            if(x1 < x2 && y1 > y2) {
                //swap y
                swap =y1;
                y1 = y2;
                y2 = y1;
            }
            if(x1 > x2 && y1 < y2) {
                //swap x,y
                swap =x1;
                x1 = x2;
                x2 = x1;


            }
            if(x1 > x2 && y1 > y2) {
                //swap x
                swap =x1;
                x1 = x2;
                x2 = x1;

                swap =y1;
                y1 = y2;
                y2 = y1;
            }
            ShapeContainer.getInstance().addColoredShape(new ColouredRectangle(x1,y1, Math.abs(x2-x1), Math.abs(y2 - y1)));
        }
        disableLineDrawing();
        clearLines();
    }

    public void onCollisionExitColoredShape(int x, int y){
        enableLineDrawing();
        Integer[] integers = {x,y};
        stack.push(integers);
    }

    public void onCollisionEnterMoveableShape(){

    }

    public void onCollisionExitMoveableShape(){

    }

    public void detectCollision() {
        if (ShapeContainer.getInstance().detectCollisionPlayers(this)) {
            if (!isColliding) System.out.println("DEAD");
            //gameOver
        }
        if (!isColliding && ShapeContainer.getInstance().detectCollisionShapes(this)) {
            isColliding = true;
            onCollisionEnterColoredShape((int) this.x, (int) this.y);
            System.out.println("COL ON");
        }
        if (isColliding && !ShapeContainer.getInstance().detectCollisionShapes(this)) {
            isColliding = false;
            onCollisionExitColoredShape((int) this.x, (int) this.y);
            System.out.println("COL OFF");
        }
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

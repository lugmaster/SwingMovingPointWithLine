import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Player extends MoveableEllipse implements MoveableShape, LineDrawingShape{

    LineDrawing lineDrawing = LineDrawing.IsNotDrawingLines;
    private boolean isColliding = false;
    private ArrayList<Point> lines = new ArrayList<>();


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

    public void onCollisionEnterColoredShape(int x, int y){
        if(isDrawingLines()) {
            disableLineDrawing();
        }
        if(!isDrawingLines()) {
            enableLineDrawing();
            add(x,y);
        }
    }

    public void onCollisionExitColoredShape(int x, int y){

    }

    public void onCollisionEnterMoveableShape(){

    }

    public void onCollisionExitMoveableShape(){

    }

    public void detectCollision(){
      ShapeContainer.getInstance().detectCollisionShapes(this);

        /*if(ShapeContainer.getInstance().detectCollisionPlayers(this)){
            if(!isColliding) System.out.println("DEAD");
            //gameOver
        }
        if(!isColliding && ShapeContainer.getInstance().detectCollisionShapes(this)){
            isColliding = true;
            onCollisionEnterColoredShape((int) this.x, (int) this.y);
            System.out.println("COL ON");
        }
        if(isColliding && !ShapeContainer.getInstance().detectCollisionShapes(this)) {
            isColliding = false;
            onCollisionExitColoredShape((int) this.x, (int) this.y);
            System.out.println("COL OFF");
        }*/

    }

    private void add(double x, double y) {
        lines.add(new Point((int) x, (int) y));
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

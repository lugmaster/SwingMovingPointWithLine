import java.awt.*;

public class AIPlayer extends ColoredEllipse{

    private Point position;
    private boolean gameIsRunning = true;

    public AIPlayer (int x, int y, int width, int height, float moveSpeed, Color color){
        super(x,y,width,height, moveSpeed, color);
        position = new Point(x,y);
    }

    public Point getPosition(){
        return position;
    }

    public void update(){

    }

    public void setGameToFinished(){
        gameIsRunning = false;
    }

}

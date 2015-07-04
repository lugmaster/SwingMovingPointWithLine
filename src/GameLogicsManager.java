import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

public class GameLogicsManager {

    private static GameLogicsManager gameLogicsManager;

    private byte[][] totalAreaAdded;

    private Player player;
    private AIPlayer aiPlayer;

    private ColoredPath innerShape;
    private ColoredPath outerShape;
    private final Area outerShapeTemplate;

    private int totalAreaInPoints = 0;
    private final int winningCondition;

    private GameLogicsManager(){
        winningCondition = 80;
        totalAreaAdded = new byte[Board.WIDTH][Board.HEIGHT];

        player = Initializer.getInstance().getPlayer();
        aiPlayer = Initializer.getInstance().getAiPlayer();

        outerShape = new ColoredPath(Initializer.getInstance().getOuterPoints(), true);
        innerShape = new ColoredPath(Initializer.getInstance().getInnerPoints(), true);
        outerShapeTemplate = new Area(outerShape);

    }

    public static GameLogicsManager getInstance(){
        if(gameLogicsManager == null)
            return new GameLogicsManager();
        return gameLogicsManager;
    }

    public void updateGame(){
        player.update(innerShape, outerShape);
        aiPlayer.update();
        updateTotalAreaAdded();
        if(maxTotalAreaReached()){
            //finish game;
            System.out.println("WON THE FUCKING GAME!");
        }
    }

    private void updateTotalAreaAdded(){
        for (int i = 0; i < totalAreaAdded.length; i++) {
            for(int j = 0; j < totalAreaAdded[i].length; j++){
                if(totalAreaAdded[i][j] != 1 && outerShape.contains(new Point(i,j)) ){
                    totalAreaAdded[i][j] = 1;
                    totalAreaInPoints++;
                }
            }
        }
    }

    private float calculateTotalAreaPercent(){
        float f = totalAreaInPoints/((Board.HEIGHT * Board.WIDTH)/100);
        return (float)(Math.round(f * 100))/100;
    }

    private boolean maxTotalAreaReached(){
        return calculateTotalAreaPercent() >= winningCondition;
    }

    public void splitInnerShape(ArrayList<Point> splitPoints) {
        removeColoredShape(outerShape);
        removeColoredShape(innerShape);
        ColoredPath[] coloredPath = innerShape.splitpath(splitPoints);
        if(coloredPath[0].contains(aiPlayer.getPosition())){
            innerShape = coloredPath[0];
        }
        else innerShape = coloredPath[1];
        addColoredShape(innerShape);
        outerShape = new ColoredPath(outerPoints,true);
        Area a0 = new Area(outerShape);
        Area a1 = new Area(innerShape);
        a0.subtract(a1);
        outerShape = new ColoredPath(a0);
        addColoredShape(outerShape);
    }

}

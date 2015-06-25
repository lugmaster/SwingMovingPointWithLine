import java.awt.*;
import java.util.ArrayList;

public final class ShapeContainer {

    private static ShapeContainer shapeContainer = new ShapeContainer();
    private ArrayList<ColoredShape> coloredShapes = new ArrayList<>();
    private ArrayList<MoveableShape> moveableShapes = new ArrayList<>();
    private Player player;
    private AIPlayer aiPlayer;

    private ShapeContainer() {
        player = new Player(60, 50, 3, 3, 1.5f);

        coloredShapes.add(new ColouredRectangle(0, 0, 10, 10));
        coloredShapes.add(new ColouredRectangle(40, 40, 10, 10));
        coloredShapes.add(new ColouredRectangle(80, 80, 10, 10));
        coloredShapes.add(new ColouredRectangle(100, 100, 50, 50));
        moveableShapes.add(player);
        moveableShapes.add(new AIPlayer(50,50,3,3,1.5f, Color.red));
    }

    public static ShapeContainer getInstance(){
        if(shapeContainer == null)
            return new ShapeContainer();
        return shapeContainer;
    }

    public Player getPlayer(){
        return player;
    }

    public AIPlayer getAiPlayer(){
        return aiPlayer;
    }

    public ArrayList<ColoredShape> getColoredShapes() {
        return coloredShapes;
    }

    public ArrayList<MoveableShape> getMoveableShapes() {
        return moveableShapes;
    }

    public void createPlayer(Player player){
        if(player == null){
            this.player = player;
            addMoveableShape(player);
        }
    }

    public void createAIPlayer(AIPlayer aiPlayer){
        if(player == null){
            this.aiPlayer = aiPlayer;
            addMoveableShape(aiPlayer);
        }
    }

    public void createRectangle(int x, int y, int width, int height) {
       addColoredShape(new ColouredRectangle(x,y,width,height));
    }

    public void addColoredShape(ColoredShape coloredShape){
        coloredShapes.add(coloredShape);
    }

    public void addMoveableShape(MoveableShape moveableShape){
        moveableShapes.add(moveableShape);
    }


    public boolean detectCollisionShapes(MoveableShape moveableShape){
        //System.out.println("Im called here dude!");
        for(ColoredShape coloredShape : coloredShapes) {
            if(moveableShape.intersects(coloredShape.getBounds()));
            return true;
        }
        return false;
    }

    public boolean detectCollisionPlayers(MoveableShape moveableShape){
        for(MoveableShape m : moveableShapes) {
            if(m != moveableShape && m.intersects(moveableShape.getBounds())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Point> getLines(){
            return player.getLines();
    }
}

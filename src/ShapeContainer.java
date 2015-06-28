import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

public final class ShapeContainer {

    private static ShapeContainer shapeContainer = new ShapeContainer();
    private ArrayList<ColoredShape> coloredShapes = new ArrayList<>();
    private ArrayList<MoveableShape> moveableShapes = new ArrayList<>();
    private Player player;
    private AIPlayer aiPlayer;

    private ShapeContainer() {
        player = new Player(192, 150, 4, 4, 2f);

        coloredShapes.add(new ColouredRectangle(0, 0, 10, 210));
        coloredShapes.add(new ColouredRectangle(190, 0, 10, 210));
        coloredShapes.add(new ColouredRectangle(0, 190, 210, 10));
        coloredShapes.add(new ColouredRectangle(0, 0, 210, 10));
        moveableShapes.add(player);
        //moveableShapes.add(new AIPlayer(50,50,3,3,1.0f, Color.red));
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

    public void createRectangle(float x, float y, float width, float height) {
       addColoredShape(new ColouredRectangle(x,y,width,height));
    }

    public void createPolygon(int[] intsX, int[] intsY){
        addColoredShape(new ColoredPolygon(intsX, intsY));
        for (int i = 0; i < intsX.length; i++) {
            System.out.println("X:" + intsX[i] + ", Y:" + intsY[i]);
        }
    }

    public void addColoredShape(ColoredShape coloredShape){
        coloredShapes.add(coloredShape);
    }

    public void addMoveableShape(MoveableShape moveableShape){
        moveableShapes.add(moveableShape);
    }

    public void doGameCycle(){
        updatePlayer();
    }

    private void updatePlayer(){
        getPlayer().updatePlayer(coloredShapes, moveableShapes );
    }

    public ArrayList<Point> getLines(){
            return player.getLines();
    }
}

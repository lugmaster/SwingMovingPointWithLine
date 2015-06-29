import java.awt.*;
import java.awt.geom.Area;
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
        // Player and AI
        player = new Player(192, 150, 4, 4, 2f);
        moveableShapes.add(player);
        moveableShapes.add(new AIPlayer(50,50,3,3,1.0f, Color.red));

        //Inner and Outer Shape
        Path2D.Float p2d = new Path2D.Float();
        p2d.moveTo(0,0);
        p2d.lineTo(200,0);
        p2d.lineTo(200,200);
        p2d.lineTo(0,200);
        p2d.closePath();

        Path2D.Float p2d1 = new Path2D.Float();
        //inner
        p2d1.moveTo(9, 190);
        p2d1.lineTo(9,9);
        p2d1.lineTo(190,9);
        p2d1.lineTo(190,190);
        p2d1.closePath();

        Area a0 = new Area(p2d);
        Area a1 = new Area(p2d1);
        ColoredPath cpath0 = new ColoredPath(a0);
        ColoredPath cpath1 = new ColoredPath(a1);
        addColoredShape(cpath0);
        addColoredShape(cpath1);
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

    public void createColoredPath(ArrayList<Point2D.Float> points) {
        addColoredShape(new ColoredPath(points, RandomColorGenerator.generateRandomColor()));
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

    public ArrayList<Point2D.Float> getLines(){
            return player.getLines();
    }
}

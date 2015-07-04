import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public final class ShapeContainer {

    private static ShapeContainer shapeContainer = new ShapeContainer();

    private ArrayList<ColoredShape> coloredShapes = new ArrayList<>();
    private ArrayList<MoveableShape> moveableShapes = new ArrayList<>();

    private Player player;
    private AIPlayer aiPlayer;

    private ColoredPath innerShape;
    private ColoredPath outerShape;
    private final Area outerShapeTemplate;

    private ShapeContainer() {
        // Player and AI
        player = Initializer.getInstance().getPlayer();
        aiPlayer = Initializer.getInstance().getAiPlayer();
        moveableShapes.add(player);
        moveableShapes.add(aiPlayer);

        outerShape = new ColoredPath(Initializer.getInstance().getOuterPoints(), true);
        innerShape = new ColoredPath(Initializer.getInstance().getInnerPoints(), true);
        outerShapeTemplate = new Area(outerShape);

        Area a0 = new Area(outerShape);
        Area a1 = new Area(innerShape);
        a0.subtract(a1);
        outerShape = new ColoredPath(a0);

        addColoredShape(outerShape);
        addColoredShape(innerShape);
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

    public void createColoredPath(ArrayList<Point> points) {
        addColoredShape(new ColoredPath(points, Color.RED, true));
    }

    public void addColoredShape(ColoredShape coloredShape){
        coloredShapes.add(coloredShape);
    }

    public void removeColoredShape(ColoredShape coloredShape){
        if(coloredShapes.contains(coloredShape)) coloredShapes.remove(coloredShape);
    }

    public void upDateGame(){
        updatePlayer();
    }

    private void updatePlayer(){
        getPlayer().updatePlayer(innerShape, outerShape);
    }

    public void doDrawing(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if(!coloredShapes.isEmpty()) {
            for(ColoredShape coloredShape : coloredShapes){
                g2d.setColor(coloredShape.getColor());
                g2d.draw(coloredShape);
                g2d.fill(coloredShape);
            }
        }
        if(player.getPlayerPath() != null){
            g2d.setColor(player.getColor());
            g2d.draw(player.getPlayerPath());
        }
        if(!moveableShapes.isEmpty()) {
            for (MoveableShape moveableShape : moveableShapes) {
                g2d.setColor(moveableShape.getColor());
                g2d.draw(moveableShape);
                g2d.fill(moveableShape);
            }
        }

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

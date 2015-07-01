import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public final class ShapeContainer {

    private static ShapeContainer shapeContainer = new ShapeContainer();
    private ArrayList<ColoredShape> coloredShapes = new ArrayList<>();
    private ArrayList<MoveableShape> moveableShapes = new ArrayList<>();
    private ArrayList<Point> innerPoints = new ArrayList<>();
    private ArrayList<Point> outerPoints = new ArrayList<>();
    private Player player;
    private AIPlayer aiPlayer;
    private ColoredPath innerShape;
    private ColoredPath outerShape;

    private ShapeContainer() {
        // Player and AI
        player = new Player(194, 150, 3, 3, 1f);
        aiPlayer = new AIPlayer(50,50,3,3,1.0f, Color.red);
        //player = new Player(5, 58, 3, 3, 2f);
        //player = new Player(58, 5, 3, 3, 2f);
        //player = new Player(150, 192, 3, 3, 2f);
        moveableShapes.add(player);
        moveableShapes.add(aiPlayer);
        //outer
        Point p0 = new Point(-3,-3);
        Point p1 = new Point(203,-3);
        Point p2 = new Point(203,203);
        Point p3 = new Point(-3,203);

        //inner
        Point p4 = new Point(9,190);
        Point p5 = new Point(9,9);
        Point p6 = new Point(190,9);
        Point p7 = new Point(190,190);
        outerPoints.add(p0);
        outerPoints.add(p1);
        outerPoints.add(p2);
        outerPoints.add(p3);

        innerPoints.add(p4);
        innerPoints.add(p5);
        innerPoints.add(p6);
        innerPoints.add(p7);

        outerShape = new ColoredPath(outerPoints, true);
        innerShape = new ColoredPath(innerPoints, true);

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

    public void createColoredPath(ArrayList<Point> points) {
        addColoredShape(new ColoredPath(points, Color.RED, true));
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

    public void removeColoredShape(ColoredShape coloredShape){
        if(coloredShapes.contains(coloredShape)) coloredShapes.remove(coloredShape);
    }

    public void doGameCycle(){
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

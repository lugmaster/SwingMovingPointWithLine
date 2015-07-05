import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

public final class ShapeContainer {

    private static ShapeContainer shapeContainer = new ShapeContainer();

    private ArrayList<ColoredShape> coloredShapes = new ArrayList<>();
    private ArrayList<MoveableShape> moveableShapes = new ArrayList<>();

    private Player player;
    private AIPlayer aiPlayer;

    private ShapeContainer() {

    }

    public static ShapeContainer getInstance(){
        if(shapeContainer == null)
            shapeContainer = new ShapeContainer();
        return shapeContainer;
    }

    public void addPlayer(Player player){
        this.player = player;
        moveableShapes.add(player);
    }

    public void addAiPlayer(AIPlayer aiPlayer){
        this.aiPlayer = aiPlayer;
        moveableShapes.add(aiPlayer);
    }




    public void addColoredShape(ColoredShape coloredShape){
        System.out.println("shape added");
        coloredShapes.add(coloredShape);
    }

    public void addColoredShape(ColoredShape coloredShape, int index){
        System.out.println("shape added");
        coloredShapes.add(index, coloredShape);
    }

    public void addMoveableShape(MoveableShape moveableShape){
        moveableShapes.add(moveableShape);
    }

    public void removeColoredShape(ColoredShape coloredShape){
        if(coloredShapes.contains(coloredShape)) coloredShapes.remove(coloredShape);
        System.out.println("shape removed");
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
        if(player!= null && player.getPlayerPath() != null){
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

    public void orderShapes(){
        for (int i = 0; i < coloredShapes.size(); i++) {
            //order 1866
        }
    }
}

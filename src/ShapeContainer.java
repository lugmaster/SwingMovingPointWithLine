import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Lukas Normal on 23.06.2015.
 */
public class ShapeContainer {

    private ArrayList<ColoredShape> coloredShapes = new ArrayList<>();
    private ArrayList<MoveableShape> moveableShapes = new ArrayList<>();
    private Player player;

    public ShapeContainer() {
        player = new Player(60, 50, 3, 3, 1.5f);

        coloredShapes.add(new ColouredRectangle(0, 0, 10, 10));
        coloredShapes.add(new ColouredRectangle(40, 40, 10, 10));
        coloredShapes.add(new ColouredRectangle(80, 80, 10, 10));
        coloredShapes.add(new ColouredRectangle(100, 100, 50, 50));
        moveableShapes.add(player);
        moveableShapes.add(new AIPlayer(50,50,3,3,1.5f, Color.red));
    }

    public Player getPlayer(){
        return player;
    }

    public ArrayList<ColoredShape> getColoredShapes() {
        return coloredShapes;
    }

    public ArrayList<MoveableShape> getMoveableShapes() {
        return moveableShapes;
    }

    public void detectCollisionShapes(){
        for(ColoredShape coloredShape : coloredShapes) {
            for(MoveableShape moveableShape : moveableShapes) {
                if (coloredShape.getColor().equals(moveableShape.getColor())) {
                    if(moveableShape.intersects(coloredShape.getBounds())){
                        System.out.println("COLLISION");
                    }
                }
            }
        }

    }

    public void detectCollisionPlayers(){
        for(MoveableShape m : moveableShapes) {
            for(MoveableShape mS : moveableShapes) {
                if(m != mS && m.intersects(mS.getBounds())) {
                    System.out.println("COLL MOVEABLESHAPES");
                }
            }
        }
    }
}

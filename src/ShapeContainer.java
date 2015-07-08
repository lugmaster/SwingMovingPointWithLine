import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
/*
 * The ShapeContainer holds all drawable Shapes received from the GameLogicsManager.
 * It draws all the shapes each updateCycle.
 * If the game is lost or won, it draws instead a Message
 */
public final class ShapeContainer {

    private static ShapeContainer shapeContainer = new ShapeContainer();

    //a list containing all drawable shapes
    private ArrayList<ColoredShape> coloredShapes = new ArrayList<>();

    private ShapeContainer(){
        setNewShapes();
    }

    public static ShapeContainer getInstance(){
        if(shapeContainer == null)
            shapeContainer = new ShapeContainer();
        return shapeContainer;
    }

    /*
     * The Graphics object is passed from the Board Class
     */
    public void doDrawing(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        //ShapeDrawing
        if(!coloredShapes.isEmpty()) {
            for(ColoredShape coloredShape : coloredShapes){
                g2d.setColor(coloredShape.getColor());
                g2d.draw(coloredShape);
                g2d.fill(coloredShape);
                //drawing player line
                if(coloredShape instanceof Player){
                    Player player = (Player) coloredShape;
                    if(player!= null && player.getPlayerPath() != null){
                        g2d.setColor(RandomColorGenerator.generateRandomColor());
                        g2d.draw(player.getPlayerPath());
                    }
                }
            }
        }
        //Area left until to cut from the inner shape
        g.setColor(Color.WHITE);
        g.drawString("Area left: " + GameLogicsManager.getInstance().getAreaLeftPercent(), 5, 15);

        //Win or lost drawing
        if(GameLogicsManager.getInstance().gameIsLost()){
            drawScreenMessage(g, "Game Over");
        }
        if(GameLogicsManager.getInstance().gameIsWon()){
            drawScreenMessage(g, "Game Won");
        }
    }

    /*
     * Receives a graphics object and a message from the doDrawing method
     */
    private void drawScreenMessage(Graphics g, String msg) {
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = g.getFontMetrics(small);

        g.setColor(RandomColorGenerator.generateRandomColor());
        g.setFont(small);
        g.drawString(msg, (Board.WIDTH - fm.stringWidth(msg)) / 2, Board.HEIGHT/ 2);
    }

    /*
     * clears all old Shapes and takes the new ones from GameLogisManaer
     */
    private void setNewShapes(){
        coloredShapes.clear();

        //add inner and outer shape and already filled rectangles
        coloredShapes.add(GameLogicsManager.getInstance().getOuterShape());
        coloredShapes.add(GameLogicsManager.getInstance().getInnerShape());
        for (ColoredShape coloredShape : GameLogicsManager.getInstance().coloredShapes()) {
            this.coloredShapes.add(coloredShape);
        }

        //add Player and AiPlayer
        coloredShapes.add(GameLogicsManager.getInstance().getPlayer());
        coloredShapes.add(GameLogicsManager.getInstance().getAiPlayer());
    }

    public void update(){
        setNewShapes();
    }
}

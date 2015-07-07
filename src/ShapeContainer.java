import com.sun.corba.se.impl.orbutil.graph.Graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;

public final class ShapeContainer {

    private static ShapeContainer shapeContainer = new ShapeContainer();

    private ArrayList<ColoredShape> coloredShapes = new ArrayList<>();
    private ArrayList<MoveableShape> moveableShapes = new ArrayList<>();

    private Player player;
    private AIPlayer aiPlayer;

    private ShapeContainer() {
        player = Initializer.getInstance().getPlayer();
       aiPlayer = Initializer.getInstance().getAiPlayer();
    }

    public static ShapeContainer getInstance(){
        if(shapeContainer == null)
            shapeContainer = new ShapeContainer();
        return shapeContainer;
    }

    public void addPlayer(Player player){
        this.player = player;
        moveableShapes.add(player);
        moveableShapes.add(aiPlayer);
    }

    public void addColoredShape(ColoredShape coloredShape){
        coloredShapes.add(coloredShape);
    }

    public void removeColoredShape(ColoredShape coloredShape){
        if(coloredShapes.contains(coloredShape)) coloredShapes.remove(coloredShape);
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
            g2d.setColor(RandomColorGenerator.generateRandomColor());
            g2d.draw(player.getPlayerPath());
        }
        if(!moveableShapes.isEmpty()) {
            for (MoveableShape moveableShape : moveableShapes) {
                g2d.setColor(moveableShape.getColor());
                g2d.draw(moveableShape);
                g2d.fill(moveableShape);
            }
        }
        g.setColor(Color.WHITE);
        g.drawString("Area left: " + GameLogicsManager.getInstance().getAreaLeft(), 5, 15);
        if(GameLogicsManager.getInstance().gameLost()){
            drawScreenMessage(g, "Game Over");
        }
        if(GameLogicsManager.getInstance().gameWon()){
            drawScreenMessage(g, "Game Won");
        }
    }

    private void drawScreenMessage(Graphics g, String msg) {
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = g.getFontMetrics(small);

        g.setColor(RandomColorGenerator.generateRandomColor());
        g.setFont(small);
        g.drawString(msg, (Board.WIDTH - fm.stringWidth(msg)) / 2, Board.HEIGHT/ 2);
    }

    private void removeOldShapes(){
        coloredShapes.clear();
    }

    private void setNewShapes(){
        coloredShapes.add(GameLogicsManager.getInstance().getOuterShape());
        coloredShapes.add(GameLogicsManager.getInstance().getInnerShape());
        for (ColoredShape coloredShape : GameLogicsManager.getInstance().coloredShapes()) {
            this.coloredShapes.add(coloredShape);
        }
    }

    public void update(){
        removeOldShapes();
        setNewShapes();
    }
}

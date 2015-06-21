import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;
 */

public class Board extends JPanel implements ActionListener{

    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private final int DELAY = 5;
    private Timer timer;
    private ArrayList<ColoredShape> coloredShapes = new ArrayList<>();
    private ArrayList<MoveableShape> moveableShapes = new ArrayList<>();
    private Player player;

    public Board() {
        super();
        player = new Player(60, 50, 3, 3);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDoubleBuffered(true);

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        timer = new Timer(DELAY, this);
        timer.start();
        coloredShapes.add(new ColouredRectangle(0, 0, 10, 10));
        coloredShapes.add(new ColouredRectangle(40, 40, 10, 10));
        coloredShapes.add(new ColouredRectangle(80, 80, 10, 10));
        coloredShapes.add(new ColouredRectangle(100, 100, 50, 50));
        moveableShapes.add(player);
        moveableShapes.add(new Player(50,50,3,3, Color.red));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        if(!coloredShapes.isEmpty()) {
            for(ColoredShape coloredShape : coloredShapes){
                g2d.setColor(coloredShape.getColor());
                g2d.draw(coloredShape);
                g2d.fill(coloredShape);
            }
        }
        if(!moveableShapes.isEmpty()) {
            for(MoveableShape moveableShape : moveableShapes){
                g2d.setColor(moveableShape.getColor());
                g2d.draw(moveableShape);
                g2d.fill(moveableShape);
                if(!moveableShape.getLines().isEmpty()) {
                    for(int i = 0; i < moveableShape.getLines().size(); i++){
                        if(moveableShape.getLines().size() > i+1) {
                            int x1 = (int) moveableShape.getLines().get(i).getX();
                            int y1 = (int) moveableShape.getLines().get(i).getY();
                            int x2 = (int) moveableShape.getLines().get(i+1).getX();
                            int y2 = (int) moveableShape.getLines().get(i+1).getY();
                            g2d.drawLine(x1, y1, x2, y2);
                        }
                    }
                }
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        player.move();
        detectCollisionPlayers();
        detectCollisionShapes();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            //call event in objects
            //player.keyReleased(e);

        }

        @Override
        public void keyPressed(KeyEvent e) {
            //call event in objects
            player.keyPressed(e);

        }
    }

    private void detectCollisionShapes(){
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

    private void detectCollisionPlayers(){
        for(MoveableShape moveableShape : moveableShapes) {
            MoveableShape m = moveableShape;
            for(MoveableShape mS : moveableShapes) {
                if(m != mS && m.intersects(mS.getBounds()))
                    System.out.println("COLL MOVEABLESHAPES");
            }
        }
    }
}

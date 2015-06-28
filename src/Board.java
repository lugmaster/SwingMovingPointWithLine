import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener{

    public static final int WIDTH = 200;
    public static final int HEIGHT = 200;
    private final int DELAY = 40;
    private Timer timer;
    private ShapeContainer shapeContainer;

    public Board() {
        super();

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDoubleBuffered(true);

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        timer = new Timer(DELAY, this);
        timer.start();

        shapeContainer = ShapeContainer.getInstance();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
        Toolkit.getDefaultToolkit().sync();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        shapeContainer.doGameCycle();
    }

    private void doDrawing(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        if(!shapeContainer.getColoredShapes().isEmpty()) {
            for(ColoredShape coloredShape : shapeContainer.getColoredShapes()){
                g2d.setColor(coloredShape.getColor());
                g2d.draw(coloredShape);
                g2d.fill(coloredShape);
            }
        }
        if(!shapeContainer.getMoveableShapes().isEmpty()) {
            for (MoveableShape moveableShape : shapeContainer.getMoveableShapes()) {
                g2d.setColor(moveableShape.getColor());
                g2d.draw(moveableShape);
                g2d.fill(moveableShape);
                if (moveableShape == shapeContainer.getPlayer()) {
                    for (int i = 0; i < shapeContainer.getLines().size(); i++) {
                        if (shapeContainer.getLines().size() > i + 1) {
                            int x1 = (int) shapeContainer.getLines().get(i).getX();
                            int y1 = (int) shapeContainer.getLines().get(i).getY();
                            int x2 = (int) shapeContainer.getLines().get(i + 1).getX();
                            int y2 = (int) shapeContainer.getLines().get(i + 1).getY();
                            g2d.drawLine(x1, y1, x2, y2);
                        }
                    }
                }
            }
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            shapeContainer.getPlayer().keyReleased(e);

        }

        @Override
        public void keyPressed(KeyEvent e) {
            shapeContainer.getPlayer().keyPressed(e);
        }


    }

}

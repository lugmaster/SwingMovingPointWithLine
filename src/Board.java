import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
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
        shapeContainer.doDrawing(g);
        Toolkit.getDefaultToolkit().sync();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        shapeContainer.doGameCycle();
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

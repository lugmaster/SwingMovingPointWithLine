import java.awt.*;
import java.util.ArrayList;

public interface MoveableShape extends ColoredShape {
    public void move(int dx, int dy);

    public ArrayList<Point> getLines();
}

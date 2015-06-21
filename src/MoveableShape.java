import java.awt.*;
import java.util.ArrayList;

public interface MoveableShape extends ColoredShape {
    public void move();

    public ArrayList<Point> getLines();
}

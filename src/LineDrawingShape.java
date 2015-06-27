import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Lukas Normal on 25.06.2015.
 */
public interface LineDrawingShape {
    public void clearLines();

    public ArrayList<Point.Float> getLines();

    public void addLines();
}

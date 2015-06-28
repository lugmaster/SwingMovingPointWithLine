import java.awt.*;

public interface ColoredShape extends Shape {
    public Color getColor();

    public Color generateRandomColor();
}

import java.awt.Color;
import java.awt.Shape;

/*
 * Extends the Shape interface, so that every shape can hold its own color;
 */

public interface ColoredShape extends Shape {
    public Color getColor();
}

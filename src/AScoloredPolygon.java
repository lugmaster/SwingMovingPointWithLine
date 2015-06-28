import java.awt.*;

public class AScoloredPolygon extends Polygon implements ColoredShape{
    private Color color;

    public AScoloredPolygon(int[] intsX, int[] intsY, Color color){
        super(intsX, intsY, intsX.length);
        this.color = color;
    }

    public AScoloredPolygon(int[] intsX, int[] intsY){
        this(intsX, intsY, Color.BLUE);

    }


    @Override
    public Color getColor() {
        return null;
    }
}

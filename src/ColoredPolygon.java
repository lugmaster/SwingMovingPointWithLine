

import java.awt.*;
import java.util.Random;

public class ColoredPolygon extends Polygon implements ColoredShape{
    private Color color;

    public ColoredPolygon(int[] intsX, int[] intsY, Color color){
        super(intsX, intsY, intsX.length);
        this.color = color;
    }

    public ColoredPolygon(int[] intsX, int[] intsY){
        this(intsX, intsY, Color.BLUE);

    }

    public Color generateRandomColor(){
        Random r = new Random();
        int colorIndex = r.nextInt(7);
        Color color = Color.blue;
        switch (colorIndex) {
            case 0:
                color = Color.GREEN;
                break;
            case 1:
                color = Color.DARK_GRAY;
                break;
            case 2:
                color = Color.CYAN;
                break;
            case 3:
                color = Color.YELLOW;
                break;
            case 4:
                color = Color.ORANGE;
                break;
            case 5:
                color = Color.PINK;
                break;
            case 6:
                color = Color.MAGENTA;
                break;
            case 7:
                color = Color.WHITE;
                break;
        }
        return color;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
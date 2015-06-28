import java.awt.*;
import java.util.Random;

/**
 * Created by Lukas Normal on 28.06.2015.
 */
public class RandomColorGenerator {

    public static Color generateRandomColor(){
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
}

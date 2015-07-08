import java.awt.Color;
import java.util.Random;

/*
 * This class has only 1 purpose: generate a random color and return it
 */
public class RandomColorGenerator {

    public static Color generateRandomColor(){
        Random r = new Random();
        int colorIndex = r.nextInt(5);
        Color color = null;
        switch (colorIndex) {
            case 0:
                color = Color.GREEN;
                break;
            case 1:
                color = Color.YELLOW;
                break;
            case 2:
                color = Color.CYAN;
                break;
            case 3:
                //brown
                color = new Color(139,69,19);
                break;
            case 4:
                color = Color.MAGENTA;
                break;
        }
        return color;
    }
}

/**
 * Created by Lukas Normal on 29.06.2015.
 */
public class Initializer {

    private static Initializer initializer;

    private Initializer(){

    }

    public static Initializer getInstance(){
        if(initializer == null)
            return new Initializer();
        return initializer;
    }

}

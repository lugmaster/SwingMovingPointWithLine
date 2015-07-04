/**
 * Created by Lukas Normal on 29.06.2015.
 */
public class GameLogicsContainer {

    private static GameLogicsContainer gameLogicsContainer;

    private GameLogicsContainer(){

    }

    public static GameLogicsContainer getInstance(){
        if(gameLogicsContainer == null)
            return new GameLogicsContainer();
        return gameLogicsContainer;
    }

}

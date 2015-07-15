import java.awt.Point;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.event.KeyEvent;

/*
 * This class generates the Player, which is controllable with the arrowKeys.
 * Each timer tick the Player object is updated and moves around by adding dx,dy to its x and y coordinates from superclass
 * The player is allowed to move on the whole board, however if he leaves the outer shape, and enters the inner shape
 * the player is vulnerable and can collide with an aiPlayer object. While moving in the inner shape, the player draws a line
 * which coordinates are stored in an arrayList. The line is drawn with a colored path object.
 * Only the following positions are stored in the arrayList:
 *  - the point of entering the inner shape
 *  - every time the player changes the moving direction, a new point with the actual position is added
 *  - the point of entering the outer shape again
 * The keyboard input which is detected by the player is stored in integer values, because they can be easily compared
 * @param x,y Coordinates of the Player, stored in position
 * @param radius radius of the ColoredEllipseShape the Player is extending
 * @param moveSpeed sets how often update is applied per timer tick
 * @param color when drawn
 */

public final class Player extends ColoredEllipse implements KeyListener{

    /*
     * KeyMapping for MovingDirection
     * if you wanna change them, this is the right place
     */
    private final int WEST = KeyEvent.VK_A;
    private final int EAST = KeyEvent.VK_D;
    private final int NORTH = KeyEvent.VK_W;
    private final int SOUTH = KeyEvent.VK_S;

    /*
     * boolean values to get information about the players current state
     */
    private boolean isColliding = false;
    private boolean isDrawingLines = false;
    private boolean isVulnerable = false;
    private boolean gameIsRunning = true;

    /*
     * int values for storing input keys.
     * -1 is used when setting the input key to "inactive" (see methods below)
     */
    private int direction = -1;
    private int lastDirection = -1;
    private int lastKeyPressed = -1;

    private int moveSpeed;

    private Point position;
    private Point lastPosition;

    /*
     * a coloredShape path object used for drawing the line while moving
     */
    private ColoredPath path;

    /*
     * each point defines a position of player
     * points are set if:
     * the player enters the inner shape
     * changes his direction
     * enters the outer shape again
     */
    private ArrayList<Point> points;


    public Player(int x, int y, int radius, int moveSpeed, Color color){
        super(x, y, radius, color);
        this.moveSpeed = moveSpeed;
        position = new Point(x,y);
        lastPosition = new Point(position);
        points = new ArrayList<>();
        points.add(new Point(position));
        path = new ColoredPath(color);
    }

    public ColoredPath getPlayerPath(){
        if(gameIsRunning && isDrawingLines){
            return path;
        }
        return null;
    }


    /*
     * keyTyped does not have any use for mondrian, so it
     * stays unimplemented!
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // no need for implementation
    }


    /*
     * valid keys are only arrow keys
     * only the last taken input is stored
     * for the usage of lastKeyPressed look at the move() method
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(isValidKey(key)) {
            lastKeyPressed = key;
        }
    }

    /*
     * valid keys are only arrow keys
     * sets lastKeyPressed to inactive again
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(isValidKey(key)){
            if(key == lastKeyPressed){
                lastKeyPressed = -1;
            }
        }
    }

    /*
     * this method is called by GameLogicsManager and stops the game
     */
    public void setGameToFinished(){
        gameIsRunning = false;
    }

    /*
     * the update method is called once per timer tick
     */
    public void update(ColoredPath inner, ColoredPath outer){
        if(gameIsRunning){
            int updateSteps = moveSpeed;
            while (updateSteps > 0) {
                move();
                updatePosition();
                detectCollisionShapes(inner, outer);
                if(isDrawingLines) {
                    detectSelfCollision();
                    updatePlayerPath();
                }
                updateSteps--;
            }
        }
    }

    public boolean isVulnerable(){
        return isVulnerable;
    }

    /*
     * move determines the direction the player is moving by checking the taken input
     * from the keyPressed method.
     * To change the moving direction without interruption caused by the update function it is necessary to
     * store the last taken input from the user. Without storing the last input, it will take a whole update cycle for stopping and assigning
     * a new direction. With storing only the last input key regardless of any released input key, the input control of the player responds quickly and
     * moving feels "natural" and "smooth".
     * It is only possible to move vertical xor horizontal. It´s not allowed to move backwards, from the last moved direction.
     * The player moves as long as the input key is held down or as long as direction is NOT -1
     * Every time the direction is changed, a new point with the actual position is added to the points arrayList
     * a direction is called "changed" if the one of the following cases occur:
     *  - lastPressedKey is not the same direction
     *  - lastPressedKey is a left or right turn seen from actual moving direction
     *  - if not moving: lastPressedKey is a left or right turn seen from the last moving direction
     *
     *  direction stores the actual moving direction
     *  last direction stores the last taken direction before direction
     */
    private void move(){
        //case: player stops, therefore direction becomes lastDirection
        if(lastKeyPressed == -1){
            if(direction != -1) lastDirection = direction;
            direction = -1;
        }
        //case: player moves
        else {
            //case: first move from player (start of the game), therefor direction and lastDirection are equal
            if(direction == -1 && lastDirection == -1){
                lastDirection = lastKeyPressed;
                direction = lastKeyPressed;
            }
            //case: first input after releasing all input keys for at least 1 timer tick
            else if(direction == -1 && !isOpositeDirection(lastDirection, lastKeyPressed)){
                direction = lastKeyPressed;
                //case: player changed direction
                if(direction != lastDirection){
                    addPoint(position);
                }
            }
            //case: new direction input key is pressed before the last one is released(this case can only be a direction change)
            else if(direction != -1 && lastKeyPressed != direction && !isOpositeDirection(direction, lastKeyPressed)){
                lastDirection = direction;
                direction = lastKeyPressed;
                addPoint(position);
            }
        }
        //generates a new int array with delta x and delta y values depending on direction
        int[] deltaPos = adjustMovement(direction);
        super.move(deltaPos[0],deltaPos[1]);
    }

    private void updatePosition(){
        position.setLocation(x,y);
    }

    /*
     * only relevant if player is drawingLines
     * checks for every point and its successor points if the player position lies between
     * those 2 points. if true, the player is colliding with his own drawn path,
     * therefore all points between the collision point and the actual player position need to be removed and
     * a new Point with the actual player position is added
     */
    private void detectSelfCollision(){
        boolean foundSelfCollision = false;
        if(points.size() >= 2){
            for (int i = 0; i < points.size(); i++) {
                if(points.isEmpty() || i < 0)return;
                if(foundSelfCollision){
                    points.remove(i);
                    i--;
                }
                else{
                    if(i+1 < points.size()){
                        Point p1 = points.get(i);
                        Point p2 = points.get(i+1);
                        if(GameLogicsManager.pointIsInLine(p1,p2,position)){
                            foundSelfCollision = true;
                        }
                    }
                }
            }
        }
        if(foundSelfCollision){
            addPoint(position);
        }
    }

    /*
     * if the player position has changed since the last update cycle
     * lastPosition needs to be reassigned.
     * The player path needs an arrayList to apply a new Shape. To avoid any circumstances
     * with the shape splitting algorithm, playerPath gets his own arrayList with the actual position
     * as last coordinate.
     * It will only be actualized if the player had been moving in the last update cycle
     */
    private void updatePlayerPath(){
        if(positionHasChanged()){
            lastPosition.setLocation(position);
            ArrayList<Point> playerPath = new ArrayList<>(points);
            playerPath.add(new Point(position));
            path.setNewPath(playerPath, false);
        }
    }


    /*
     * Detects if the player is in the inner or the outer shape
     * If the player "isColliding" means his position is in the inner shape
     * onCollisionEnterInnerShape and onCollisionExitInnerShape switch the player
     * state (see methods below)
     */
    private void detectCollisionShapes(ColoredPath inner, ColoredPath outer) {
        if(!isColliding && inner.contains(position)){
            onCollisionEnterInnerShape();
            isColliding = true;
        }
        if(isColliding && outer.contains(position)){
            onCollisionExitInnerShape();
            isColliding = false;
        }
    }

    /*
     * If this method is called, the player has succesfully "cut" a line through the inner shape.
     * The inner shape can now be split along the drawn line.
     * In order to get the right position on collision exit a adjusted point has to be added, depending on the moving
     * direction.
     */
    private void onCollisionExitInnerShape(){
        addAdjustedPoint(direction);
        GameLogicsManager.getInstance().splitInnerShape(points);
        isDrawingLines = false;
        isVulnerable = false;
        clearPoints();
    }

    /*
     * If this method is called, the player left the outer shape and starts drawing a line
     * In order to get the right position on collision enter a adjusted point has to be added, depending on the moving
     * direction.
     */
    private void onCollisionEnterInnerShape(){
        clearPoints();
        isDrawingLines = true;
        isVulnerable = true;
        addAdjustedPoint(direction);
    }

    private void addPoint(Point position) {
        points.add(new Point(position));
    }

    /*
     * If the direction is NORTH or WEST the point storing the position
     * of the player needs to be adjusted by ax or ay
     * @ param direction    actual moving direction
     */
    private void addAdjustedPoint(int direction){
        int ax = 0;
        int ay = 0;
        if(direction == WEST) ax++;
        if(direction == NORTH) ay++;
        points.add(new Point((int) this.x + ax, (int) this.y + ay));
    }

    private void clearPoints() {
        points.clear();
        path.reset();
    }

    private boolean positionHasChanged(){
        return !position.equals(lastPosition);
    }

    private boolean isValidKey(int key){
        return (key == WEST || key == EAST || key == NORTH || key == SOUTH);
    }


    /*
     * adjusts the delta x and delta y (dx, dy) values, aplied to the
     * superclass move method
     * @ param direction    actual moving direction
     */
    private int[] adjustMovement(int direction) {
        int[] deltaPos = {0,0};
        if(direction == WEST) deltaPos[0] = -1;
        if(direction == EAST) deltaPos[0] = 1;
        if(direction == NORTH) deltaPos[1] = -1;
        if(direction == SOUTH) deltaPos[1] = 1;
        return deltaPos;
    }

    private boolean isOpositeDirection(int oldDirection, int newDirection){
        return  (oldDirection == WEST && newDirection == EAST) || (oldDirection == EAST && newDirection == WEST) ||
        (oldDirection == NORTH && newDirection == SOUTH) || (oldDirection == SOUTH && newDirection == NORTH);
    }

}

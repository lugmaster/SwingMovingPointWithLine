import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ColoredPath extends Path2D.Float implements ColoredShape {

    private ArrayList<Point2D.Float> points;
    private Color color;

    public ColoredPath(Color color){
        super();
        this.color = color;
        this.points = new ArrayList<>();
    }

    public ColoredPath(Shape s){
        super(s);
        this.color = RandomColorGenerator.generateRandomColor();
        this.points = new ArrayList<>();
    }

    public ColoredPath(ArrayList<Point2D.Float> points, Color color, boolean closed){
        this.points = points;

        for (int i = 0; i < points.size(); i++) {
            if(i == 0) this.moveTo(points.get(i).getX(), points.get(i).getY());
            else this.lineTo(points.get(i).getX(), points.get(i).getY());
            System.out.println("newPath:" + points.get(i));
        }
        if(closed) this.closePath();
        System.out.println("#############################\n");
        this.color = color;
    }

    public ColoredPath(ArrayList<Point2D.Float> points, boolean closed){
        this(points, RandomColorGenerator.generateRandomColor(), closed);
    }

    public void setNewPath(ArrayList<Point2D.Float> newPoints){
        points.clear();
        if(newPoints.isEmpty()){
            moveTo(0,0);
            points.add(new Point2D.Float(0,0));
        }
        else{
            for (int i = 0; i < newPoints.size(); i++) {
                if(i==0)this.moveTo(newPoints.get(i).getX(), newPoints.get(i).getY());
                else this.lineTo(newPoints.get(i).getX(), newPoints.get(i).getY());
                points.add(new Point2D.Float((float) newPoints.get(i).getX(), (float) newPoints.get(i).getY()));
            }
        }

    }

    public void setNewPath(ArrayList<Point2D.Float> newPoints, Player player){
        Point2D.Float p2d = new Point2D.Float((float) player.getPosition().getX(), (float) player.getPosition().getY());
        if(newPoints.isEmpty()){
            points.clear();
            moveTo(p2d.getX(),p2d.getY());
        }
        else {
            setNewPath(newPoints);
            this.lineTo(p2d.getX(),p2d.getY());
            points.add(p2d);
        }

    }


    @Override
    public Color getColor() {
        return color;
    }
}

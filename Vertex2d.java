//Shiran Golbar, 313196974
//Lev Levin, 342480456
/**
 * This class represents a vertex(point) in 2 dimensional space.
 */
public class Vertex2d extends Vector {
    public Vertex2d(double x, double y) {
        super(new double[]{x,y});
    }
    public Vertex2d(Vector v) {
        super(v.getVectorAsArray());
        if (v.getNumOfElements() != 2) {
            throw new IllegalArgumentException("vertex2d has 2 elements!");
        }

    }

    public double getX() {
        return this.vector[0];
    }

    public double getY() {
        return this.vector[1];
    }


    public void setX(double x){ this.vector[0] = x; }

    public void setY(double y)
    {
        this.vector[1] = y;
    }

}

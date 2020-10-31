//Shiran Golbar, 313196974
//Lev Levin, 342480456

/**
 * This class represents a vertex(point) in 3 dimensional space.
 */
public class Vertex extends Vector {


    public Vertex(double x, double y, double z) {
        super(new double[]{x,y,z});
    }
    public Vertex(Vector v) {
        super(v.getVectorAsArray());
        if (v.getNumOfElements() != 3) {
            throw new IllegalArgumentException("vertex has 3 elements!");
        }

    }

    public double getX() {
        return this.vector[0];
    }

    public double getY() {
        return this.vector[1];
    }

    public double getZ(){
        return this.vector[2];
    }

    public void setX(double x){

        this.vector[0] = x;
    }

    public void setY(double y)
    {
        this.vector[1] = y;
    }

    public void setZ(double z) {

        this.vector[2] = z;
    }

}

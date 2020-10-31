//Shiran Golbar, 313196974
//Lev Levin, 342480456

/**
 * This class represents a line in two dimensional space.
 */
public class Line2d {
    private Vertex2d p0;
    private Vertex2d p1;
    public Line2d(Vertex2d point0, Vertex2d point1) {
        p0 = new Vertex2d(point0.getX(),point0.getY());
        p1 = new Vertex2d(point1.getX(),point1.getY());;
    }

    /**
     * This function computes intersection point of this line with 'other' line.
     * @param other
     * @return
     */
    public Vertex2d computeIntersection(Line2d other) {
        double a1 = this.p1.getY() - this.p0.getY();
        double b1 = this.p0.getX() - this.p1.getX();
        double c1 = a1 * (this.p0.getX()) + b1 * (this.p0.getY());
        double a2 = other.p1.getY() - other.p0.getY();
        double b2 = other.p0.getX() - other.p1.getX();
        double c2 = a2 * (other.p0.getX()) + b2 * (other.p0.getY());
        double det = a1 * b2 - a2 * b1;
        double x = (b2 * c1 - b1 * c2) / det;
        double y = (a1 * c2 - a2 * c1) / det;
        return new Vertex2d(x,y);
    }

    public Vertex2d getP0() {
        return new Vertex2d(p0.getX(), p0.getY());
    }

    public Vertex2d getP1() {
        return new Vertex2d(p1.getX(), p1.getY());
    }
}

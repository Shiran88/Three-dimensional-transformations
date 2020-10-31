//Shiran Golbar, 313196974
//Lev Levin, 342480456
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * This class provides with means and tools to perform clipping of graphical objects in specific window frame.
 * It implements Cohen-Sutherland algorithm.
 */
public class Clipper {
    double xMin;
    double xMax;
    double yMin;
    double yMax;
    Line2d currentLine; // current line which is processing(in order to clip if it is needed)

    //4 points defining window frame in 2d window.
    private Vertex2d downLeft;
    private Vertex2d upLeft;
    private Vertex2d upRight;
    private Vertex2d downRight;
    public Clipper(int xMin, int xMax, int yMin, int yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        downLeft = new Vertex2d(xMin,yMin);
        upLeft = new Vertex2d(xMin,yMax);
        upRight = new Vertex2d( xMax,yMax);
        downRight = new Vertex2d(xMax,yMin);
        currentLine = null;
    }

    /**
     * This function returns the processed clipped line by clipping algorithm.
     * If there was no need to clip the line - the clipping algorithm would not
     * change the line. This function must be called only after the line was processed
     * by the clip() function.
     * @return processed line by the algorithm.
     */
    public Line2d getClippedLine() {
        if (currentLine == null) {
            throw new IllegalArgumentException( "Calling getClippledLine() before clip() was called! ");
        }
        return currentLine;
    }

    /**
     * This function performs clipping on the line. To get clipped line, use getClippedLine() function.
     *
     * @param line to clip
     * @return true if the line is out of screen borders(in this way the line wasn't clipped and should not be shown)
     *         false if some parts of the line is inside the screen frame.
     */
    public boolean clip(Line2d line) {
        this.currentLine = line;
        int[] codeP0 = initCode(line.getP0().getX(),line.getP0().getY());
        int[] codeP1 = initCode(line.getP1().getX(),line.getP1().getY());
        int[] resultAnd = {0, 0, 0, 0};
        int[] resultOr = {0, 0, 0, 0};
        for (int i = 0; i < 4; i++) {
            resultAnd[i] = ((codeP0[i]!=0) && (codeP1[i]!=0))? 1:0;
        }
        if (checkCode(resultAnd) != 0) {
            return false;
        }
        for (int i = 0; i < 4; i++) {
            resultOr[i] = ((codeP0[i]!=0) || (codeP1[i]!=0))? 1:0;
        }
        if (checkCode(resultOr) == 0) {
            return true;
        }
        return cutLine(codeP0,codeP1);
    }

    /**
     * Initializing bits code for a point (x,y).
     * @param x x coordinate of a point.
     * @param y y coordinate of a point.
     * @return
     */
    private int[] initCode(double x,double y) {
        int[] bits = new int[4];
        bits[0] = y< yMin ? 1:0;
        bits[1] = x > xMax ? 1:0;
        bits[2] = y > yMax? 1:0;
        bits[3] = x< xMin? 1:0;
        return bits;
    }

    /**
     * This function cuts line(clips it) according to bits codes of line points.
     * @param codeP0 bits code for start line point.
     * @param codeP1 bits code for end line point.
     * @return
     */
    private boolean cutLine(int[] codeP0,int[] codeP1) {

        Vertex2d[] lines={downLeft,downRight,upRight,upLeft};
        while (checkCode(codeP0) != 0) {
            for (int i = 0; i < 4; i++) {
                if (codeP0[i] == 1) {
                    Vertex2d newP = this.currentLine.computeIntersection(new Line2d(lines[i],lines[(i+1)%4]));
                    this.currentLine = new Line2d(newP, this.currentLine.getP1());
                    codeP0 = initCode(this.currentLine.getP0().getX(), this.currentLine.getP0().getY());
                    if (checkCode(codeP0)!=0){
                        return false;
                    }
                }
            }
        }
        while (checkCode(codeP1) != 0) {
            for (int i = 0; i < 4; i++) {
                if (codeP1[i] == 1) {
                    Vertex2d newP = this.currentLine.computeIntersection(new Line2d(lines[i],lines[(i+1)%4]));
                    this.currentLine = new Line2d(this.currentLine.getP0(), newP);
                    codeP1 = initCode(this.currentLine.getP1().getX(),this.currentLine.getP1().getY());
                    if (checkCode(codeP1)!=0){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private int checkCode(int[] codeResult) {
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += codeResult[i];
        }
        return sum;
    }
    public void initLine() {
        this.currentLine = null;
    }

}



//    ArrayList<Vector> boundVectors = new ArrayList<>();
//    ArrayList<Vector> origins = new ArrayList<>();
//    ArrayList<Vector> ns = new ArrayList<>();
//    Clipper(Vertex2d topLeft, Vertex2d topRight, Vertex2d botLeft, Vertex2d botRight) {
//        boundVectors.add(topRight.subtract(topLeft));
//        origins.add(topLeft);
//        boundVectors.add((topRight.subtract(botRight)));
//        origins.add(botRight);
//        boundVectors.add(botRight.subtract(botLeft));
//        origins.add(botLeft);
//        boundVectors.add(topLeft.subtract(botLeft));
//        origins.add(botLeft);
//
//        for (int i = 0; i < boundVectors.size(); i++) {
//            ns.add(new Vector(new double[]{-boundVectors.get(i).getElement(1),
//                                            boundVectors.get(i).getElement(0)}));
//        }
//
//    }
//
//    public Line2d clip(Line2d line) {
//        ArrayList<Vertex2d> pls = new ArrayList<>();
//        ArrayList<Vertex2d> pes = new ArrayList<>();
//        for (int i = 0; i < ns.size(); i++){
//            Vector p0 = line.getP0().subtract(origins.get(i));
//            Vector p1 = line.getP1().subtract(origins.get(i));
//            Line2d lineP = new Line2d(new Vertex2d(p0), new Vertex2d(p1));
//            Vector v = boundVectors.get(i);
//            Vector q = new Vector(new double[] {v.getElement(0)/2, v.getElement(1)/2});
//            if (q.equals(p0)) {
//                q.setElement(v.getElement(0) / 3,0);
//                q.setElement(v.getElement(0) / 3, 0);
//            }
//            Vector delta = p1.subtract(p0);
//            double deltaProductN =  delta.dotProduct(ns.get(i);
//            if (deltaProductN == 0) {
//                continue;
//            }
//            double t = (ns.get(i).dotProduct(p0.subtract(q))) / -ns.get(i).dotProduct(delta);
//            if  (t > 1 || t < 0) {
//
//                continue;
//            }
//            Vertex2d firstPointV = new Vertex2d(0, 0);
//            Vertex2d secondPointV = new Vertex2d(boundVectors.get(i).getElement(0), boundVectors.get(i).getElement(1));
//            Line2d vAsLine = new Line2d(firstPointV, secondPointV);
//            Vertex2d intersection = lineP.calculateIntersection(vAsLine);
//            if (intersection != null && deltaProductN > 0) {
//                pls.add(intersection);
//            } else if(intersection != null && deltaProductN < 0) {
//                pes.add(intersection);
//            }
//        }
//
//    }



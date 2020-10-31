//Shiran Golbar, 313196974
//Lev Levin, 342480456
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;

import static java.lang.System.exit;

/**
 * This class represents a canvas that presents on the screen a movable cube and provides
 * with tools to perform different transformations on it.
 */
public class CanvasOfAll extends Canvas implements KeyListener, MouseListener, MouseMotionListener {
    private static final int MARGIN = 40;
    private double afterMousePressX, afterMousePressY; //x and y after mouse press
    private Scene scn;
    private View view;
    Frame myFrame;
    private ArrayList<Vertex> listOfVertexes;
    private ArrayList<int[]> listOfEdges;
    private double afterMouseDragX, afterMouseDragY; //x and y after mouse drag
    private int widthScreen, heightScreen;
    // height and width immediately after loading the scn and viw files
    private int originalWidthScreen, originalHeightScreen;
    private Matrix VM1, P,AT, VM2, CT; // All matrices needed in transformation pipeline.
    private Vertex center; //Vertex representing the center of thee window.
    private boolean clippingOn = false; // whether to do clipping or not.
    private Clipper clipper;
    private RotationAxis axis = RotationAxis.Z;

    public CanvasOfAll(Frame frame, String scnFileName, String viwFileName) {

        this.myFrame = frame;
        this.scn = new Scene(scnFileName);
        this.view = new View(viwFileName);
        scn.parseScnFile();
        view.parseViwFile();
        originalWidthScreen = view.getWidthScreen();
        originalHeightScreen = view.getHeightScreen();
        listOfVertexes = scn.getListOfPoints();
        listOfEdges = scn.getListOfLines();
        this.clipper = new Clipper(MARGIN / 2, view.getWidthScreen() + MARGIN/2,
                MARGIN / 2, view.getHeightScreen()+MARGIN/2);
        Dimension d = new Dimension();
        d.width = (int) this.view.getWidthScreen() + MARGIN;
        d.height = (int) this.view.getHeightScreen() + MARGIN;
        this.setPreferredSize(d); // adjusting window size.
        frame.pack();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);


        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Component comp = e.getComponent();
                Dimension dim = comp.getSize();
                widthScreen = dim.width - MARGIN;
                heightScreen = dim.height - MARGIN;
                center.setX((widthScreen / 2) + MARGIN / 2);
                center.setY((heightScreen / 2) + MARGIN / 2);
                view.setWidthScreen(widthScreen);
                view.setHeightScreen(heightScreen);
                clipper = new Clipper(MARGIN / 2, view.getWidthScreen() + MARGIN/2,
                        MARGIN / 2, view.getHeightScreen()+MARGIN/2);
               // Transformations3d.setView(view);
                restartAllTheMatrix();
            }
        });
        this.initializeScreen(true);
        int x=(this.widthScreen /2);
        int y=(this.heightScreen/2);
        this.center = new Vertex(x+MARGIN/2,y+MARGIN/2,0);
        restartAllTheMatrix();

    }
    private void restartAllTheMatrix(){
        //need to check that VM1 is good- all of is
        VM1 = Transformations3d.Viewing( this.view.getCameraPosition(),
                this.view.getLookAt(), this.view.getCameraAngleUp());
        P = Transformations3d.orthographicProjection();
       // T1 = tranforamtions.t1();
        //T2 = tranforamtions.t2();
        VM2 = this.view.getViewPortMatrix();
    }


    /**
     * Creates vectors so that they are vertices but have the form [x, y, z, 1];
     * We need this function because View class stores vertices in a form [x ,y ,z], but the
     * calculation in pipeline require these vertices to have a form [x, y, z, 1]. This function
     * maps from [x,y,z] list to [x,y,z,1] list so that the transformations on the vertices can
     * be applied.
     *
     * @param vertices list of vertices to present on the screen.
     * @return
     */
    public ArrayList<Vector> createVectorsFromVertexes(ArrayList<Vertex> vertices) {
        ArrayList<Vector> vectors = new ArrayList<Vector>();
        for (int i = 0; i < vertices.size(); i++) {
            double[] arr = new double[]{
                    vertices.get(i).getX(), vertices.get(i).getY(), vertices.get(i).getZ(), 1};
            vectors.add(i, new Vector(arr));
        }
        return vectors;
    }
    public void paint(Graphics g) {
        this.requestFocus();
        g.drawRect(MARGIN / 2, MARGIN / 2, view.getWidthScreen(), view.getHeightScreen());
        //Mapping vertices to vectors(adding additional dimension with value 1) so that transformation can be applied.
        ArrayList<Vector> vertices = this.createVectorsFromVertexes(this.listOfVertexes);
        //in this list we will store vectors of vertices after applying transformations pipeline, so that they can
        //be directly drawn with draLine() method on the screen.
        ArrayList<Vector> newVertexList = new ArrayList<Vector>();
        // Initializing the matrix that maps from world coordinates to viewer coordinates.
        VM1 = Transformations3d.Viewing(this.view.getCameraPosition(),
                this.view.getLookAt(), this.view.getCameraAngleUp());
        // Initializing the matrix that projects 3d coordinates to 2d coordinates.
        this.P = Transformations3d.orthographicProjection();
        // Initializing the matrix that maps from 2d coordinates of the viewer to 2d coordinates of the device(screen)
        // so that the object can be directly pictured.
        VM2 = this.view.getViewPortMatrix();
        //The final matrix that represents all transformation pipeline so that after multiplying vertices by it
        // we will get vertices that can be directly drawn on the screen.
        Matrix FinalM = VM2.multiply(this.P).multiply(CT).multiply(this.AT).multiply(this.VM1);
        for (int i = 0; i < this.listOfVertexes.size(); i++) {
            Vector newV = FinalM.multiply(vertices.get(i));
            newVertexList.add(i, newV);
        }
        for (int[] e : this.listOfEdges) {
            int x1 = (int) newVertexList.get(e[0]).getElement(0);
            int y1 = (int) newVertexList.get(e[0]).getElement(1);
            int x2 = (int) newVertexList.get(e[1]).getElement(0);
            int y2 = (int) newVertexList.get(e[1]).getElement(1);

            if (clippingOn && clipper.clip(new Line2d(new Vertex2d(x1,y1), new Vertex2d(x2,y2)))) {
                Line2d l = clipper.getClippedLine();
                clipper.initLine();
                g.drawLine((int) l.getP0().getX(), (int) l.getP0().getY(), (int) l.getP1().getX(), (int) l.getP1().getY());
            } else if (!clippingOn) {
                g.drawLine(x1,y1,x2,y2);
            }
        }
    }

    /**
     * This function prepares the matrix transformation matrix CT so that
     * multiplying the vertex by it will translate the vertex.
     */
    private void prepareTranslation() {
        double x= afterMouseDragX - afterMousePressX;
        double y= afterMouseDragY - afterMousePressY;

        CT = Transformations3d.translate(x * ((view.getWindowWidth()) / this.widthScreen),
                y * (-((view.getWindowHeight()) / this.heightScreen)),0);
    }
    /**
     * This function prepares the matrix transformation matrix CT so that
     * multiplying the vertex by it will scale the vertex.
     */
    private void prepareScale(){
        //distance
        double x_center=this.center.getX();
        double y_center=this.center.getY();
        double dv = Math.hypot(afterMouseDragX -x_center, afterMouseDragY -y_center);
        double sv = Math.hypot(this.afterMousePressX -x_center, this.afterMousePressY -y_center);
        double sf = dv / sv;
        CT = Transformations3d.scale(sf, sf, sf);
        adjustCT();
    }
    /**
     * This function prepares the matrix transformation matrix CT so that
     * multiplying the vertex by it will rotate the vertex.
     */
    private void prepareRotation() {
        double yStart = afterMousePressY - center.getY();
        double xStart = afterMousePressX - center.getX();
        double yEnd = afterMouseDragY - center.getY();
        double xEnd = afterMouseDragX - center.getX();
        double angleStart = Math.atan2(yStart, xStart);
        double angleEnd = Math.atan2(yEnd, xEnd);
        CT = Transformations3d.rotate(angleStart - angleEnd,this.axis);
        adjustCT();
    }


    /**
     * Adjusts CT transformation matrix so that Scale and rotation transformations are performed relatively
     * to the LookAt point.
     */
    private void adjustCT(){
        double d;
        Vertex lookAtPoint = view.getLookAt();
        Vertex posPoint = view.getCameraPosition();
        Vector sub = posPoint.subtract(lookAtPoint);
        d = sub.getMagnitude();

        Matrix tmp = Matrix.createIdentityMatrix(4);
        tmp.setEntry(d,2,3);
        CT = CT.multiply(tmp);
        tmp.setEntry(-d,2,3);
        CT = tmp.multiply(CT);


    }



    /**
     * according to (x,y) mouse position detects in which of 9 parts of the window the mouse was pressed.
     * After this, according to the part specification, performs appropriate Transformation.
     * @param x x pos of mouse
     * @param y y pos of mouse
     */
    private void detectAndPerformAct(double x, double y) {
        int limit = MARGIN / 2;
        double xVal = x  / (this.widthScreen + MARGIN);
        double yVal = y / (this.heightScreen + MARGIN);
        //not on borders
        if (x < limit|| y < limit || x > this.widthScreen + limit || y > this.heightScreen + limit) {
            return;
        } else if (xVal >= 1.0/3.0 && xVal <= 2.0/3.0 && yVal >= 1.0/3.0 && yVal <= 2.0/3.0) {
            prepareTranslation();
        } else if ((xVal < 1.0/3.0 || xVal > 2.0/3.0) &&(yVal < 1.0/3.0 || yVal > 2.0/3.0)) {
            prepareRotation();
        } else {
            prepareScale();
        }
    }

    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mousePressed(MouseEvent arg0) {
        // TODO Auto-generated method stub
        afterMousePressX = arg0.getX();
        afterMousePressY = arg0.getY();

    }

    public void mouseReleased(MouseEvent arg0) {
        afterMouseDragX = arg0.getX();
        afterMouseDragY = arg0.getY();
        AT = CT.multiply(AT);
        CT = Matrix.createIdentityMatrix(4);


        this.repaint();
    }
    /*
     get x and y after drag and find which operation to do
     */
    public void mouseDragged(MouseEvent arg0) {
        // TODO Auto-generated method stub
        afterMouseDragX = arg0.getX();
        afterMouseDragY = arg0.getY();
        //oper();
        detectAndPerformAct(this.afterMousePressX, afterMousePressY);
        this.repaint();

    }

    public void mouseMoved(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    /**
     * This method initializes screen parameters according to current view and scn.
     */
    public void initializeScreen(boolean isReload) {
        this.CT = Matrix.createIdentityMatrix(4);
        this.AT = Matrix.createIdentityMatrix(4);

        this.listOfVertexes = this.scn.getListOfPoints();
        this.listOfEdges = this.scn.getListOfLines();
        if (isReload) {
            this.widthScreen = view.getWidthScreen();
            this.heightScreen = view.getHeightScreen();
            originalWidthScreen = view.getWidthScreen();
            originalHeightScreen = view.getHeightScreen();
        } else {
            this.widthScreen = originalWidthScreen;
            this.heightScreen = originalHeightScreen;
        }

        Dimension dimension = new Dimension();
        dimension.height = this.heightScreen + MARGIN;
        dimension.width = MARGIN + this.widthScreen;
        this.setPreferredSize(dimension);
        myFrame.pack();
    }

    /**
     * This function resets screen configurations to default state according to current scn and viw settings.
     */
    private void reset(boolean flag) {
        initializeScreen(flag);
        this.axis = RotationAxis.Z;
        this.clippingOn = false;
        this.restartAllTheMatrix();
        this.repaint();

    }

    /**
     * This function opens file dialog window which allows
     * to the user to upload new scn or viw files and reset the screen
     * adjusted by these updated files while the program is running.
     */
    private void reloadFiles() {
        JFileChooser chooser = new JFileChooser();
        String workingDir = System.getProperty("user.dir");
        chooser.setCurrentDirectory(new File(workingDir));
        if (chooser.showOpenDialog(chooser.getParent()) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            String extension = path.substring(path.lastIndexOf('.') + 1);
            if (extension.equals("scn")) {
                this.scn = new Scene(path);
                this.scn.parseScnFile();
            } else if (extension.equals("viw")) {
                this.view = new View(path);
                this.view.parseViwFile();
                this.clipper = new Clipper(MARGIN / 2, view.getWidthScreen() + MARGIN/2,
                        MARGIN / 2, view.getHeightScreen()+MARGIN/2);
            }
            reset(true);
        }
        repaint();
    }
    public void keyTyped(KeyEvent e) {
        char key = Character.toLowerCase(e.getKeyChar());
        switch (key) {
            case 'c':
                this.clippingOn = !this.clippingOn;
                this.repaint();
                break;
            case 'x':
                this.axis = RotationAxis.X;
                break;
            case 'y':
                this.axis = RotationAxis.Y;
                break;
            case 'z':
                this.axis = RotationAxis.Z;
                break;
            case 'r':
                this.reset(false);
                break;
            case 'q':
                exit(1);
                break;
            case 'l':
                reloadFiles();
            default:
                break;

        }
    }











}
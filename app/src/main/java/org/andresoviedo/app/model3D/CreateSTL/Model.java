package org.andresoviedo.app.model3D.CreateSTL;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/*import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;*/

/**
 * @author schmitz-justensj16
 */
public class Model //extends RenderWrapper implements ActionListener, ChangeListener            이 두 리스너도 필요없을 듯
{
    private static final long serialVersionUID = 1L;
    private ArrayList<Triangle> myTriangles = new ArrayList<Triangle>();
    private String outputPath; // to output file
    private double scaleX, scaleY, scaleZ;

    public Model() {
        scaleX = 6;
        scaleY = 6;
        scaleZ = 1;
    }

    /**
     * Precondition: Runner.inputFile1 has a valid Image; Postcondition:
     * myTrangles is filled with all necessary triangles for the model
     * <p>
     * Recomputes the Triangles for the 3D model
     */

    public void recompute() {
        // Create array to hold all Vertices temporarily

        Vertex[][][] varr = new Vertex[Runner.inputFile1.getImageWidth()][Runner.inputFile1
                .getImageHeight()][2];
        // Discard any previous triangles

        myTriangles.clear();
        System.out.println("Fill Array :" + varr.length);

        // fill array with vertex info from images
        for (int x = 0; x < varr.length; x++) {
            for (int y = 0; y < varr[x].length; y++) {

                // load from images, 0 indicates top surface, 1 indicates bottom
                varr[x][y][0] = Runner.inputFile1.getVertex(x, y);
                varr[x][y][1] = Runner.inputFile2.getVertex(x, y);

                // If z value for bottom is >= z value of top
                if (varr[x][y][0].z <= varr[x][y][1].z) {
                    // Let both share one vertex that is average of the two
                    Vertex temp = new Vertex(x, y,
                            (varr[x][y][0].z + varr[x][y][1].z) / 2);
                    varr[x][y][1] = temp;
                    varr[x][y][0] = temp;
                }
            }
        }

        System.out.println("Make Top/Bottom");
        // make triangles for top and bottom surfaces
        for (int col = 0; col < varr.length - 1; col++) {
            for (int row = 0; row < varr[col].length - 1; row++) {

                // create temporary triangles for top and bottom to allow
                // for
                // comparison
                Triangle top1, top2, bottom1, bottom2;
                // create triangles for top
                top1 = new Triangle(varr[col][row][0], varr[col][row + 1][0],
                        varr[col + 1][row][0]);
                top2 = new Triangle(varr[col][row + 1][0],
                        varr[col + 1][row + 1][0], varr[col + 1][row][0]);
                // create triangles for bottom
                bottom1 = new Triangle(varr[col + 1][row][1],
                        varr[col][row + 1][1], varr[col][row][1]);
                bottom2 = new Triangle(varr[col + 1][row][1],
                        varr[col + 1][row + 1][1], varr[col][row + 1][1]);


                // only add triangles if top and bottom do not share same
                // vertices

                if (!top1.equals(bottom1)) {
                    myTriangles.add(top1);
                    myTriangles.add(bottom1);
                }
                if (!top2.equals(bottom2)) {
                    myTriangles.add(top2);
                    myTriangles.add(bottom2);
                }
            }
        }

        System.out.println("Left/right");
        // make triangles for left surface
        // make triangles for right surface
        for (int row = 0; row < varr[0].length - 1; row++) {
            //            // add triangles for the left side
            myTriangles.add(new Triangle(varr[0][row][1], varr[0][row + 1][1],
                    varr[0][row][0]));
            myTriangles.add(new Triangle(varr[0][row + 1][1],
                    varr[0][row + 1][0], varr[0][row][0]));
            // add triangles for the right side
            myTriangles.add(new Triangle(varr[varr.length - 1][row][0],
                    varr[varr.length - 1][row + 1][0],
                    varr[varr.length - 1][row][1]));
            myTriangles.add(new Triangle(varr[varr.length - 1][row + 1][0],
                    varr[varr.length - 1][row + 1][1],
                    varr[varr.length - 1][row][1]));
        }

        System.out.println("front/back");
        // make triangles for the front
        // make triangles for the back
        for (int col = 0; col < varr.length - 1; col++) {
            // add triangles for the front
            myTriangles.add(new Triangle(varr[col][varr[0].length - 1][0],
                    varr[col][varr[0].length - 1][1],
                    varr[col + 1][varr[0].length - 1][0]));
            myTriangles.add(new Triangle(varr[col][varr[0].length - 1][1],
                    varr[col + 1][varr[0].length - 1][1],
                    varr[col + 1][varr[0].length - 1][0]));
            // add triangles for the back
            myTriangles.add(new Triangle(varr[col][0][1], varr[col][0][0],
                    varr[col + 1][0][1]));
            myTriangles.add(new Triangle(varr[col][0][0], varr[col + 1][0][0],
                    varr[col + 1][0][1]));
        }

        System.out.println("Remove extras");
        // Some of the right, front, left, and back triangles have zero
        // area.
        // Remove them.
        ArrayList<Triangle> temp = new ArrayList<Triangle>();
        for (Triangle t : myTriangles)
            if (t.hasArea())
                temp.add(t);
        myTriangles = temp;

        System.out.println("Total Triangles: " + myTriangles.size());

        System.out.println("normalize");
        normalize();

        System.out.println("render");
        //render(myTriangles);

    }

    public void recompute(int width, int height) {
        // Create array to hold all Vertices temporarily
        //Log.e("Runner", "inputFile1 넓이값: "+ Runner.inputFile1.getImageWidth());
        Vertex[][][] varr = new Vertex[width][height][2];
        // Discard any previous triangles
        myTriangles.clear();

        System.out.println("Fill Array :" + varr.length);
        // fill array with vertex info from images
        for (int x = 0; x < varr.length; x++) {
            for (int y = 0; y < varr[x].length; y++) {

                // load from images, 0 indicates top surface, 1 indicates bottom
                varr[x][y][0] = Runner.inputFile1.getVertex(x, y);
                varr[x][y][1] = Runner.inputFile2.getVertex(x, y);

                // If z value for bottom is >= z value of top
                if (varr[x][y][0].z <= varr[x][y][1].z) {
                    // Let both share one vertex that is average of the two
                    Vertex temp = new Vertex(x, y,
                            (varr[x][y][0].z + varr[x][y][1].z) / 2);
                    varr[x][y][1] = temp;
                    varr[x][y][0] = temp;
                }
            }
        }

        System.out.println("Make Top/Bottom");
        // make triangles for top and bottom surfaces
        for (int col = 0; col < varr.length - 1; col++) {
            for (int row = 0; row < varr[col].length - 1; row++) {

                // create temporary triangles for top and bottom to allow
                // for
                // comparison
                Triangle top1, top2, bottom1, bottom2;
                // create triangles for top
                top1 = new Triangle(varr[col][row][0], varr[col][row + 1][0],
                        varr[col + 1][row][0]);
                top2 = new Triangle(varr[col][row + 1][0],
                        varr[col + 1][row + 1][0], varr[col + 1][row][0]);
                // create triangles for bottom
                bottom1 = new Triangle(varr[col + 1][row][1],
                        varr[col][row + 1][1], varr[col][row][1]);
                bottom2 = new Triangle(varr[col + 1][row][1],
                        varr[col + 1][row + 1][1], varr[col][row + 1][1]);


                // only add triangles if top and bottom do not share same
                // vertices

                if (!top1.equals(bottom1)) {
                    myTriangles.add(top1);
                    myTriangles.add(bottom1);
                }
                if (!top2.equals(bottom2)) {
                    myTriangles.add(top2);
                    myTriangles.add(bottom2);
                }
            }
        }

        System.out.println("Left/right");
        // make triangles for left surface
        // make triangles for right surface
        for (int row = 0; row < varr[0].length - 1; row++) {
            //            // add triangles for the left side
            myTriangles.add(new Triangle(varr[0][row][1], varr[0][row + 1][1],
                    varr[0][row][0]));
            myTriangles.add(new Triangle(varr[0][row + 1][1],
                    varr[0][row + 1][0], varr[0][row][0]));
            // add triangles for the right side
            myTriangles.add(new Triangle(varr[varr.length - 1][row][0],
                    varr[varr.length - 1][row + 1][0],
                    varr[varr.length - 1][row][1]));
            myTriangles.add(new Triangle(varr[varr.length - 1][row + 1][0],
                    varr[varr.length - 1][row + 1][1],
                    varr[varr.length - 1][row][1]));
        }

        System.out.println("front/back");
        // make triangles for the front
        // make triangles for the back
        for (int col = 0; col < varr.length - 1; col++) {
            // add triangles for the front
            myTriangles.add(new Triangle(varr[col][varr[0].length - 1][0],
                    varr[col][varr[0].length - 1][1],
                    varr[col + 1][varr[0].length - 1][0]));
            myTriangles.add(new Triangle(varr[col][varr[0].length - 1][1],
                    varr[col + 1][varr[0].length - 1][1],
                    varr[col + 1][varr[0].length - 1][0]));
            // add triangles for the back
            myTriangles.add(new Triangle(varr[col][0][1], varr[col][0][0],
                    varr[col + 1][0][1]));
            myTriangles.add(new Triangle(varr[col][0][0], varr[col + 1][0][0],
                    varr[col + 1][0][1]));
        }

        System.out.println("Remove extras");
        // Some of the right, front, left, and back triangles have zero
        // area.
        // Remove them.
        ArrayList<Triangle> temp = new ArrayList<Triangle>();
        for (Triangle t : myTriangles)
            if (t.hasArea())
                temp.add(t);
        myTriangles = temp;

        System.out.println("Total Triangles: " + myTriangles.size());

        System.out.println("normalize");
        normalize();

        System.out.println("render");
        //render(myTriangles);

    }


    public boolean hasPath() {
        return outputPath != null;
    }

    public String toString() {
        // output in the ASCII STL format for a solid
        // NOTE: Calls to this method may hang due to memory limits.
        String output = "solid Model \n";
        for (int i = 0; i < myTriangles.size(); i++) {
            output += myTriangles.get(i).toString() + "\n";
        }
        output += "endsolid Model";
        return output;
    }

    /**
     * Finds the amount by which the vertexes need to be offset and scaled to be
     * between 0-1 Normalizes each vertex
     */
    public void normalize() {
        Vertex max = new Vertex(Double.MIN_VALUE, Double.MIN_VALUE,
                Double.MIN_VALUE);
        Vertex min = new Vertex(Double.MAX_VALUE, Double.MAX_VALUE,
                Double.MAX_VALUE);
        HashMap<Vertex, Vertex> usedVerts = new HashMap<Vertex, Vertex>();
        for (Triangle t : myTriangles) {
            usedVerts.put(t.v1, t.v1);
            usedVerts.put(t.v2, t.v2);
            usedVerts.put(t.v3, t.v3);
        }
        Set<Vertex> theVerts = usedVerts.keySet();
        for (Vertex v : theVerts) {
            if (v.x > max.x)
                max.x = v.x;
            if (v.x < min.x)
                min.x = v.x;
            if (v.y > max.y)
                max.y = v.y;
            if (v.y < min.y)
                min.y = v.y;
            if (v.z > max.z)
                max.z = v.z;
            if (v.z < min.z)
                min.z = v.z;
            // find max and min
        }
        Vertex scale = new Vertex(1 / (max.x - min.x), 1 / (max.y - min.y),
                1 / (max.z - min.z));

        // calculate shift and scale
        for (Vertex v : theVerts) {
            v.normalize(min, scale);
        }
    }

    /**
     * writes the model to the path
     */
    public void writeOutput(String fileName) {
        //이 if문은 좀 나중에 해결할 문제임
        //if (outputPath == null)
        //    return;

        try {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/STL");
            if (!dir.exists())
                dir.mkdir();

            PrintWriter writer = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/STL/"+fileName+".stl"), true);
            writer.println("solid Model");
            for (int i = 0; i < myTriangles.size(); i++) {
                writer.println(myTriangles.get(i).toScaledString(scaleX,
                        scaleY, scaleZ));
            }
            writer.println("endsolid Model");
            writer.close();
        } catch (Exception e) {
            Log.e("Exception", "에러: " + e);
        }
        //Runner.update(); 이부분도 에러나서 에러처리
    }

    /**
     * Sets the path for where the file should be saved
     */


}



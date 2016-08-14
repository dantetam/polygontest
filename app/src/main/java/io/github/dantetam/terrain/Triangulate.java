package io.github.dantetam.terrain;

import android.content.SyncAdapterType;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Dante on 8/9/2016.
 */
public class Triangulate {

    /*****************************************************************/
    /** Static class to triangulate any contour/polygon efficiently **/
    /** You should replace Vector2d with whatever your own Vector   **/
    /** class might be.  Does not support polygons with holes.      **/
    /** Uses STL vectors to represent a dynamic array of vertices.  **/
    /** This code snippet was submitted to FlipCode.com by          **/
    /** John W. Ratcliff (jratcliff@verant.com) on July 22, 2000    **/
    /** I did not write the original code/algorithm for this        **/
    /** this triangulator, in fact, I can't even remember where I   **/
    /** found it in the first place.  However, I did rework it into **/
    /** the following black-box static class so you can make easy   **/
    /** use of it in your own code.  Simply replace Vector2d with   **/
    /** whatever your own Vector implementation might be.           **/
    /*****************************************************************/

    static float EPSILON = 0.0000000001f;
    //static float EPSILON = 0.0000001f;

    public static float area(List<Point> contour) {
        int n = contour.size();
        float A = 0f;
        for (int p = n - 1, q = 0; q < n; p = q++) {
            A += contour.get(p).x * contour.get(q).y - contour.get(q).x * contour.get(p).y;
        }
        return A * 0.5f;
    }

    /*
      InsideTriangle decides if a point P is Inside of the triangle
      defined by A, B, C.
    */
    public static boolean insideTriangle(float Ax, float Ay,
                                         float Bx, float By,
                                         float Cx, float Cy,
                                         float Px, float Py) {
        float ax, ay, bx, by, cx, cy, apx, apy, bpx, bpy, cpx, cpy;
        float cCROSSap, bCROSScp, aCROSSbp;

        ax = Cx - Bx;
        ay = Cy - By;
        bx = Ax - Cx;
        by = Ay - Cy;
        cx = Bx - Ax;
        cy = By - Ay;
        apx = Px - Ax;
        apy = Py - Ay;
        bpx = Px - Bx;
        bpy = Py - By;
        cpx = Px - Cx;
        cpy = Py - Cy;

        aCROSSbp = ax * bpy - ay * bpx;
        cCROSSap = cx * apy - cy * apx;
        bCROSScp = bx * cpy - by * cpx;

        return ((aCROSSbp >= 0.0f) && (bCROSScp >= 0.0f) && (cCROSSap >= 0.0f));
    }

    public static boolean snip(List<Point> contour, int u, int v, int w, int n, int[] V) {
        int p;
        float Ax, Ay, Bx, By, Cx, Cy, Px, Py;

        Ax = contour.get(V[u]).x;
        Ay = contour.get(V[u]).y;

        Bx = contour.get(V[v]).x;
        By = contour.get(V[v]).y;

        Cx = contour.get(V[w]).x;
        Cy = contour.get(V[w]).y;

        if ((((Bx - Ax) * (Cy - Ay)) - ((By - Ay) * (Cx - Ax))) < EPSILON) return false;

        for (p = 0; p < n; p++) {
            if ((p == u) || (p == v) || (p == w)) continue;
            Px = contour.get(V[p]).x;
            Py = contour.get(V[p]).y;
            if (insideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px, Py)) return false;
        }

        return true;
    }

    public static List<Point> process(List<Point> contour) {
        /* allocate and initialize list of Vertices in polygon */

        for (Point p: contour) {
            int matches = 0;
            for (Point q: contour) {
                if (p.equals(q)) {
                    matches++;
                }
            }
            if (matches > 1) {
                System.out.println("Duplicate point!");
            }
        }

        int i = 0;
        for (Point p: contour) {
            System.out.print(p.toString() + " ");
            i++;
        }
        System.out.println("--------");

        List<Point> result = new ArrayList<>();

        int n = contour.size();
        if (n < 3) return null;

        int[] V = new int[n];

        /* we want a counter-clockwise polygon in V */

        if (area(contour) > 0.0f)
            for (int v = 0; v < n; v++) V[v] = v;
        else
            for (int v = 0; v < n; v++) V[v] = (n - 1) - v;

        int nv = n;

        /*  remove nv-2 Vertices, creating 1 triangle every time */
        int count = 2 * nv;   /* error detection */

        for (int m = 0, v = nv - 1; nv > 2; ) {
        /* if we loop, it is probably a non-simple polygon */
            if (0 >= (count--)) {
                System.out.println("Triangulate: ERROR - probable bad polygon!");
                return null;
            }

            /* three consecutive vertices in current polygon, <u,v,w> */
            int u = v;
            if (nv <= u) u = 0;     /* previous */
            v = u + 1;
            if (nv <= v) v = 0;     /* new v    */
            int w = v + 1;
            if (nv <= w) w = 0;     /* next     */

            if (snip(contour, u, v, w, nv, V)) {
                int a, b, c, s, t;

                /* true names of the vertices */
                a = V[u];
                b = V[v];
                c = V[w];

                /* output Triangle */
                result.add(contour.get(a));
                result.add(contour.get(b));
                result.add(contour.get(c));

                m++;

                /* remove v from remaining polygon */
                for (s = v, t = v + 1; t < nv; s++, t++)
                    V[s] = V[t];
                nv--;

                /* resest error detection counter */
                count = 2 * nv;
            }
        }

        return result;
    }


    /************************************************************************/
    /*** END OF CODE SECTION TRIANGULATE.CPP BEGINNING OF TEST.CPP A SMALL **/
    /*** TEST APPLICATION TO DEMONSTRATE THE USAGE OF THE TRIANGULATOR     **/
    /************************************************************************/

    public static void main(String[] args) {

        // Small test application demonstrating the usage of the triangulate
        // class.


        // Create a pretty complicated little contour by pushing them onto
        // an stl vector.

        List<Point> a = new ArrayList<>();

        /*a.add(new Point(0, 6));
        a.add(new Point(0, 0));
        a.add(new Point(3, 0));
        a.add(new Point(4, 1));
        a.add(new Point(6, 1));
        a.add(new Point(8, 0));
        a.add(new Point(12, 0));
        a.add(new Point(13, 2));
        a.add(new Point(8, 2));
        a.add(new Point(8, 4));
        a.add(new Point(11, 4));
        a.add(new Point(11, 6));
        a.add(new Point(6, 6));
        a.add(new Point(4, 3));
        a.add(new Point(2, 6));*/

        a.add(new Point(0, 0));
        a.add(new Point(0, 2));
        a.add(new Point(0, 4));
        a.add(new Point(1, 4));
        a.add(new Point(1, 2));
        a.add(new Point(2, 2));
        a.add(new Point(2, 0));

        // allocate an STL vector to hold the answer.

        //List<Vector2f> result = new ArrayList<>();

        //  Invoke the triangulator to triangulate this polygon.
        List<Point> result = process(a);

        // print out the results.
        int tcount = result.size() / 3;

        for (int i = 0; i < tcount; i++) {
            Point p1 = result.get(i * 3 + 0);
            Point p2 = result.get(i * 3 + 1);
            Point p3 = result.get(i * 3 + 2);
            System.out.println("Triangle: " + p1.toString() + "; " + p2.toString() + "; " + p3.toString());
        }

        System.exit(0);
    }

}

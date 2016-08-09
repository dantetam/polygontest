package io.github.dantetam.terrain;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by Dante on 8/9/2016.
 */
public class Triangulate {

    // COTD Entry submitted by John W. Ratcliff [jratcliff@verant.com]

// ** THIS IS A CODE SNIPPET WHICH WILL EFFICIEINTLY TRIANGULATE ANY
// ** POLYGON/CONTOUR (without holes) AS A STATIC CLASS.  THIS SNIPPET
// ** IS COMPRISED OF 3 FILES, TRIANGULATE.H, THE HEADER FILE FOR THE
// ** TRIANGULATE BASE CLASS, TRIANGULATE.CPP, THE IMPLEMENTATION OF
// ** THE TRIANGULATE BASE CLASS, AND TEST.CPP, A SMALL TEST PROGRAM
// ** DEMONSTRATING THE USAGE OF THE TRIANGULATOR.  THE TRIANGULATE
// ** BASE CLASS ALSO PROVIDES TWO USEFUL HELPER METHODS, ONE WHICH
// ** COMPUTES THE AREA OF A POLYGON, AND ANOTHER WHICH DOES AN EFFICENT
// ** POINT IN A TRIANGLE TEST.
// ** SUBMITTED BY JOHN W. RATCLIFF (jratcliff@verant.com) July 22, 2000

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

    //static float EPSILON = 0.0000000001f;
    static float EPSILON = 0.0000001f;

    public static float area(List<Vector2f> contour) {
        int n = contour.size();
        float A = 0f;
        for (int p = n - 1, q = 0; q < n; p = q++) {
            A += contour.get(p).x*contour.get(q).y - contour.get(q).x*contour.get(p).y;
        }
        return A*0.5f;
    }

    /*
      InsideTriangle decides if a point P is Inside of the triangle
      defined by A, B, C.
    */
    public static boolean insideTriangle(float Ax, float Ay,
                                     float Bx, float By,
                                     float Cx, float Cy,
                                     float Px, float Py)
    {
        float ax, ay, bx, by, cx, cy, apx, apy, bpx, bpy, cpx, cpy;
        float cCROSSap, bCROSScp, aCROSSbp;

        ax = Cx - Bx; ay = Cy - By;
        bx = Ax - Cx; by = Ay - Cy;
        cx = Bx - Ax; cy = By - Ay;
        apx= Px - Ax; apy= Py - Ay;
        bpx= Px - Bx; bpy= Py - By;
        cpx= Px - Cx; cpy= Py - Cy;

        aCROSSbp = ax*bpy - ay*bpx;
        cCROSSap = cx*apy - cy*apx;
        bCROSScp = bx*cpy - by*cpx;

        return ((aCROSSbp >= 0.0f) && (bCROSScp >= 0.0f) && (cCROSSap >= 0.0f));
    };

    public static boolean snip(List<Vector2f> contour, int u, int v, int w, int n, int[] V)
    {
        int p;
        float Ax, Ay, Bx, By, Cx, Cy, Px, Py;

        Ax = contour.get(V[u]).x;
        Ay = contour.get(V[u]).y;

        Bx = contour.get(V[v]).x;
        By = contour.get(V[v]).y;

        Cx = contour.get(V[w]).x;
        Cy = contour.get(V[w]).y;

        if ( EPSILON > (((Bx-Ax)*(Cy-Ay)) - ((By-Ay)*(Cx-Ax))) ) return false;

        for (p=0;p<n;p++)
        {
            if( (p == u) || (p == v) || (p == w) ) continue;
            Px = contour.get(V[p]).x;
            Py = contour.get(V[p]).y;
            if (insideTriangle(Ax, Ay, Bx, By, Cx, Cy, Px, Py)) return false;
        }

        return true;
    }

    public static boolean process(List<Vector2f> contour, List<Vector2f> result)
    {
  /* allocate and initialize list of Vertices in polygon */

        int n = contour.size();
        if ( n < 3 ) return false;

        int[] V = new int[n];

  /* we want a counter-clockwise polygon in V */

        if ( 0.0f < area(contour) )
            for (int v=0; v<n; v++) V[v] = v;
        else
            for(int v=0; v<n; v++) V[v] = (n-1)-v;

        int nv = n;

  /*  remove nv-2 Vertices, creating 1 triangle every time */
        int count = 2*nv;   /* error detection */

        for (int m = 0, v = nv - 1; nv > 2; )
        {
    /* if we loop, it is probably a non-simple polygon */
            if (0 >= (count--))
            {
                //** Triangulate: ERROR - probable bad polygon!
                return false;
            }

    /* three consecutive vertices in current polygon, <u,v,w> */
            int u = v; if (nv <= u) u = 0;     /* previous */
            v = u+1; if (nv <= v) v = 0;     /* new v    */
            int w = v+1; if (nv <= w) w = 0;     /* next     */

            if ( snip(contour, u, v, w, nv, V) )
            {
                int a,b,c,s,t;

      /* true names of the vertices */
                a = V[u]; b = V[v]; c = V[w];

      /* output Triangle */
                result.add(contour.get(a));
                result.add( contour.get(b) );
                result.add(contour.get(c));

                m++;

      /* remove v from remaining polygon */
                for (s = v, t = v+1 ; t < nv; s++, t++)
                    V[s] = V[t];
                nv--;

      /* resest error detection counter */
                count = 2*nv;
            }
        }

        return true;
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

        List<Vector2f> a = new ArrayList<>();

        a.add(new Vector2f(0, 6));
        a.add(new Vector2f(0, 0));
        a.add(new Vector2f(3, 0));
        a.add(new Vector2f(4, 1));
        a.add(new Vector2f(6, 1));
        a.add(new Vector2f(8, 0));
        a.add(new Vector2f(12, 0));
        a.add(new Vector2f(13, 2));
        a.add(new Vector2f(8, 2));
        a.add(new Vector2f(8, 4));
        a.add(new Vector2f(11, 4));
        a.add(new Vector2f(11, 6));
        a.add(new Vector2f(6, 6));
        a.add(new Vector2f(4, 3));
        a.add(new Vector2f(2, 6));

        // allocate an STL vector to hold the answer.

        List<Vector2f> result = new ArrayList<>();

        //  Invoke the triangulator to triangulate this polygon.
        process(a, result);

        // print out the results.
        int tcount = result.size() / 3;

        for (int i = 0; i < tcount; i++) {
            Vector2f p1 = result.get(i * 3 + 0);
            Vector2f p2 = result.get(i * 3 + 1);
            Vector2f p3 = result.get(i * 3 + 2);
            System.out.println("Triangle: " + p1.toString() + "; " + p2.toString() + "; " + p3.toString());
        }
    }

}

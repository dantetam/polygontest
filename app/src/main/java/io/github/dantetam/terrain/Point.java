package io.github.dantetam.terrain;

/**
 * Created by Dante on 8/9/2016.
 */
public class Point {
    public float x,y,z;
    public float[] texData;
    public Point(float a, float b) {
        this(a, b, 0);
    }
    public Point(float a, float b, float c) {
        x = a; y = b; z = c;
        texData = new float[2];
    }
    public float dist(Point p) {
        return (float) Math.sqrt((x - p.x)*(x - p.x) + (y - p.y)*(y - p.y) + (z - p.z)*(z - p.z));
    }
    public Point add(Point p) {
        return new Point(x + p.x, y + p.y, z + p.z);
    }
    public String toString() {
        return "(Point: " + x + ", " + y + ", " + z + ")";
    }
    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) {
            return false;
        }
        Point p = (Point) obj;
        return x == p.x && y == p.y && z == p.z;
    }
}

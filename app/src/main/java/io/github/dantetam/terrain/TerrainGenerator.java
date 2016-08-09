package io.github.dantetam.terrain;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dante on 8/6/2016.
 */
public class TerrainGenerator {

    public Point[][] allPoints;
    public List<Polygon> hexes;

    public TerrainGenerator(int len, float scale) {
        hexagonProcess(len, scale);
    }

    public void hexagonProcess(int len, float scale) {
        allPoints = new Point[len][len];
        hexes = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                float x = scale*i*0.5f, y = scale*j*0.75f;
                if ((j % 2 == 0 && i % 2 == 1) || (j % 2 == 1 && i % 2 == 0)) x -= scale*0.125f;
                else x += scale*0.125f;
                allPoints[i][j] = new Point(x, y, 0);
                allPoints[i][j].texData = new float[]{x / (scale*0.5f*len), y / (scale*0.75f*len)};
            }
        }
        for (int r = 0; r < len - 2; r += 2) {
            for (int c = 0; c < len - 2; c += 2) {
                PointList hex = new PointList();
                hex.add(allPoints[r][c]);
                hex.add(allPoints[r][c + 1]);
                hex.add(allPoints[r][c + 2]);
                hex.add(allPoints[r + 1][c + 2]);
                hex.add(allPoints[r + 1][c + 1]);
                hex.add(allPoints[r + 1][c]);
                hexes.add(new Polygon(hex));
            }
        }
        for (int r = 1; r < len - 2; r += 2) {
            for (int c = 1; c < len - 2; c += 2) {
                PointList hex = new PointList();
                hex.add(allPoints[r][c]);
                hex.add(allPoints[r][c + 1]);
                hex.add(allPoints[r][c + 2]);
                hex.add(allPoints[r + 1][c + 2]);
                hex.add(allPoints[r + 1][c + 1]);
                hex.add(allPoints[r + 1][c]);
                hexes.add(new Polygon(hex));
            }
        }
    }

    public static int SEGMENTS = 16, SAMPLE_SEGMENT = 4;
    public List<Edge> roughEdge(Edge edge) {
        float[] newEdgeData = new PerlinNoiseLine(870).generatePerlinLine(SEGMENTS, 0.5f, 0.25f);
        //float slope = edge.slope();
        float normalSlope = - 1f / edge.slope();
        float angle = (float) Math.atan(normalSlope);
        //float len = edge.length();
        List<Point> points = new ArrayList<>();
        for (int i = SAMPLE_SEGMENT / 2; i < SEGMENTS; i += SAMPLE_SEGMENT) {
            float mid = (float) i / (float) SEGMENTS;
            Point between = edge.inBetween(mid);
            Point newPoint = between.add(new Point(newEdgeData[i]*(float)Math.cos(angle), newEdgeData[i]*(float)Math.sin(angle), 0));
            points.add(newPoint);
        }
        points.add(0, edge.edge0());
        points.add(edge.edge1());
        List<Edge> results = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            results.add(new Edge(points.get(i), points.get(i + 1)));
        }
        return results;
    }

    public class EdgeGroup {

    }

    public class Edge {
        private Point edge0, edge1;
        public float slope;
        public Edge(Point p0, Point p1) {
            if (p0.x < p1.x) {
                edge0 = p0;
                edge1 = p1;
            }
            else {
                edge0 = p1;
                edge1 = p0;
            }
            if (edge0.x < edge1.x) {
                slope = (edge1.y - edge0.y) / (edge1.x - edge0.x);
            }
            else {
                slope = (edge1.y - edge0.y) / (edge0.x - edge1.x);
            }
        }
        public Point edge0() {return edge0;}
        public Point edge1() {return edge1;}
        public float slope() {
            return slope;
        }
        public float length() {
            return edge0.dist(edge1);
        }
        public Point inBetween(float percent) {
            float offX = (edge1.x - edge0.x) * percent;
            float offY = (edge1.y - edge0.y) * percent;
            float offZ = (edge1.z - edge0.z) * percent;
            return new Point(edge0.x + offX, edge1.y + offY, edge0.z + offZ);
        }
    }

    //Work around type erasure for generics.
    public class PointList extends ArrayList<Point> {}
    public class EdgeList extends ArrayList<Edge> {}

    public class Polygon {
        public List<Polygon> neighbors;
        public List<Point> points;
        public List<Edge> edges;

        public Polygon(PointList createFromPoints) {
            neighbors = new ArrayList<>();
            points = createFromPoints;
            edges = new ArrayList<>();
            for (int i = 0; i < points.size() - 1; i++) {
                edges.add(new Edge(points.get(i), points.get(i + 1)));
            }
            edges.add(new Edge(points.get(points.size() - 1), points.get(0)));
        }

        public Polygon(EdgeList createFromEdges) {
            neighbors = new ArrayList<>();
            edges = createFromEdges;
            points = new ArrayList<>();
            for (Edge edge: edges) {
                points.add(edge.edge0());
            }
        }

        public float[][] getVertexData() {
            List<Point> twoDimensionPoints = new ArrayList<>();
            for (Point point: points) {
                twoDimensionPoints.add(point);
            }
            List<Point> result = Triangulate.process(twoDimensionPoints);
            float[] vertices = new float[result.size() * 3];
            for (int i = 0; i < result.size(); i++) {
                Point vertex = result.get(i);
                vertices[3*i] = vertex.y;
                vertices[3*i + 1] = vertex.x;
                vertices[3*i + 2] = 0;
            }
            float[] textures = new float[result.size() * 2];
            for (int i = 0; i < result.size(); i++) {
                Point vertex = result.get(i);
                textures[2*i] = vertex.texData[0];
                textures[2*i + 1] = vertex.texData[1];
            }
            return new float[][]{vertices, textures};
        }

        public float[][] getHexagonData() {
            int[] indices = {5, 1, 0, 4, 1, 5, 4, 2, 1, 4, 3, 2};
            //int[] indices = {0, 1, 2, 3, 4, 5};

            float[] vertices = new float[indices.length * 3];

            for (int i = 0; i < indices.length; i++) {
                Point vertex = points.get(indices[i]);
                vertices[3*i] = vertex.y;
                vertices[3*i + 1] = vertex.x;
                vertices[3*i + 2] = vertex.z;
            }
            float[] textures = new float[indices.length * 2];
            for (int i = 0; i < indices.length; i++) {
                Point vertex = points.get(indices[i]);
                textures[2*i] = vertex.texData[0];
                textures[2*i + 1] = vertex.texData[1];
            }
            return new float[][]{vertices, textures};
        }
    }

}

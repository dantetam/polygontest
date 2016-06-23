package io.github.dantetam.opstrykontest;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ObjLoader {

    public static Solid loadSolid(final Context context,
                                  final int resourceId)
    {
        float[][] data = loadObjModel(context, resourceId);
        Solid solid = new Solid(data[0], data[1], data[2], 1);
        solid.numVerticesToRender = data[0].length;
        return solid;
    }

    public static float[][] loadObjModel(final Context context,
        final int resourceId)
    {
        final InputStream inputStream = context.getResources().openRawResource(
                resourceId);
        final InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream);
        final BufferedReader bufferedReader = new BufferedReader(
                inputStreamReader);

        String nextLine;
        final StringBuilder body = new StringBuilder();

        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line;
        ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
        ArrayList<Vector2f> textures = new ArrayList<Vector2f>();
        ArrayList<Vector3f> normals = new ArrayList<Vector3f>();
        ArrayList<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray, normalsArray = null, textureArray = null;
        int[] indicesArray;
        try
        {
            while (true)
            {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) //vertex position
                {
                    Vector3f vertex = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );
                    vertices.add(vertex);
                }
                else if (line.startsWith("vt ")) //texture coordinate
                {
                    Vector2f texture = new Vector2f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2])
                    );
                    textures.add(texture);
                }
                else if (line.startsWith("vn ")) //normal
                {
                    Vector3f vertex = new Vector3f(
                            Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3])
                    );
                    normals.add(vertex);
                }
                else if (line.startsWith("f ")) //face object
                {
                    //All the v, vt, vn lines have been passed, end the loop
                    textureArray = new float[vertices.size()*2];
                    normalsArray = new float[vertices.size()*3];
                    break;
                }
            }

            while (line != null)
            {
                //Make sure a face line is being read
                if (!line.startsWith("f "))
                {
                    line = reader.readLine();
                    continue;
                }
                //A face is in the from f x/y/z a/b/c d/e/f
                //Split into these 4 sections
                //and then split the sections by slashes to get the numbers x, y, z, etc.
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1,indices,textures,normals,textureArray,normalsArray);
                processVertex(vertex2,indices,textures,normals,textureArray,normalsArray);
                processVertex(vertex3,indices,textures,normals,textureArray,normalsArray);
                line = reader.readLine();
            }
            reader.close();

        } catch(Exception e) {e.printStackTrace();}

        verticesArray = new float[vertices.size()*3]; //Convert lists to array
        indicesArray = new int[indices.size()];
        int vertexPointer = 0;
        for (Vector3f vertex : vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return new float[][]{verticesArray, normalsArray, textureArray};
    }

    private static void processVertex(
            String[] vertexData,
            ArrayList<Integer> indices,
            ArrayList<Vector2f> textures,
            ArrayList<Vector3f> normals,
            float[] textureArray,
            float[] normalsArray)
    {
        int currentVertex = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertex);

        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertex*2] = currentTex.x;
        textureArray[currentVertex*2 + 1] = 1 - currentTex.y; //Blender and OpenGL convention about xy coordinate system

        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalsArray[currentVertex*3] = currentNorm.x;
        normalsArray[currentVertex*3 + 1] = currentNorm.y;
        normalsArray[currentVertex*3 + 2] = currentNorm.z;
    }

    public static class Vector2f {
        public float x,y;
        public Vector2f(float a, float b) {x = a; y = b;}
    }
    public static class Vector3f {
        public float x,y,z;
        public Vector3f(float a, float b, float c) {x = a; y = b; z = c;}
    }

}
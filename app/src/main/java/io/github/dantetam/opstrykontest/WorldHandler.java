package io.github.dantetam.opstrykontest;

import java.util.ArrayList;
import java.util.List;

import io.github.dantetam.world.*;

/**
 * Created by Dante on 6/17/2016.
 * A connection between the classes of the world package that store world data,
 * and the OpenGL classes that render the world.
 */
public class WorldHandler {

    public World world;
    public WorldGenerator worldGenerator;
    private Model tilesStored = null;

    private LessonSevenActivity mActivity;

    static final int POSITION_DATA_SIZE = 3;
    static final int NORMAL_DATA_SIZE = 3;
    static final int TEXTURE_COORDINATE_DATA_SIZE = 2;
    static final int BYTES_PER_FLOAT = 4;

    public WorldHandler(LessonSevenActivity mActivity) {
        world = new World(LessonSevenRenderer.WORLD_LENGTH, LessonSevenRenderer.WORLD_LENGTH);
        worldGenerator = new WorldGenerator(world);
        worldGenerator.init();
        this.mActivity = mActivity;
    }

    public Model worldRep() {
        if (tilesStored == null) {
            tilesStored = new Model();
            tilesStored.add(generateHexes(world));
        }
        return tilesStored;
    }

    private static float tileWidth = 4;
    /*private Model[][] tileRep() {
        if (tilesStored == null) {
            tilesStored = new Model[world.rows][world.cols];
            for (int r = 0; r < world.rows; r++) {
                for (int c = 0; c < world.cols; c++) {
                    Tile tile = world.getTile(r, c);
                    Solid solid = new Solid();
                    solid.move(r * tileWidth, 0.5f * (float) (tile.elevation), c * tileWidth);
                    solid.scale(tileWidth, tile.elevation, tileWidth);
                    solid.rotate(0, 0, 1, 0);
                    solid.color(Tile.Biome.colorFromInt(tile.biome.type));
                    tilesStored[r][c] = new Model();
                    tilesStored[r][c].add(solid);
                }
            }
        }
        return tilesStored;
    }*/

    private Solid generateAllHexes(World world) {
        LessonSevenRenderer.Condition cond = new LessonSevenRenderer.Condition() {
            public boolean allowed
        }
    }

    private Solid generateHexes(World world, LessonSevenRenderer.Condition condition) {
        float[][] hexData = ObjLoader.loadObjModelByVertex(mActivity, R.raw.hexagon);

        //int mRequestedCubeFactor = WORLD_LENGTH;

        int numHexesToRender = 0;
        for (int x = 0; x < world.arrayLengthX; x++) {
            for (int z = 0; z < world.arrayLengthZ; z++) {
                Tile tile = world.getTile(x,z);
                if (tile == null) continue;
            }
        }

        final float[] totalCubePositionData = new float[hexData[0].length * world.getNumHexes()];
        int cubePositionDataOffset = 0;

        final float[] totalNormalPositionData = new float[hexData[0].length / POSITION_DATA_SIZE * NORMAL_DATA_SIZE * world.getNumHexes()];
        int cubeNormalDataOffset = 0;
        final float[] totalTexturePositionData = new float[hexData[0].length / POSITION_DATA_SIZE * TEXTURE_COORDINATE_DATA_SIZE * world.getNumHexes()];
        int cubeTextureDataOffset = 0;

        final float TRANSLATE_FACTOR = 3;

        for (int x = 0; x < world.arrayLengthX; x++) {
            for (int z = 0; z < world.arrayLengthZ; z++) {
                Tile tile = world.getTile(x,z);
                if (tile == null) continue;

                float extra = x % 2 == 1 ? TRANSLATE_FACTOR*-0.5f : 0;
                final float[] thisCubePositionData = translateData(hexData[0], x*TRANSLATE_FACTOR, 0, z*TRANSLATE_FACTOR + extra);

                System.arraycopy(thisCubePositionData, 0, totalCubePositionData, cubePositionDataOffset, thisCubePositionData.length);
                cubePositionDataOffset += thisCubePositionData.length;

                System.arraycopy(hexData[1], 0, totalNormalPositionData, cubeNormalDataOffset, hexData[1].length);
                cubeNormalDataOffset += hexData[1].length;
                System.arraycopy(hexData[2], 0, totalTexturePositionData, cubeTextureDataOffset, hexData[2].length);
                cubeTextureDataOffset += hexData[2].length;
            }
            //}
        }

        //return new float[][]{totalCubePositionData, totalNormalPositionData, totalTexturePositionData};
        return ObjLoader.loadSolid(new float[][]{totalCubePositionData, totalNormalPositionData, totalTexturePositionData});
    }

    private float[] translateData(float[] data, float dx, float dy, float dz) {
        float[] newData = new float[data.length];
        for (int i = 0; i < data.length; i++) {
            if (i % 3 == 0) newData[i] = data[i] + dx;
            else if (i % 3 == 1) newData[i] = data[i] + dy;
            else newData[i] = data[i] + dz;
        }
        return newData;
    }

    public List<Object> concat(List<Object> a, List<Object> b) {
        List<Object> combined = new ArrayList<Object>();
        for (Object o: a) combined.add(o);
        for (Object o: b) combined.add(o);
        return combined;
    }

}

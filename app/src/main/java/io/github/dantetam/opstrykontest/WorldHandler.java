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
    private Model[][] tilesStored = null;

    public WorldHandler() {
        world = new World(33, 33);
        worldGenerator = new WorldGenerator(world);
        worldGenerator.init();
    }

    public List<Model> worldRep() {
        List<Model> list = new ArrayList<Model>();
        Model[][] tiles = tileRep();
        for (int r = 0; r < tiles.length; r++) {
            for (int c = 0; c < tiles[0].length; c++) {
                list.add(tiles[r][c]);
            }
        }
        return list;
    }

    private static float tileWidth = 4;
    private Model[][] tileRep() {
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
    }

    public List<Object> concat(List<Object> a, List<Object> b) {
        List<Object> combined = new ArrayList<Object>();
        for (Object o: a) combined.add(o);
        for (Object o: b) combined.add(o);
        return combined;
    }

}

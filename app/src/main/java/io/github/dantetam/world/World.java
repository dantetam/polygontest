package io.github.dantetam.world;

import java.util.ArrayList;

/**
 * Created by Dante on 6/13/2016.
 */
public class World {

    //private QuadTree<Tile, int[]> tiles;
    //private WorldTree tree;
    protected Tile[][] tiles;
    public int rows, cols;

    public World(int totalRows, int totalCols) {
        //tree = new WorldTree();
        tiles = new Tile[totalRows][totalCols];
        rows = totalRows; cols = totalCols;
    }

    public void init(int[][] biomes, int[][] terrain, Tile.Resource[][] resources, int[][] elevations) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile tile = new Tile(r, c);
                tile.biome = Tile.Biome.fromInt(biomes[r][c]);
                tile.terrain = Tile.Terrain.fromInt(terrain[r][c]);
                tile.resources = new ArrayList<Tile.Resource>();
                //tile.resources.add(Tile.Resource.fromInt(resources[r][c]));
                tile.resources.add(resources[r][c]);
                tile.elevation = elevations[r][c];
                tiles[r][c] = tile;
            }
        }
    }

    public Tile getTile(int r, int c) {
        if (r < 0 || c < 0 || r >= tiles.length || c >= tiles[0].length) {
            throw new IllegalArgumentException("Out of bounds or degenerate grid");
        }
        return tiles[r][c];
    }

}

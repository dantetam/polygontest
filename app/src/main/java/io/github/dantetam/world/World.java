package io.github.dantetam.world;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dante on 6/13/2016.
 */
public class World {

    //private QuadTree<Tile, int[]> tiles;
    //private WorldTree tree;
    protected Tile[][] hexes;
    public int totalX, totalZ;

    //x represents height, z represents length
    public World(int q, int r) {
        //tree = new WorldTree();
        hexes = new Tile[r][q + r/2];
        this.totalX = q; this.totalZ = r;
        int startingZ = q + r/2 - 1;
        for (int x = 0; x < r; x++) {
            for (int z = startingZ; z >= startingZ - r; z--) {
                hexes[x][z] = new Tile(x, z);
            }
            if (x % 2 == 1) {
                startingZ--;
            }
        }

        for (int i = 0; i < hexes.length; i++) {
            for (int j = 0; j < hexes[0].length; j++) {
                String stringy = hexes[i][j] != null ? "X" : "-";
                System.out.print(stringy + " ");
            }
            System.out.println();
        }
    }

    public void init(int[][] biomes, int[][] terrain, Tile.Resource[][] resources, int[][] elevations) {
        /*for (int r = 0; r < rows; r++) {
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
        }*/
    }

    public Tile getTile(int r, int c) {
        if (r < 0 || c < 0 || r >= hexes.length || c >= hexes[0].length) {
            //throw new IllegalArgumentException("Out of bounds or degenerate grid");
            return null;
        }
        return hexes[r][c];
    }


    public static final int[][] neighborDirections = {
            {1, 0}, {1, -1}, {0, -1},
            {-1, 0}, {-1, 1}, {0, 1}
    };
    public List<Tile> neighbors(Tile t) {
        List<Tile> temp = new ArrayList<Tile>();
        for (int i = 0; i < neighborDirections.length; i++) {
            Tile candidate = getTile(t.q + neighborDirections[i][0], t.r + neighborDirections[i][1]);
            if (candidate != null) temp.add(candidate);
        }
        return temp;
    }

}

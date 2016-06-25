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
    public int arrayLengthX, arrayLengthZ;
    public int totalX, totalZ;
    private int numHexes = -1;

    //x represents height, z represents length
    public World(int q, int r) {
        //tree = new WorldTree();
        hexes = new Tile[r][q + r/2];
        this.totalX = q; this.totalZ = r;
        this.arrayLengthX = r; this.arrayLengthZ = q + r/2;
        int startingZ = q + r/2 - 1;
        numHexes = 0;
        for (int x = 0; x < r; x++) {
            for (int z = startingZ; z >= startingZ - r; z--) {
                hexes[x][z] = new Tile(this, x, z);
                numHexes++;
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
        for (int r = 0; r < biomes.length; r++) {
            for (int c = 0; c < biomes[0].length; c++) {
                //Tile tile = new Tile(r, c);
                Tile tile = getTile(r,c);
                if (tile == null) continue;
                tile.biome = Tile.Biome.fromInt(biomes[r][c]);
                tile.terrain = Tile.Terrain.fromInt(terrain[r][c]);
                tile.resources = new ArrayList<Tile.Resource>();
                //tile.resources.add(Tile.Resource.fromInt(resources[r][c]));
                tile.resources.add(resources[r][c]);
                tile.elevation = elevations[r][c];
            }
        }
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

    public int getNumHexes() {
        if (numHexes == -1) {
            throw new RuntimeException("World has not been initialized");
        }
        return numHexes;
    }

}

package io.github.dantetam.opstrykontest;

import io.github.dantetam.world.DiamondSquare;
import io.github.dantetam.world.Tile;
import io.github.dantetam.world.World;

/**
 * Created by Dante on 6/17/2016.
 */
public class WorldGenerator {

    private World world;

    public WorldGenerator(World w) {
        world = w;
    }

    public void init() {
        int width = Math.max(world.totalX, world.totalZ);
        int[][] biomes = new DiamondSquare(width, 10, 0.4).seed(870).getIntTerrain(0, Tile.Biome.numBiomes - 1);
        int[][] terrains = new DiamondSquare(width, 10, 0.4).seed(0417).getIntTerrain(0, Tile.Terrain.numTerrains - 1);
        Tile.Resource[][] resources = makeNewResources(width, width);
        int[][] elevations = new DiamondSquare(width, 10, 0.5).seed(916).getIntTerrain(1, 10);
        world.init(biomes, terrains, resources, elevations);
    }

    private Tile.Resource[][] makeNewResources(int rows, int cols) {
        Tile.Resource[][] temp = new Tile.Resource[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Tile.Resource index;
                if (Math.random() < 0.1) {
                    index = Tile.Resource.randomResource();
                }
                else {
                    index = Tile.Resource.NO_RESOURCE;
                }
                temp[r][c] = index;
            }
        }
        return temp;
    }

}

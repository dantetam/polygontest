package io.github.dantetam.world;

import java.util.HashMap;

/**
 * Created by Dante on 6/16/2016.
 */
public class Building extends Entity {

    public enum BuildingType {
        WHEAT_PLOT (0),
        SHALLOW_MINE (1);
        public int id;
        BuildingType(int t) {id = t;}
        private static BuildingType[] rawTypes = BuildingType.class.getEnumConstants();
        public static HashMap<Integer, BuildingType> types = null;
        public static BuildingType fromInt(int n) {
            if (types == null) {
                init();
            }
            if (types.containsKey(n)) {
                return types.get(n);
            }
            throw new IllegalArgumentException("Invalid terrain type: " + n);
        }
        private static void init() {
            types = new HashMap<>();
            for (int i = 0; i < rawTypes.length; i++) {
                types.put(rawTypes[i].id, rawTypes[i]);
            }
        }
        private static final int numBuildingTypes = 0;
        public static final int getNumBuildingTypes() {
            if (numBuildingTypes == 0) init();
            return types.size();
        }
    }

    public Building() {

    }

}

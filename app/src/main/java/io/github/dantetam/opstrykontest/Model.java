package io.github.dantetam.opstrykontest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dante on 6/18/2016.
 */
public class Model {

    public List<Solid> parts;

    public Model() {
        parts = new ArrayList<Solid>();
    }

    public void add(Solid solid) {
        parts.add(solid);
    }

}

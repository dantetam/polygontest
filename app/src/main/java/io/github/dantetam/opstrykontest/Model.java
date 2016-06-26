package io.github.dantetam.opstrykontest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dante on 6/18/2016.
 */
public class Model {

    public List<RenderEntity> parts;

    public Model() {
        parts = new ArrayList<RenderEntity>();
    }

    public void add(Solid solid) {
        parts.add(solid);
    }

    public void release() {
        for (RenderEntity solid: parts) {
            solid.release();
        }
    }

}

package io.github.dantetam.world;

import java.util.List;

/**
 * Created by Dante on 6/23/2016.
 */
public interface Traversable {

    public double dist(Traversable t);
    public List<Traversable> neighbors();

}

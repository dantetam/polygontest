package io.github.dantetam.world;

import java.util.List;

/**
 * Created by Dante on 6/23/2016.
 */
public interface Traversable<T> {

    public float dist(T t);
    public List<T> neighbors();

}

package io.github.dantetam.world;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dante on 6/13/2016.
 */
public class Clan {

    public String name;
    public List<Person> people;

    public Clan(String n) {
        name = n;
        people = new ArrayList<Person>();
    }

}

package com.assanguino.adventofcode2021;

import java.util.List;

import java.util.ArrayList;

public class Path {

    public final static String START_CAVE = "start";
    public final static String END_CAVE = "end";
   
    protected List<String> path = new ArrayList<String>();
    protected List<String> small_caves = new ArrayList<String>();
    // store the name if the small cave that has benn visited twice
    protected String repeated_small_cave = "";

    public Path() { }

    public Path(String cave) {
        path.add(cave);
    }

    // copy constructor
    public Path(Path path) {
        for(String node : path.getPath()) {
            this.path.add(node);
            addSmallCave(node);
        }
    }

    public List<String> getPath() {
        return path;
    }

    public void add(String cave) {
        path.add(cave);
        addSmallCave(cave);
    }

    /**
     * True if te cave is a small one (just a lower case letter)
     * @param cave
     * @return
     */
    protected boolean isSmallCave(String cave) {
        return cave.equals(cave.toLowerCase());
    }

    /**
     * True if the cave if the 'start' or the 'end' cave
     * @param cave
     * @return
     */
    protected boolean isBoundCave(String cave) {
        return cave.equals(START_CAVE) || cave.equals(END_CAVE);
    }

    /**
     * Add the cave to the list of small caves.
     * If there is not any repeated small cave,
     * it's able to visit a small cave twice (the repeated small cave)
     * @param cave
     */
    protected void addSmallCave(String cave) {
        if(isSmallCave(cave) && !isBoundCave(cave)) {

            if(small_caves.contains(cave) && repeated_small_cave.equals("")) {
                repeated_small_cave = cave;
            }

            small_caves.add(cave);
        }
    }

    public int size() {
        return path.size();
    }

    public String getLastCave() {
        return path.get(path.size()-1);
    }

    public boolean canVisitCave(String cave, Part part) {

        if(part == Part.first) {
            return !(cave.equals(cave.toLowerCase()) && path.contains(cave));
        } else /* if(part == Part.second) */ {

            if(isBoundCave(cave)) {
                return !path.contains(cave);
            } else {
                // in case of be a big cave, or else be a new small cave, or else, there is no repeated small caves visited yet
                return !isSmallCave(cave) || !path.contains(cave) || repeated_small_cave.equals("");
            }
        }
    }

}

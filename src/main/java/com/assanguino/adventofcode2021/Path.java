package com.assanguino.adventofcode2021;

import java.util.List;

import java.util.ArrayList;

public class Path {

    public static final String START_CAVE = "start";
    public static final String END_CAVE = "end";
   
    protected List<String> pathList = new ArrayList<>();
    protected List<String> smallCaves = new ArrayList<>();
    // store the name if the small cave that has benn visited twice
    protected String repeatedSmallCave = "";

    public Path() { }

    public Path(String cave) {
        pathList.add(cave);
    }

    // copy constructor
    public Path(Path path) {
        for(String node : path.getPathList()) {
            this.pathList.add(node);
            addSmallCave(node);
        }
    }

    public List<String> getPathList() {
        return pathList;
    }

    public void add(String cave) {
        pathList.add(cave);
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

            if(smallCaves.contains(cave) && repeatedSmallCave.equals("")) {
                repeatedSmallCave = cave;
            }

            smallCaves.add(cave);
        }
    }

    public int size() {
        return pathList.size();
    }

    public String getLastCave() {
        return pathList.get(pathList.size()-1);
    }

    public boolean canVisitCave(String cave, Part part) {

        if(part == Part.first) {
            return !(cave.equals(cave.toLowerCase()) && pathList.contains(cave));
        } else {

            if(isBoundCave(cave)) {
                return !pathList.contains(cave);
            } else {
                // in case of be a big cave, or else be a new small cave, or else, there is no repeated small caves visited yet
                return !isSmallCave(cave) || !pathList.contains(cave) || repeatedSmallCave.equals("");
            }
        }
    }

}

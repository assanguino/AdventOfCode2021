package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Level;

public class CavesPath implements Executable {
    
    protected Part part;
    protected boolean verbose;

    protected Map<String, ArrayList<String>> cavesMap = new HashMap<>();
    protected List<Path> paths = new ArrayList<>();

    public CavesPath(Part part) {
        this.part = part;
        this.verbose = false;
    }

    public void processRow(String string) {

        String[] splitted = string.split("-");
        String cave1 = splitted[0];
        String cave2 = splitted[1];

        // connections from 'A' to 'B'
        if(!cavesMap.containsKey(cave1)) {
            var list = new ArrayList<String>();
            list.add(cave2);
            cavesMap.put(cave1, list);
        } else {
            ArrayList<String> list = cavesMap.get(cave1);
            if(!list.contains(cave2))
                list.add(cave2);
        }

        // connections from 'B' to 'A'
        if(!cavesMap.containsKey(cave2)) {
            var list = new ArrayList<String>();
            list.add(cave1);
            cavesMap.put(cave2, list);
        } else {
            ArrayList<String> list = cavesMap.get(cave2);
            if(!list.contains(cave1))
                list.add(cave1);
        }

    }

    public void execute() {

        paths.clear();
        paths.add(new Path(Path.START_CAVE));

        printCavesMap();

        int pathsSize = 1;
        var pathsToProcess = paths.stream().filter(getPathsOfSize(pathsSize)).collect(Collectors.toList());

        do
        {
            logger.printf(Level.INFO, String.format("Processing paths of size [%2d]; a total of [%2d] different paths to process.", 
                pathsSize, pathsToProcess.size()));

            for(Path path : pathsToProcess) {
                List<Path> newPaths = new ArrayList<>();
                for (String node : cavesMap.get(path.getLastCave())) {
                    if(path.canVisitCave(node, part)) {
                        var newPath = new Path(path);
                        newPath.add(node);
                        newPaths.add(newPath);

                        String strNewPath = String.join(",", newPath.getPathList());
                        logger.debug("   path: %s", strNewPath );
                    }
                }
                paths.addAll(newPaths);
            }

            pathsToProcess = paths.stream().filter(getPathsOfSize(++pathsSize)).collect(Collectors.toList());
        }
        while(!pathsToProcess.isEmpty());

        // prune invalid paths
        paths.removeIf(p -> !p.getLastCave().equals(Path.END_CAVE));
    }

    public String printDescription() {
        return (part == Part.FIRST) ? 
            "Passage Pathing - How many paths ?" : 
            "Passage Pathing - How many paths visiting small caves twice ?";
    }

    public void printResult() {
        logger.info("");
        if(verbose) {
            logger.info("Final paths: ");
            paths.forEach(p -> logger.info("%s", String.join(",", p.getPathList())));
        }
        logger.info("Final paths are (%d):", paths.size());
        logger.info("");
    }

    public String getResult() {
        return String.valueOf(paths.size());
    }

    protected Predicate<Path> getPathsOfSize(int e) {
        return p -> p.size() == e;
    }

    protected void printCavesMap() {
        logger.info("");
        logger.info("Caves map:");
        cavesMap.entrySet().forEach(entry -> logger.info("%s -> %s", entry.getKey(), String.join(",", entry.getValue()) ));
        logger.info("");
    }

}


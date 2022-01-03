package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CavesPath implements Executable {
    
    protected Part part;
    protected boolean verbose;

    protected Map<String, ArrayList<String>> cavesMap = new HashMap<String, ArrayList<String>>();
    protected List<Path> paths = new ArrayList<Path>();       

    public CavesPath(Part part) {
        // TODO change when log4j
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

        int paths_size = 1;
        var paths_to_process = paths.stream().filter(getPathsOfSize(paths_size)).collect(Collectors.toList());

        do
        {
            /*
            System.out.println(String.format("Processing paths of size [%2d]; a total of [%2d] different paths to process.", 
                paths_size, paths_to_process.size()));
            */

            for(Path path : paths_to_process) {
                List<Path> newPaths = new ArrayList<>();
                for (String node : cavesMap.get(path.getLastCave())) {
                    if(path.canVisitCave(node, part)) {
                        var newPath = new Path(path);
                        newPath.add(node);
                        newPaths.add(newPath);

                        // System.out.println("   path: " + String.join(",", newPath.getPath() ));
                    }
                }
                paths.addAll(newPaths);
            }

            paths_to_process = paths.stream().filter(getPathsOfSize(++paths_size)).collect(Collectors.toList());
        }
        while(paths_to_process.size() > 0);

        // prune invalid paths
        paths.removeIf(p -> !p.getLastCave().equals(Path.END_CAVE));
    }

    public String printDescription() {
        return (part == Part.first) ? 
            "Passage Pathing - How many paths ?" : 
            "Passage Pathing - How many paths visiting small caves twice ?";
    }

    public void printResult() {
        System.out.println();
        if(verbose) {
            System.out.println("Final paths: ");
            paths.forEach(p -> System.out.println(String.join(",", p.getPath())));
        }
        System.out.println("Final paths are (" + paths.size() + "):");
        System.out.println();
    }

    public String getResult() {
        return String.valueOf(paths.size());
    }

    protected Predicate<Path> getPathsOfSize(int e) {
        return p -> p.size() == e;
    }

    protected void printCavesMap() {
        System.out.println();
        System.out.println("Caves map:");
        cavesMap.entrySet().forEach(entry -> System.out.println(entry.getKey() + " -> " + String.join(",", entry.getValue()) ));
        System.out.println();
    }

}


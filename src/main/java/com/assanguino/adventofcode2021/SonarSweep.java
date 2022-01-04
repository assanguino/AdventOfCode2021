package com.assanguino.adventofcode2021;

import java.util.ArrayList;

public class SonarSweep implements Executable {
    
    protected Part part;

    protected Integer current, last = -1;
    protected Integer noRows = 0;
    protected Integer increments = 0;
    protected ArrayList<Integer> measures = new ArrayList<Integer>();

    public SonarSweep(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        if(part == Part.first) {
            if (noRows == 0) {
                last = Integer.parseInt(row);
                noRows++;
            } else {
                current = Integer.parseInt(row);
                noRows++;
    
                if (current > last)
                    increments++;
    
                last = current;
            }
        } else {
            int current = Integer.parseInt(row);

            // three-measurement sliding window
            measures.add(noRows, current);
            if (noRows - 1 >= 0)
                measures.set(noRows - 1, measures.get(noRows - 1) + current);
            if (noRows - 2 >= 0)
                measures.set(noRows - 2, measures.get(noRows - 2) + current);
    
            noRows++;
        }

    }

    public void execute() {
        // Do nothing, all done when row processing!
        if (part == Part.second) {
            // because the last two measurements can't fill a three-measurements
            // sliding-window
            increments = 0;
            for (int i = 1; i < noRows - 2; i++) {
                if (measures.get(i) > measures.get(i - 1))
                    increments++;
            }
        }
    }

    public String printDescription() {
        return (part == Part.first) ? 
            "Sonar Sweep - How many measurements are larger than the previous measurement ?" : 
            "Sonar Sweep - How many sums are larger than the previous sum ?";
    }

    public void printResult() {
        System.out.println("measurements: " + noRows);
        System.out.println("increments: " + increments);
    }

    public String getResult() {
        return String.valueOf(increments);
    }

}

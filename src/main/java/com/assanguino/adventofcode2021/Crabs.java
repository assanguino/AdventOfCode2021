package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;

public class Crabs implements Executable {
    
    protected Part part;
    protected List<Integer> crabsInput = new ArrayList<>();
    protected int minValue = -1;
    protected long minDistance = -1;

    public Crabs(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        for(String c : row.split(",")) {
            crabsInput.add(Integer.parseInt(c));
        }
    }

    public void execute() {
        Collections.sort(crabsInput);

        // begin in the middle...
        int middleIndex = ((crabsInput.size() - 1) / 2);
        minValue = crabsInput.get(middleIndex);
        minDistance = calculateDistance(crabsInput, minValue);

        logger.printf(Level.INFO, "Iterating MIDDLE position %d, fuel cost %d", minValue, minDistance);

        // going up
        int upperValue = crabsInput.get(middleIndex);
        do {
            upperValue++;
            long upperDistance = calculateDistance(crabsInput, upperValue);

            logger.printf(Level.DEBUG, "Iterating UP position %d, fuel cost %d", upperValue, upperDistance);

            if(upperDistance <= minDistance) {
                minValue = upperValue;
                minDistance = upperDistance;
            } else {
                break;
            }

        } while (upperValue < crabsInput.get(crabsInput.size() - 1));

        // going down
        int lowerValue = crabsInput.get(middleIndex);
        do {
            lowerValue--;
            long lowerDistance = calculateDistance(crabsInput, lowerValue);

            logger.printf(Level.DEBUG, "Iterating DOWN position %d, fuel cost %d", lowerValue, lowerDistance);

            if(lowerDistance <= minDistance) {
                minValue = lowerValue;
                minDistance = lowerDistance;
            } else {
                break;
            }

        } while (lowerValue > crabsInput.get(0));
    }

    public String printDescription() {
        return (part == Part.FIRST) ? 
            "The Treachery of Whales - Position with less fuel consumption ?" : 
            "The Treachery of Whales - Total of fuel consumption ?";
    }

    public void printResult() {
        logger.info("number of crabs: %d", crabsInput.size());
        logger.info("position with less fuel consumption: %d", minValue);
        logger.info("total of fuel consumption: %d", minDistance);
    }

    public String getResult() {
        return String.valueOf(minDistance);
    }

    protected int calculateDistance(List<Integer> list, int value) {
        int totalDistance = 0;

        for(int i = 0; i < list.size(); i++) {

            int distance = Math.abs(list.get(i) - value);
            if(part == Part.SECOND) {
                int progressiveDistance = 0;
                for(int x = distance; x > 0; x--) {
                    progressiveDistance += x;
                }
                distance = progressiveDistance;
            }
            totalDistance += distance;
        }

        return totalDistance;
    }

}

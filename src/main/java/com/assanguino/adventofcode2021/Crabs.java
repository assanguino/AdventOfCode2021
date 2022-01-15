package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;

public class Crabs implements Executable {
    
    protected Part part;
    protected List<Integer> crabs_input = new ArrayList<>();
    protected int min_value = -1;
    protected long min_distance = -1;

    public Crabs(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        for(String c : row.split(",")) {
            crabs_input.add(Integer.parseInt(c));
        }
    }

    public void execute() {
        Collections.sort(crabs_input);

        // begin in the middle...
        int middle_index = (int)((crabs_input.size() - 1) / 2);
        min_value = crabs_input.get(middle_index);
        min_distance = calculate_distance(crabs_input, min_value);

        logger.printf(Level.INFO, "Iterating MIDDLE position %d, fuel cost %d", min_value, min_distance);

        // going up
        int upper_value = crabs_input.get(middle_index);
        do {
            upper_value++;
            long upper_distance = calculate_distance(crabs_input, upper_value);

            logger.printf(Level.DEBUG, "Iterating UP position %d, fuel cost %d", upper_value, upper_distance);

            if(upper_distance <= min_distance) {
                min_value = upper_value;
                min_distance = upper_distance;
            } else {
                break;
            }

        } while (upper_value < crabs_input.get(crabs_input.size() - 1));

        // going down
        int lower_value = crabs_input.get(middle_index);
        do {
            lower_value--;
            long lower_distance = calculate_distance(crabs_input, lower_value);

            logger.printf(Level.DEBUG, "Iterating DOWN position %d, fuel cost %d", lower_value, lower_distance);

            if(lower_distance <= min_distance) {
                min_value = lower_value;
                min_distance = lower_distance;
            } else {
                break;
            }

        } while (lower_value > crabs_input.get(0));
    }

    public String printDescription() {
        return (part == Part.first) ? 
            "The Treachery of Whales - Position with less fuel consumption ?" : 
            "The Treachery of Whales - Total of fuel consumption ?";
    }

    public void printResult() {
        System.out.println("number of crabs: " + crabs_input.size());
        System.out.println("position with less fuel consumption: " + min_value);
        System.out.println("total of fuel consumption: " + min_distance);
    }

    public String getResult() {
        return String.valueOf(min_distance);
    }

    protected int calculate_distance(List<Integer> list, int value) {
        int total_distance = 0;

        for(int i = 0; i < list.size(); i++) {

            int distance = Math.abs(list.get(i) - value);
            if(part == Part.second) {
                int progressive_distance = 0;
                for(int x = distance; x > 0; x--) {
                    progressive_distance += x;
                }
                distance = progressive_distance;
            }
            total_distance += distance;
        }

        return total_distance;
    }

}

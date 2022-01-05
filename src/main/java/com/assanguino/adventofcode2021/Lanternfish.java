package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;

public class Lanternfish implements Executable {
    
    protected final int DAYS_PROCESSED_FIRST = 80;
    protected final int DAYS_PROCESSED_SECOND = 256;

    protected Part part;
    protected int initial_lanternfishes = 0;
    protected long totalFishes = 0;

    /**
     * There is a way to codify this.
     * The index of the lanternfish_day list is the day;
     * the amount (the value) of lanternfish_day.get(day) is the number
     * of fishes
     */
    protected List<Long> lanternfish_day = new ArrayList<>();

    public Lanternfish(Part part) {
        this.part = part;
        lanternfish_day.addAll(Collections.nCopies(9, (long)0));
    }

    public void processRow(String row) {
        for(String c : row.split(",")) {
            int value = Integer.parseInt(c);
            lanternfish_day.set(value, lanternfish_day.get(value)+1);
            initial_lanternfishes++;
        }
    }

    public void execute() {

        for(int day = 1; day <= getNumberOfDays(); day++) {
            // lanternfish_day
            long newFishes = lanternfish_day.get(0);
            lanternfish_day.remove(0);
            lanternfish_day.set(6, lanternfish_day.get(6) + newFishes);
            lanternfish_day.add(newFishes);

            long totalFishes = 0;
            for(int i = 0; i < lanternfish_day.size(); i++) {
                totalFishes += lanternfish_day.get(i);
            }

            logger.printf(Level.INFO, "      day #%3d, number of lanternfishes: %d", day, totalFishes);
        }

        totalFishes = 0;
        for(int i = 0; i < lanternfish_day.size(); i++) {
            totalFishes += lanternfish_day.get(i);
        }
    }

    public String printDescription() {
        return "Lanternfish - How many lanternfish would there be after " + getNumberOfDays() + " days ?";
    }

    public void printResult() {
        System.out.println();
        System.out.println("number of initial lanternfishes: " + initial_lanternfishes);
        System.out.println("number of days: " + getNumberOfDays());
        System.out.println("number of final lanternfishes: " + totalFishes);
    }

    public String getResult() {
        return String.valueOf(totalFishes);
    }

    protected int getNumberOfDays() {
        return part == Part.first ? DAYS_PROCESSED_FIRST : DAYS_PROCESSED_SECOND;
    }

}

package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.Level;

public class Lanternfish implements Executable {
    
    protected static final int DAYS_PROCESSED_FIRST = 80;
    protected static final int DAYS_PROCESSED_SECOND = 256;

    protected Part part;
    protected int initialLanternfishes = 0;
    protected long totalFishes = 0;

    /**
     * There is a way to codify this.
     * The index of the lanternfish_day list is the day;
     * the amount (the value) of lanternfish_day.get(day) is the number
     * of fishes
     */
    protected List<Long> lanternfishDay = new ArrayList<>();

    public Lanternfish(Part part) {
        this.part = part;
        lanternfishDay.addAll(Collections.nCopies(9, (long)0));
    }

    public void processRow(String row) {
        for(String c : row.split(",")) {
            int value = Integer.parseInt(c);
            lanternfishDay.set(value, lanternfishDay.get(value)+1);
            initialLanternfishes++;
        }
    }

    public void execute() {

        for(int day = 1; day <= getNumberOfDays(); day++) {
            // lanternfish_day
            long newFishes = lanternfishDay.get(0);
            lanternfishDay.remove(0);
            lanternfishDay.set(6, lanternfishDay.get(6) + newFishes);
            lanternfishDay.add(newFishes);

            long lanternfishes = 0;
            for(int i = 0; i < lanternfishDay.size(); i++) {
                lanternfishes += lanternfishDay.get(i);
            }

            logger.printf(Level.INFO, "      day #%3d, number of lanternfishes: %d", day, lanternfishes);
        }

        totalFishes = 0;
        for(int i = 0; i < lanternfishDay.size(); i++) {
            totalFishes += lanternfishDay.get(i);
        }
    }

    public String printDescription() {
        return "Lanternfish - How many lanternfish would there be after " + getNumberOfDays() + " days ?";
    }

    public void printResult() {
        logger.info("");
        logger.info("number of initial lanternfishes: %d", initialLanternfishes);
        logger.info("number of days: %d", getNumberOfDays());
        logger.info("number of final lanternfishes: %d", totalFishes);
    }

    public String getResult() {
        return String.valueOf(totalFishes);
    }

    protected int getNumberOfDays() {
        return part == Part.FIRST ? DAYS_PROCESSED_FIRST : DAYS_PROCESSED_SECOND;
    }

}

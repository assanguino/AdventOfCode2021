package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;

public class Polymerization implements Executable {
    
    protected Part part;
    protected String polymerTemplate = "";
    protected Map<String, String> insertionRules = new HashMap<>();
    protected Map<String, ArrayList<String>> occurrencesGrowthRules = new HashMap<>();
    protected Map<String, Long> occurrencesMap = new HashMap<>();

    protected long occurrencesDisparity = -1;
    protected long mostCommon = -1;
    protected long lessCommon = -1;

    public Polymerization(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        // separation line; do nothing
        if(row.length() == 0)
            return;

        String[] chains = row.split(" -> ");
        if(chains.length == 1) {
            // It is the polymer template
            polymerTemplate = chains[0];
        } else {
            insertionRules.put(chains[0], chains[1]);
        }
    }

    public void execute() {

        printInsertionRules();

        generateOccurrencesGrowthRules();
        printOccurrencesGrowthRules();

        populateOccurrencesMap();
        printOccurrencesMap(0);

        // Iterations
        for(int i = 1; i < getSteps() + 1; i++) {
            iteratePolymer();
            printOccurrencesMap(i);
        }

        countOccurrences();
    }

    public String printDescription() {
        return "Extended Polymerization - What do you get if after " + getSteps() + " steps, substracts the most common element to the least common element ?";
    }

    public void printResult() {
        logger.info("Occurrence disparity is: %3d - %3d = %3d", mostCommon, lessCommon, occurrencesDisparity);
    }

    public String getResult() {
        return String.valueOf(occurrencesDisparity);
    }

    protected int getSteps() {
        return part == Part.FIRST ? 10 : 40;
    }

    protected void generateOccurrencesGrowthRules() {

        for(var entry : insertionRules.entrySet()) {

            String key = entry.getKey();
            String firstOutput = key.charAt(0) + entry.getValue();
            String secondOutput = entry.getValue() + key.charAt(1);

            occurrencesGrowthRules.put(key, new ArrayList<>(List.of(firstOutput, secondOutput)));
        }
    }

    protected void populateOccurrencesMap() {
        for(int i = 0; i < polymerTemplate.length() - 1; i++) {
            String sub = polymerTemplate.substring(i, i+2);
         
            if(occurrencesMap.containsKey(sub)) {
                occurrencesMap.put(sub, occurrencesMap.get(sub) + 1);
            } else {
                occurrencesMap.put(sub, (long)1);
            }
        }
    }

    protected void iteratePolymer() {

        // data structure where adding the new results (the next iteration)
        Map<String, Long> increasingOccurrencesMap = new HashMap<>();

        for(var entry : occurrencesMap.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            if(value == 0)
                continue;

            // first, reset the current occurrence
            entry.setValue((long)0);
            // then, increase the occurences for the next iteration
            for(var occ : occurrencesGrowthRules.get(key)) {
                if(increasingOccurrencesMap.containsKey(occ)) {
                    increasingOccurrencesMap.put(occ, increasingOccurrencesMap.get(occ) + value);
                } else {
                    increasingOccurrencesMap.put(occ, value);
                }                    
            }
        }

        // add the occurrences for the next iteration
        for(var entry : increasingOccurrencesMap.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            if(occurrencesMap.containsKey(key)) {
                occurrencesMap.put(key, occurrencesMap.get(key) + value);
            } else {
                occurrencesMap.put(key, value);
            }
        }

    }

    protected void countOccurrences() {

        HashMap<String, Long> letterMap = new HashMap<>();

        for(var entry : occurrencesMap.entrySet()) {
            
            String first = String.valueOf(entry.getKey().charAt(0));
            if(letterMap.containsKey(first)) {
                letterMap.put(first, letterMap.get(first) + entry.getValue());
            } else {
                letterMap.put(first, entry.getValue());
            }

            logger.debug("     occurrences of %s: %d", entry.getKey(), entry.getValue());
        }

        // Add at the end the last letter of the polymer:
        String last = String.valueOf(polymerTemplate.charAt(polymerTemplate.length() - 1));
        if(letterMap.containsKey(last)){
            letterMap.put(last, letterMap.get(last) + 1);
        } else {
            letterMap.put(last, (long)1);
        }

        mostCommon = -1;
        lessCommon = -1;
        for(var entry : letterMap.entrySet()) {
            if(mostCommon == -1 || mostCommon < entry.getValue())
                mostCommon = entry.getValue();
            if(lessCommon == -1 || lessCommon > entry.getValue())
                lessCommon = entry.getValue();
        }

        occurrencesDisparity = mostCommon - lessCommon;
    }

    protected void printInsertionRules() {
        logger.info("%n Insertion rules: ");
        for(var entry : insertionRules.entrySet()) {
            logger.info("     insertion rule: %s -> %s", entry.getKey(), entry.getValue());
        }
    }

    protected void printOccurrencesGrowthRules() {
        logger.info("%n Occurrences growth rules (A total of %d rules): ", occurrencesGrowthRules.size());
        for(var entry : occurrencesGrowthRules.entrySet()) {
            logger.printf(Level.INFO, "     key [%s] values [%s %s]", entry.getKey(), entry.getValue().get(0), entry.getValue().get(1));
        }
    }

    protected void printOccurrencesMap(int step) {
        String title = step == 0 ? "Template" : "After iterating " + step + " times:";
        logger.debug(title);
        for(var entry : occurrencesMap.entrySet()) {
            logger.debug("     occurrencesMap [%s] -> repeated [%5d] times.", entry.getKey(), entry.getValue());
        }
    }

}

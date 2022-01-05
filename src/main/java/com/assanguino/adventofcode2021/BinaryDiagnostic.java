package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.logging.log4j.Level;

public class BinaryDiagnostic implements Executable {

    protected Part part;
    protected int noRows = 0;
    protected int binaryLength = 0;

    // first part properties
    protected ArrayList<Integer> zeros = new ArrayList<>();
    protected ArrayList<Integer> ones = new ArrayList<>();

    protected String gamma_rate_binary = "", epsilon_rate_binary = "";
    protected int gamma_rate = -1;
    protected int epsilon_rate = -1;

    // second part properties
    protected ArrayList<String> oxygen_generator_list = new ArrayList<>();
    protected ArrayList<String> CO2_scrubber_list = new ArrayList<>();

    protected int oxygen_generator_rating = -1;
    protected int CO2_scrubber_rating = -1;

    public BinaryDiagnostic(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        // initialize arrays if this is first iteration
        if (binaryLength == 0) {
            binaryLength = row.length();

            if(part == Part.first) {
                zeros.addAll(Collections.nCopies(binaryLength, 0));
                ones.addAll(Collections.nCopies(binaryLength, 0));
            }
        }

        if(part == Part.first) {

            for (int i = 0; i < binaryLength; i++) {
                if (row.charAt(i) == '0') {
                    zeros.set(i, zeros.get(i) + 1);
                } else if (row.charAt(i) == '1') {
                    ones.set(i, ones.get(i) + 1);
                }
            }
        } else {

            oxygen_generator_list.add(row);
            CO2_scrubber_list.add(row);
        }

        noRows++;
    }

    public void execute() { 
        if(part == Part.first) {
            getGammaEpsilonRates();
        } else {
            getLifeSupportRating();
        }
    }

    public String printDescription() {
        return (part == Part.first) ? 
            "Binary Diagnostic - What is the power consumption of the submarine ?" : 
            "Binary Diagnostic - What is the life support rating of the submarine ?";
    }

    public void printResult() {
        System.out.println();
        System.out.println("measurements: " + noRows);

        if(part == Part.first) {
            System.out.println("gamma_rate_binary:   " + gamma_rate_binary +  " - gamma_rate: " + gamma_rate);
            System.out.println("epsilon_rate_binary: " + epsilon_rate_binary +  " - epsilon_rate: " + epsilon_rate);
            System.out.println("final value (multiplication): " + gamma_rate * epsilon_rate);
        } else {
            System.out.println("oxygen_generator_rating: " + oxygen_generator_rating);
            System.out.println("CO2_scrubber_rating:     " + CO2_scrubber_rating);
            System.out.println("life_support_rating (multiplication): " + oxygen_generator_rating * CO2_scrubber_rating);
        }    
    }

    public String getResult() {
        return part == Part.first ? 
            String.valueOf(gamma_rate * epsilon_rate) :
            String.valueOf(oxygen_generator_rating * CO2_scrubber_rating);
    }

    protected void getGammaEpsilonRates() {
        gamma_rate_binary = "";
        epsilon_rate_binary = "";
        
        for(int i = 0; i < binaryLength; i++) {
            if(zeros.get(i) > ones.get(i)) {
                gamma_rate_binary = gamma_rate_binary + "0";
                epsilon_rate_binary = epsilon_rate_binary + "1";
            } else {
                gamma_rate_binary = gamma_rate_binary + "1";
                epsilon_rate_binary = epsilon_rate_binary + "0";
            }
        }

        gamma_rate = Integer.parseInt(gamma_rate_binary, 2);
        epsilon_rate = Integer.parseInt(epsilon_rate_binary, 2);       
    }

    protected void getLifeSupportRating() {

        int index = 0;
        do {

            int noZeros = 0, noOnes = 0;
            for(String str : oxygen_generator_list) {
                if(str.charAt(index) == '0') {
                    noZeros++;
                } else {
                    noOnes++;
                }
            }

            final int finalIndex = index;
            if(noOnes >= noZeros) {
                oxygen_generator_list.removeIf(str -> str.charAt(finalIndex) == '0');
            } else {
                oxygen_generator_list.removeIf(str -> str.charAt(finalIndex) == '1');
            }

            // Iteration
            logger.printf(Level.INFO, "oxygen_generator_list - Iteration #" + index + " list size " + oxygen_generator_list.size() + " elements.");

            index++;

        } while(oxygen_generator_list.size() > 1 && index < binaryLength);

        // Middle report
        logger.printf(Level.INFO, "oxygen_generator_list size: " + oxygen_generator_list.size());
        logger.printf(Level.INFO, "oxygen_generator_list first value: " + (oxygen_generator_list.size() > 0 ? oxygen_generator_list.get(0) : "[No value]"));
        logger.printf(Level.INFO, "index reached: " + index + " / " + binaryLength + " bits.");

        index = 0;
        do {

            int noZeros = 0, noOnes = 0;
            for(String str : CO2_scrubber_list) {
                if(str.charAt(index) == '0') {
                    noZeros++;
                } else {
                    noOnes++;
                }
            }

            final int finalIndex = index;
            if(noOnes >= noZeros) {
                CO2_scrubber_list.removeIf(str -> str.charAt(finalIndex) == '1');
            } else {
                CO2_scrubber_list.removeIf(str -> str.charAt(finalIndex) == '0');
            }

            // Iteration
            logger.printf(Level.INFO, "CO2_scrubber_list - Iteration #" + index + " list size " + CO2_scrubber_list.size() + " elements.");

            index++;

        // } while(index < binaryLength);
        } while(CO2_scrubber_list.size() > 1 && index < binaryLength);

        // Middle report
        logger.printf(Level.INFO, "CO2_scrubber_list size: " + CO2_scrubber_list.size());
        logger.printf(Level.INFO, "CO2_scrubber_list first value: " + (CO2_scrubber_list.size() > 0 ? CO2_scrubber_list.get(0) : "[No value]"));
        logger.printf(Level.INFO, "index reached: " + index + " / " + binaryLength + " bits.");

        oxygen_generator_rating = Integer.parseInt(oxygen_generator_list.get(0), 2);
        CO2_scrubber_rating = Integer.parseInt(CO2_scrubber_list.get(0), 2);

    }
}

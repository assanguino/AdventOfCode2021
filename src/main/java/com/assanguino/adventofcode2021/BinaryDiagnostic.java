package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinaryDiagnostic implements Executable {

    protected Part part;
    protected int noRows = 0;
    protected int binaryLength = 0;

    // first part properties
    protected ArrayList<Integer> zeros = new ArrayList<>();
    protected ArrayList<Integer> ones = new ArrayList<>();

    protected StringBuilder gammaRateBinary = new StringBuilder();
    protected StringBuilder epsilonRateBinary = new StringBuilder();
    protected int gammaRate = -1;
    protected int epsilonRate = -1;

    // second part properties
    protected ArrayList<String> oxygenGeneratorList = new ArrayList<>();
    protected ArrayList<String> listCO2scrubber = new ArrayList<>();

    protected int oxygenGeneratorRating = -1;
    protected int ratingCO2scrubber = -1;

    public BinaryDiagnostic(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        // initialize arrays if this is first iteration
        if (binaryLength == 0) {
            binaryLength = row.length();

            if(part == Part.FIRST) {
                zeros.addAll(Collections.nCopies(binaryLength, 0));
                ones.addAll(Collections.nCopies(binaryLength, 0));
            }
        }

        if(part == Part.FIRST) {

            for (int i = 0; i < binaryLength; i++) {
                if (row.charAt(i) == '0') {
                    zeros.set(i, zeros.get(i) + 1);
                } else if (row.charAt(i) == '1') {
                    ones.set(i, ones.get(i) + 1);
                }
            }
        } else {

            oxygenGeneratorList.add(row);
            listCO2scrubber.add(row);
        }

        noRows++;
    }

    public void execute() { 
        if(part == Part.FIRST) {
            getGammaEpsilonRates();
        } else {
            getLifeSupportRating();
        }
    }

    public String printDescription() {
        return (part == Part.FIRST) ? 
            "Binary Diagnostic - What is the power consumption of the submarine ?" : 
            "Binary Diagnostic - What is the life support rating of the submarine ?";
    }

    public void printResult() {
        logger.info("");
        logger.info("measurements: %d", noRows);

        if(part == Part.FIRST) {
            logger.info("gamma_rate_binary:   %s - gamma_rate: %d", gammaRateBinary, gammaRate);
            logger.info("epsilon_rate_binary: %s  - epsilon_rate: %d", epsilonRateBinary, epsilonRate);
            logger.info("final value (multiplication): %d", gammaRate * epsilonRate);
        } else {
            logger.info("oxygen_generator_rating: %d", oxygenGeneratorRating);
            logger.info("CO2_scrubber_rating: %d", ratingCO2scrubber);
            logger.info("life_support_rating (multiplication): %d", oxygenGeneratorRating * ratingCO2scrubber);
        }    
    }

    public String getResult() {
        return part == Part.FIRST ? 
            String.valueOf(gammaRate * epsilonRate) :
            String.valueOf(oxygenGeneratorRating * ratingCO2scrubber);
    }

    protected void getGammaEpsilonRates() {
        for(int i = 0; i < binaryLength; i++) {
            if(zeros.get(i) > ones.get(i)) {
                gammaRateBinary.append('0');
                epsilonRateBinary.append('1');
            } else {
                gammaRateBinary.append('1');
                epsilonRateBinary.append('0');
            }
        }

        gammaRate = Integer.parseInt(gammaRateBinary.toString(), 2);
        epsilonRate = Integer.parseInt(epsilonRateBinary.toString(), 2);       
    }

    private boolean countMoreOnesThanZeros(int index, List<String> list) {

        int noZeros = 0;
        int noOnes = 0;
        for(String str : list) {
            if(str.charAt(index) == '0') {
                noZeros++;
            } else {
                noOnes++;
            }
        }

        return noOnes >= noZeros;
    }

    protected void getLifeSupportRating() {

        int index = 0;
        do {

            final int finalIndex = index;
            if(countMoreOnesThanZeros(index, oxygenGeneratorList)) {
                oxygenGeneratorList.removeIf(str -> str.charAt(finalIndex) == '0');
            } else {
                oxygenGeneratorList.removeIf(str -> str.charAt(finalIndex) == '1');
            }

            // Iteration
            logger.info("oxygen_generator_list - Iteration #%d list size %d elements.", index, oxygenGeneratorList.size());

            index++;

        } while(oxygenGeneratorList.size() > 1 && index < binaryLength);

        // Middle report
        String oxygenGeneratorValue = oxygenGeneratorList.isEmpty() ? "[No value]" : oxygenGeneratorList.get(0);
        logger.info("oxygen_generator_list size: %d", oxygenGeneratorList.size());
        logger.info("oxygen_generator_list first value: %s", oxygenGeneratorValue);
        logger.info("index reached: %d / %d bits.", index, binaryLength);

        index = 0;
        do {

            final int finalIndex = index;
            if(countMoreOnesThanZeros(index, listCO2scrubber)) {
                listCO2scrubber.removeIf(str -> str.charAt(finalIndex) == '1');
            } else {
                listCO2scrubber.removeIf(str -> str.charAt(finalIndex) == '0');
            }

            // Iteration
            logger.info("CO2_scrubber_list - Iteration #%d list size %d elements.", index, listCO2scrubber.size());

            index++;

        } while(listCO2scrubber.size() > 1 && index < binaryLength);

        // Middle report
        logger.info("CO2_scrubber_list size: %d", listCO2scrubber.size());
        logger.info("CO2_scrubber_list first value: %s", (listCO2scrubber.isEmpty() ? "[No value]" : listCO2scrubber.get(0)));
        logger.info("index reached: %d / %d bits.", index, binaryLength);

        oxygenGeneratorRating = Integer.parseInt(oxygenGeneratorList.get(0), 2);
        ratingCO2scrubber = Integer.parseInt(listCO2scrubber.get(0), 2);
    }
}

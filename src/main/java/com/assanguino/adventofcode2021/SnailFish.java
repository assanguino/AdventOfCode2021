package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.List;

public class SnailFish implements Executable {

    protected Part part;
    SnailFishFactory factory = new SnailFishFactory(1);

    List<String> numbers = new ArrayList<>();
    Long maximum = Long.MIN_VALUE;
    SnailFishNumber cumulative = null;
    Long magnitude;

    public SnailFish(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        // Comment or blank line, so skip
        if(row.length() == 0 || row.charAt(0) == '#')
            return;

        if(part == Part.FIRST) {
            var number = factory.getNewSnailFishNumber(row);
            number.check();
    
            if(cumulative == null) {
                cumulative = number;
            } else {
                cumulative.sum(number);
            }
        } else if(part == Part.SECOND) {
            numbers.add(row);
        }

    }

    public void execute() {
        if(part == Part.FIRST) {
            magnitude = cumulative.magnitude();
        } else if(part == Part.SECOND) {
            getMaximumSum();
        }
    }

    public String printDescription() {
        return part == Part.FIRST ? 
            "What is the magnitude of the final sum ?" :
            "What is the largest magnitude of any sum of two different snailfish numbers from the homework assignment ?";
    }

    public void printResult() {
        logger.info("");
        if(part == Part.FIRST) {
            logger.info("result of the cumulative sum: %s", cumulative.toString());
            logger.info("magnitude: %s", getResult());
        } else {
            logger.info("maximum: %s", getResult());
        }
    }

    public String getResult() {
        return part == Part.FIRST ? magnitude.toString() : maximum.toString();
    }

    protected void getMaximumSum() {
        int size = numbers.size();
        Long[][] magnitudes = new Long[size][size];

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {

                var a0 = factory.getNewSnailFishNumber(numbers.get(i));
                var b0 = factory.getNewSnailFishNumber(numbers.get(j));
                var a1 = factory.getNewSnailFishNumber(numbers.get(i));
                var b1 = factory.getNewSnailFishNumber(numbers.get(j));

                a0.sum(b0);
                magnitudes[i][j] = a0.magnitude();
                b1.sum(a1);
                magnitudes[j][i] = b1.magnitude();
            }
        }

        // get the maximum
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(magnitudes[i][j] > maximum) {
                    maximum = magnitudes[i][j];
                }
            }
        }
    }

}

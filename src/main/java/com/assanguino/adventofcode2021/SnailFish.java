package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.List;

public class SnailFish implements Executable {

    protected Part part;

    SnailFishNumber cumulative = null;
    Long magnitude;

    List<String> numbers = new ArrayList<>();
    Long maximum = Long.MIN_VALUE;
    
    public SnailFish(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        // Comment or blank line, so skip
        if(row.length() == 0 || row.charAt(0) == '#')
            return;

        if(part == Part.first) {
            var number = new LiteralSnailFishNumber(row);
            number.check();
    
            if(cumulative == null) {
                cumulative = number;
            } else {
                cumulative.sum(number);
            }
        } else if(part == Part.second) {
            numbers.add(row);
        }

    }

    public void execute() {
        if(part == Part.first) {
            magnitude = cumulative.magnitude();
        } else if(part == Part.second) {
            getMaximumSum();
        }
    }

    public String printDescription() {
        return part == Part.first ? 
            "What is the magnitude of the final sum ?" :
            "What is the largest magnitude of any sum of two different snailfish numbers from the homework assignment ?";
    }

    public void printResult() {
        System.out.println();
        if(part == Part.first) {
            System.out.println("result of the cumulative sum: " + cumulative.toString());
            System.out.println("magnitude: " + getResult());
        } else {
            System.out.println("maximum: " + getResult());
        }
    }

    public String getResult() {
        return part == Part.first ? magnitude.toString() : maximum.toString();
    }

    protected void getMaximumSum() {
        int size = numbers.size();
        Long[][] magnitudes = new Long[size][size];

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                var a0 = new LiteralSnailFishNumber(numbers.get(i));
                var b0 = new LiteralSnailFishNumber(numbers.get(j));
                var a1 = new LiteralSnailFishNumber(numbers.get(i));
                var b1 = new LiteralSnailFishNumber(numbers.get(j));

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

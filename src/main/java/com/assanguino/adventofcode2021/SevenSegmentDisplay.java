package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class SevenSegmentDisplay implements Executable {
    
    protected static final int INPUT_NUMBERS = 10;
    protected static final int OUTPUT_NUMBERS = 4;

    protected static final String BAD_DIGIT = "BAD_DIGIT";

    protected Part part;
    protected List<String> decodedDigits = new ArrayList<>();
    protected int noRows = 0;
    protected long counterFirst = 0;
    protected long outputSum = 0;

    public SevenSegmentDisplay(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        noRows++;
        String[] strings = row.split(" ");

        if(part == Part.FIRST) {

            for(int i = strings.length - 1; i > strings.length - 5; i--) {
                int len = strings[i].length();
                if(len == 2 || len == 3 || len == 4 || len == 7) {
                    counterFirst++;
                }
            }
        } else {

            List<String> input = new ArrayList<>();
    
            input.addAll(Arrays.asList(Arrays.copyOf(strings, INPUT_NUMBERS)));

            // decoded digits
            decodeDigits(input);
    
            String strNumber = buildNumber(strings);

            outputSum += Integer.parseInt(strNumber);
    
            logger.info("Decoding each number. Measure #%d. Number: %s. Sum: %ld", noRows, strNumber, outputSum);
        }

    }

    public void execute() {
        // No thing to do here; all is done in the processRow() method.
    }

    public String printDescription() {
        return (part == Part.FIRST) ? 
            "Seven Segment Search - How many times do digits 1, 4, 7, or 8 appear ?" : 
            "Seven Segment Search - What do you get if you add up all of the output values ?";
    }

    public void printResult() {
        logger.info("number of measurements: %ld", noRows);
        if(part == Part.FIRST) {
            logger.info("number of 1, 4, 7 and 8 outputs: %ld", counterFirst);
        } else {
            logger.info("sum of all outputs: %ld", outputSum);
        }
    }

    public String getResult() {
        return part == Part.FIRST ? 
            String.valueOf(counterFirst) :
            String.valueOf(outputSum);
    }
    
    protected void decodeDigits(List<String> input) {
        // init the decodification result
        decodedDigits.clear();
        decodedDigits.addAll(Collections.nCopies(10, ""));

        // get the "1" -> length 2
        String one = input.stream().filter(n -> n.length() == 2).findFirst().orElse(BAD_DIGIT);
        decodedDigits.set(1, sortCharacters(one));
        input.removeIf(n -> n.length() == 2);

        // get the "7" -> length 3
        String seven = input.stream().filter(n -> n.length() == 3).findFirst().orElse(BAD_DIGIT);
        decodedDigits.set(7, sortCharacters(seven));
        input.removeIf(n -> n.length() == 3);

        // get the "4" -> length 4
        String four = input.stream().filter(n -> n.length() == 4).findFirst().orElse(BAD_DIGIT);
        decodedDigits.set(4, sortCharacters(four));
        input.removeIf(n -> n.length() == 4);

        // get the "8" -> length 7
        String eigth = input.stream().filter(n -> n.length() == 7).findFirst().orElse(BAD_DIGIT);
        decodedDigits.set(8, sortCharacters(eigth));
        input.removeIf(n -> n.length() == 7);

        // get the "0", "6", and "9" -> length 6 (left 0, 9)

        // Number "3" is like seven with more elements (and has 5 length)
        Predicate<String> threeCondition = (n -> n.length() == 5 && containsAllCharacters(decodedDigits.get(7), n));
        String three = input.stream().filter(threeCondition).findFirst().orElse(BAD_DIGIT);
        decodedDigits.set(3, sortCharacters(three));
        input.removeIf(threeCondition);

        // Number "6" has length 6, and has not all the "1" elements
        Predicate<String> sixCondition = (n -> n.length() == 6 && !containsAllCharacters(decodedDigits.get(1), n));
        String six = input.stream().filter(sixCondition).findFirst().orElse(BAD_DIGIT);
        decodedDigits.set(6, sortCharacters(six));
        input.removeIf(sixCondition);

        // Number "5" has length 5, and has all the elements of number "6"
        Predicate<String> fiveCondition = (n -> n.length() == 5 && containsAllCharacters(n, decodedDigits.get(6)));
        String five = input.stream().filter(fiveCondition).findFirst().orElse(BAD_DIGIT);
        decodedDigits.set(5, sortCharacters(five));
        input.removeIf(fiveCondition);

        // Now, number "2" is the only one with length 5
        Predicate<String> twoCondition = (n -> n.length() == 5);
        String two = input.stream().filter(twoCondition).findFirst().orElse(BAD_DIGIT);
        decodedDigits.set(2, sortCharacters(two));
        input.removeIf(twoCondition);

        // Now just left "0" and "9". It's needed to compare with number "3"
        // ("9" has all the elements of "3", but "0" don't)
        Predicate<String> nineCondition = (n -> containsAllCharacters(decodedDigits.get(3), n));
        String nine = input.stream().filter(nineCondition).findFirst().orElse(BAD_DIGIT);
        decodedDigits.set(9, sortCharacters(nine));
        input.removeIf(nineCondition);

        // "0"
        String zero = input.stream().findFirst().orElse(BAD_DIGIT);
        decodedDigits.set(0, sortCharacters(zero));
        input.removeIf(n -> n.length() == 6);
    }

    protected String sortCharacters(String in) {

        char[] chars = in.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    protected boolean containsAllCharacters(String lessCharacters, String moreCharacters) {
        for(int i = 0; i < lessCharacters.length(); i++) {
            if(!moreCharacters.contains(lessCharacters.substring(i, i+1))) {
                return false;
            }
        }

        return true;
    }

    protected String buildNumber(String[] strings) {
        StringBuilder builtNumber = new StringBuilder("");

        for(int i = 0; i < OUTPUT_NUMBERS; i++) {
            String sorted = sortCharacters(strings[strings.length - 1 - i]);

            for(int j = 0; j < decodedDigits.size(); j++) {
                if(decodedDigits.get(j).equals(sorted)) {
                    builtNumber.insert(0, j);
                }
            }
        }

        return builtNumber.toString();
    }

}

package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class SevenSegmentDisplay {
    
    protected static final int INPUT_NUMBERS = 10;
    protected static final int OUTPUT_NUMBERS = 4;

    protected static List<String> decodedDigits = new ArrayList<String>();

    public static int decodeOutput(String[] strings) {

        List<String> input = new ArrayList<>();

        for(int i = 0; i < INPUT_NUMBERS; i++) {
            input.add(strings[i]);
        }

        // decoded digits
        decodeDigits(input);

        String number_str = "";
        for(int i = 0; i < OUTPUT_NUMBERS; i++) {
            String sorted = sortCharacters(strings[strings.length - 1 - i]);

            for(int j = 0; j < decodedDigits.size(); j++) {
                if(decodedDigits.get(j).equals(sorted)) {
                    number_str = String.format("%d%s", j, number_str);
                }
            }
        }

        return Integer.parseInt(number_str);
    }

    protected static void decodeDigits(List<String> input) {
        // init the decodification result
        decodedDigits.clear();
        decodedDigits.addAll(Collections.nCopies(10, ""));

        // get the "1" -> length 2
        String one = input.stream().filter(n -> n.length() == 2).findFirst().get();
        decodedDigits.set(1, sortCharacters(one));
        input.removeIf(n -> n.length() == 2);

        // get the "7" -> length 3
        String seven = input.stream().filter(n -> n.length() == 3).findFirst().get();
        decodedDigits.set(7, sortCharacters(seven));
        input.removeIf(n -> n.length() == 3);

        // get the "4" -> length 4
        String four = input.stream().filter(n -> n.length() == 4).findFirst().get();
        decodedDigits.set(4, sortCharacters(four));
        input.removeIf(n -> n.length() == 4);

        // get the "8" -> length 7
        String eigth = input.stream().filter(n -> n.length() == 7).findFirst().get();
        decodedDigits.set(8, sortCharacters(eigth));
        input.removeIf(n -> n.length() == 7);

        // get the "0", "6", and "9" -> length 6 (left 0, 9)

        // Number "3" is like seven with more elements (and has 5 length)
        Predicate<String> threeCondition = (n -> n.length() == 5 && containsAllCharacters(decodedDigits.get(7), n));
        String three = input.stream().filter(threeCondition).findFirst().get();
        decodedDigits.set(3, sortCharacters(three));
        input.removeIf(threeCondition);

        // Number "6" has length 6, and has not all the "1" elements
        Predicate<String> sixCondition = (n -> n.length() == 6 && !containsAllCharacters(decodedDigits.get(1), n));
        String six = input.stream().filter(sixCondition).findFirst().get();
        decodedDigits.set(6, sortCharacters(six));
        input.removeIf(sixCondition);

        // Number "5" has length 5, and has all the elements of number "6"
        Predicate<String> fiveCondition = (n -> n.length() == 5 && containsAllCharacters(n, decodedDigits.get(6)));
        String five = input.stream().filter(fiveCondition).findFirst().get();
        decodedDigits.set(5, sortCharacters(five));
        input.removeIf(fiveCondition);

        // Now, number "2" is the only one with length 5
        Predicate<String> twoCondition = (n -> n.length() == 5);
        String two = input.stream().filter(twoCondition).findFirst().get();
        decodedDigits.set(2, sortCharacters(two));
        input.removeIf(twoCondition);

        // Now just left "0" and "9". It's needed to compare with number "3"
        // ("9" has all the elements of "3", but "0" don't)
        Predicate<String> nineCondition = (n -> containsAllCharacters(decodedDigits.get(3), n));
        String nine = input.stream().filter(nineCondition).findFirst().get();
        decodedDigits.set(9, sortCharacters(nine));
        input.removeIf(nineCondition);

        // "0"
        String zero = input.stream().findFirst().get();
        decodedDigits.set(0, sortCharacters(zero));
        input.removeIf(n -> n.length() == 6);
    }

    protected static String sortCharacters(String in) {

        char[] chars = in.toCharArray();
        Arrays.sort(chars);
        String out = new String(chars);

        return out;
    }

    protected static boolean containsAllCharacters(String lessCharacters, String moreCharacters) {
        for(int i = 0; i < lessCharacters.length(); i++) {
            if(!moreCharacters.contains(lessCharacters.substring(i, i+1))) {
                return false;
            }
        }

        return true;
    }

}

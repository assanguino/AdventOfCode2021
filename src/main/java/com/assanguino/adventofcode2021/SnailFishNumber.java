package com.assanguino.adventofcode2021;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

interface SnailFishNumber {
    public static final Logger logger = LogManager.getLogger(SnailFishNumber.class.getName());

    final char CH_OPEN = '[';
    final char CH_CLOSE = ']';
    final char CH_SEPARATOR = ',';
    final String STR_EXPLODED = "0";
    final int NESTED_LEVEL = 4;
    final int SPLIT_LIMIT = 10;
    final int LEFT_FACTOR = 3;
    final int RIGHT_FACTOR = 2;

    boolean check();
    String toString();
    void add(SnailFishNumber a);
    void explode(Object obj);
    boolean split();
    void reduce();
    void sum(SnailFishNumber a);
    Long magnitude();
}

class LiteralSnailFishNumber implements SnailFishNumber {

    protected String number;

    LiteralSnailFishNumber(String number) {
        this.number = number;
    }

    public boolean check() {

        String collapsed = new String(number);

        do {
            // checking open & close charactrs
            int close = collapsed.indexOf(CH_CLOSE);
            if(close == -1) {
                logger.printf(Level.ERROR, "Checking number [%s] - bad composition of [%s]", number, collapsed);
                return false;
            }
            int open = collapsed.substring(0, close).lastIndexOf(CH_OPEN);
            if(open == -1) {
                logger.printf(Level.ERROR, "Checking number [%s] - bad composition of [%s]", number, collapsed);
                return false;
            }

            // check that has a separator
            if(collapsed.substring(open, close+1).indexOf(CH_SEPARATOR) == -1) {
                logger.printf(Level.ERROR, "Checking number [%s] - no separator in [%s]", number, collapsed);
                return false;
            }

            collapsed = collapse(collapsed, open, close, STR_EXPLODED);

        } while (collapsed.length() > STR_EXPLODED.length());

        logger.printf(Level.DEBUG, "Checking number [%s] - OK", number);

        return true;
    }

    @Override
    public String toString() {
        return number;
    }

    public void add(SnailFishNumber a) {
        number = format(number.toString(), a.toString());
    }

    public void explode(Object obj) {

        // The obj reference is an Integer, pointing out the index of the regular snailfish number
        Integer indexBegin = (Integer)obj;

        if(indexBegin == -1) {
            logger.printf(Level.ERROR, "Trying to explode a snailfish number that doesn't exist.");
            return;
        }

        if(number.indexOf(number, indexBegin) != -1) {
            logger.printf(Level.ERROR, "Trying to explode a repeated snailfish number.");
            return;
        }

        // ensure the indexExploded is a 'CH_OPEN' (going to the left)
        while(number.charAt(indexBegin) != CH_OPEN) {
            indexBegin--;
        }

        // get the indexes for the snailfish number to explode
        int indexSeparator = number.indexOf(CH_SEPARATOR, indexBegin);
        int indexEnd = number.indexOf(CH_CLOSE, indexSeparator);

        // get the numbers within, and replace the number by the exploded one
        Integer left  = Integer.parseInt(number.substring(indexBegin+1, indexSeparator));
        Integer right = Integer.parseInt(number.substring(indexSeparator+1, indexEnd));

        number = collapse(number, indexBegin, indexEnd, STR_EXPLODED);

        // replace the number on the right
        Integer rightIndex = -1;
        String strRightChar = "";
        for(int i = indexBegin+1; i < number.length(); i++) {
            char c = number.charAt(i);
            if(isRegularNumber(c)) {
                if(rightIndex == -1)
                    rightIndex = i;

                strRightChar = strRightChar + c;
            } else if(!strRightChar.isEmpty()) {
                break;
            }
        }

        if(rightIndex != -1) {
            right += Integer.parseInt(strRightChar);
            number = number.substring(0, rightIndex) + right.toString() + number.substring(rightIndex + strRightChar.length());
        }

        // replace the number on the left
        Integer leftIndex = -1;
        String strLeftChar = "";
        for(int i = indexBegin-1; i >= 0; i--) {
            char c = number.charAt(i);
            if(isRegularNumber(c)) {
                leftIndex = i;
                strLeftChar = c + strLeftChar;
            } else if(!strLeftChar.isEmpty()) {
                break;
            }
        }

        if(leftIndex != -1) {
            left += Integer.parseInt(strLeftChar);
            number = number.substring(0, leftIndex) + left.toString() + number.substring(leftIndex + strLeftChar.length());
        }
    }

    public boolean split() {
        int index = -1;
        String str = "";
        for(int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if(isRegularNumber(c)) {
                if(index == -1) index = i;
                str += c;
            } else if(!str.equals("")) {
                int n = Integer.parseInt(str);
                if(n < SPLIT_LIMIT) {
                    index = -1;
                    str = "";
                } else {
                    Integer left = n / 2;
                    Integer right = left + n % 2;
                    number = number.substring(0, index) + format(left.toString(), right.toString()) + number.substring(index + str.length());
                    return true;
                }
            }
        }

        return false;
    }

    public void reduce() {
        Integer index = -1;
        String previous_number = "";

        logger.printf(Level.DEBUG, "after sum    : %s", number);

        do {
            previous_number = new String(number);

            while((index = getNestedPair()) != -1) {
                explode(index);
                logger.printf(Level.DEBUG, "after explode: %s (index %d)", number, index);
            }
    
            split();
            logger.printf(Level.DEBUG, "after split  : %s", number);

        } while(!number.equals(previous_number));
    }

    /**
     * Sum involves the actions of addition and reduction of the number
     * @param a the snailfish number to be added to this object number
     */
    public void sum(SnailFishNumber a) {
        add(a);
        reduce();
    }

    public Long magnitude() {
        String str_magnitude = new String(number);

        do {
            int indexBegin = 0;
            do {
                if(isSnailFishRegularNumber(str_magnitude, indexBegin)) {
                    // get the indexes for the snailfish number to explode
                    int indexSeparator = str_magnitude.indexOf(CH_SEPARATOR, indexBegin);
                    int indexEnd = str_magnitude.indexOf(CH_CLOSE, indexSeparator);
    
                    // get the numbers within, and replace the number by the exploded one
                    Integer left  = Integer.parseInt(str_magnitude.substring(indexBegin+1, indexSeparator));
                    Integer right = Integer.parseInt(str_magnitude.substring(indexSeparator+1, indexEnd));
    
                    Long current_magnitude = (long)(LEFT_FACTOR*left + RIGHT_FACTOR*right);

                    str_magnitude = collapse(str_magnitude, indexBegin, indexEnd, current_magnitude.toString());
                    break;
                }
                
            } while(indexBegin++ < str_magnitude.length());
        } while(str_magnitude.indexOf(CH_OPEN) != -1);

        return Long.parseLong(str_magnitude);
    }

    protected boolean isRegularNumber(char c) {
        return !(c == CH_OPEN || c == CH_SEPARATOR || c == CH_CLOSE);
    }

    /**
     * Check if it is a snailfish number with two single regular numbers
     * @param index
     * @return
     */
    protected boolean isSnailFishRegularNumber(String a, int index) {

        int indexEnd = a.indexOf(CH_CLOSE, index);
        String subnumber = a.substring(index, indexEnd+1);

        return subnumber.charAt(0) == CH_OPEN && subnumber.indexOf(CH_OPEN, 1) == -1 && subnumber.indexOf(CH_CLOSE) == subnumber.length() - 1;
    }

    protected String format(String number_a, String number_b) {
        return String.format("%c%s%c%s%c", CH_OPEN, number_a, CH_SEPARATOR, number_b, CH_CLOSE);
    }

    protected String collapse(String a, int index_begin, int index_end, String replace_str) {
        return new String(a.substring(0, index_begin) + replace_str + a.substring(index_end+1));
    }

    /**
     * It returns a pair of two regular numbers, that are nested inside 'depth' pairs
     * @param depth
     * @return The single snailfish number index with two regular numbers (in case of)
     */
    protected Integer getNestedPair() {

        int current_depth = 0;
        for(int i = 0; i < number.length(); i++) {

            char c = number.charAt(i);
            if(c == CH_OPEN) current_depth++;
            if(c == CH_CLOSE) current_depth--;

            // check if it is a snailfish number with two single regular numbers
            if(current_depth > NESTED_LEVEL && isSnailFishRegularNumber(number, i)) {
                return i;
            }
        }

        return -1;
    }

}

// TODO Avoiding classes & do it directly as a string
/*
class SnailFishNumber {
    Integer leftNumber;
    Integer rightNumber;
    SnailFishNumber leftSnailFishNumber;
    SnailFishNumber rightSnailFishNumber;
    SnailFishNumber father;

    public boolean isConsistent() {
        return leftNumber  == null && leftSnailFishNumber  != null ||
                leftNumber  != null && leftSnailFishNumber  == null ||
                rightNumber == null && rightSnailFishNumber != null || 
                rightNumber != null && rightSnailFishNumber == null;
    }

    public SnailFishNumber add(SnailFishNumber x) {
        SnailFishNumber sum = new SnailFishNumber();
        sum.leftSnailFishNumber = this;
        sum.rightSnailFishNumber = x;
        this.father = sum;
        x.father = sum;
        
        return sum;
    }

    public void reduce() {
        // explode();
        // split();
    }

    protected void explode() {
        if(leftNumber == null || rightNumber == null) {
            logger.printf(Level.ERROR, "Trying to explode a snailfish number with no regular numbers.");
            return;
        }

        var leftFather = father;
        while(leftFather != null) {
            if(leftFather.leftNumber != null) {
                leftFather.leftNumber += leftNumber;
                break;
            } else {
                leftFather = leftFather.father;
            }
        }
        var rightFather = father;
        while(rightFather != null) {
            if(rightFather.rightNumber != null) {
                rightFather.rightNumber += rightNumber;
                break;
            } else {
                rightFather = rightFather.father;
            }
        }

        // then, removes the current snailfish number
        var currentFather = father;
        if(currentFather != null) {
            if(currentFather.leftSnailFishNumber == this) {
                currentFather.leftNumber = 0;
                currentFather.leftSnailFishNumber = null;
            } else {
                currentFather.rightNumber = 0;
                currentFather.rightSnailFishNumber = null;
            }
        }

    }

    @Override
    public String toString() {
        // guard
        if(!isConsistent())
            return "Inconsistent number";

        String left  = leftNumber  != null ? leftNumber.toString()  : leftSnailFishNumber.toString();
        String right = rightNumber != null ? rightNumber.toString() : rightSnailFishNumber.toString();
        return String.format("[%s,%s]", left, right);
    }
}
*/

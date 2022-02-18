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
    boolean doExplode();
    boolean split();
    Long magnitude();

    default boolean isNumber(char c) {
        return !(c == CH_OPEN || c == CH_SEPARATOR || c == CH_CLOSE);
    }

    /**
     * Returns the snailfish number using the canonical string format
     * @param number_a
     * @param number_b
     * @return
     */
    default String format(String number_a, String number_b) {
        return String.format("%c%s%c%s%c", CH_OPEN, number_a, CH_SEPARATOR, number_b, CH_CLOSE);
    }

    /**
     * Check if it is a snailfish number with two single regular numbers
     * @param index
     * @return
     */
    default boolean isSnailFishRegularNumber(String number) {

        String subnumber = number.substring(0, number.indexOf(CH_CLOSE) + 1);

        boolean oneOpenCharacter = subnumber.charAt(0) == CH_OPEN && subnumber.indexOf(CH_OPEN, 1) == -1;
        boolean oneCloseCharacter = subnumber.indexOf(CH_CLOSE) == subnumber.length() - 1;
        boolean oneSeparator = (subnumber.length() - subnumber.replace(String.valueOf(CH_SEPARATOR), "").length() == 1);

        return oneOpenCharacter && oneCloseCharacter && oneSeparator;
    }

    default String getSplittedSnailFishNumber(Integer n) {
        Integer left = n / 2;
        Integer right = left + n % 2;

        return format(left.toString(), right.toString());
    }

    /**
     * Sum involves the actions of addition and reduction of the number
     * @param a the snailfish number to be added to this object number
     */
    default void sum(SnailFishNumber a) {
        add(a);
        reduce();
    }

    default void reduce() {
        logger.printf(Level.DEBUG, "after sum    : %s", toString());
        boolean modified = false;
        do {
            if(modified = doExplode()) {
                logger.printf(Level.DEBUG, "after explode: %s", toString());
            } else if(modified = split()) {
                logger.printf(Level.DEBUG, "after split  : %s", toString());
            }
        } while(modified);
    }

}

class LiteralSnailFishNumber implements SnailFishNumber {

    private String number;

    LiteralSnailFishNumber() {
    }

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

    protected boolean explode(Integer obj) {
        // The obj reference is an Integer, pointing out the index of the regular snailfish number
        Integer indexBegin = obj;

        if(indexBegin == -1) {
            // logger.printf(Level.ERROR, "Trying to explode a snailfish number that doesn't exist.");
            return false;
        }

        if(number.indexOf(number, indexBegin) != -1) {
            logger.printf(Level.ERROR, "Trying to explode a repeated snailfish number.");
            return false;
        }

        // ensure the indexExploded is a 'CH_OPEN' (going to the left)
        while(number.charAt(indexBegin) != CH_OPEN) {
            indexBegin--;
        }

        // get the indexes for the snailfish number to explode
        Integer[] numberValues = getNumbersFromRegularSnailFishNumber(number, indexBegin);
        number = collapse(number, indexBegin, numberValues[2], STR_EXPLODED);

        // replace the number on the right
        Integer rightIndex = -1;
        String strRightChar = "";
        for(int i = indexBegin+1; i < number.length(); i++) {
            char c = number.charAt(i);
            if(isNumber(c)) {
                if(rightIndex == -1)
                    rightIndex = i;

                strRightChar = strRightChar + c;
            } else if(!strRightChar.isEmpty()) {
                break;
            }
        }

        if(rightIndex != -1) {
            numberValues[1] += Integer.parseInt(strRightChar);
            number = collapse(number, rightIndex, rightIndex + strRightChar.length() - 1, numberValues[1].toString());
        }

        // replace the number on the left
        Integer leftIndex = -1;
        String strLeftChar = "";
        for(int i = indexBegin-1; i >= 0; i--) {
            char c = number.charAt(i);
            if(isNumber(c)) {
                leftIndex = i;
                strLeftChar = c + strLeftChar;
            } else if(!strLeftChar.isEmpty()) {
                break;
            }
        }

        if(leftIndex != -1) {
            numberValues[0] += Integer.parseInt(strLeftChar);
            number = collapse(number, leftIndex, leftIndex + strLeftChar.length() - 1, numberValues[0].toString());
        }

        return true;
    }

    public boolean doExplode() {
        return explode(getNestedIndex());
    }

    public boolean split() {
        int index = -1;
        String str = "";
        for(int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if(isNumber(c)) {
                if(index == -1) index = i;
                str += c;
            } else if(!str.equals("")) {
                int n = Integer.parseInt(str);
                if(n < SPLIT_LIMIT) {
                    index = -1;
                    str = "";
                } else {
                    number = collapse(number, index, index+str.length()-1, getSplittedSnailFishNumber(n));
                    return true;
                }
            }
        }

        return false;
    }

    public Long magnitude() {
        String str_magnitude = new String(number);

        do {
            int indexBegin = 0;
            do {
                if(isSnailFishRegularNumber(str_magnitude.substring(indexBegin))) {

                    // get the numbers within, and replace the number by the exploded one
                    Integer[] values = getNumbersFromRegularSnailFishNumber(str_magnitude, indexBegin);
                    Long current_magnitude = (long)(LEFT_FACTOR*values[0] + RIGHT_FACTOR*values[1]);
                    str_magnitude = collapse(str_magnitude, indexBegin, values[2], current_magnitude.toString());
                    break;
                }
                
            } while(indexBegin++ < str_magnitude.length());
        } while(str_magnitude.indexOf(CH_OPEN) != -1);

        return Long.parseLong(str_magnitude);
    }

    private String collapse(String a, int index_begin, int index_end, String replace_str) {
        return new String(a.substring(0, index_begin) + replace_str + a.substring(index_end+1));
    }

    /**
     * Obtain left & right values from the regular snailfish number string
     * (and also the end index, that is usually required next)
     * @param index of the regular snailfish number within the whole number
     * @return an array with [left, right, endIndex] values
     */
    private Integer[] getNumbersFromRegularSnailFishNumber(String n, Integer index) {
        Integer[] values = new Integer[3];

        // get the indexes for the snailfish number to explode
        int indexSeparator = n.indexOf(CH_SEPARATOR, index);
        int indexEnd = n.indexOf(CH_CLOSE, indexSeparator);

        // get the numbers within, and replace the number by the exploded one
        values[0] = Integer.parseInt(n.substring(index+1, indexSeparator));
        values[1] = Integer.parseInt(n.substring(indexSeparator+1, indexEnd));
        values[2] = indexEnd;

        return values;
    }

    /**
     * It returns a pair of two regular numbers, that are nested inside 'depth' pairs
     * @param depth
     * @return The single snailfish number index with two regular numbers (in case of)
     */
    private Integer getNestedIndex() {

        int current_depth = 0;
        for(int i = 0; i < number.length(); i++) {

            char c = number.charAt(i);
            if(c == CH_OPEN) current_depth++;
            if(c == CH_CLOSE) current_depth--;

            // check if it is a snailfish number with two single regular numbers
            if(current_depth > NESTED_LEVEL && isSnailFishRegularNumber(number.substring(i))) {
                return i;
            }
        }

        return -1;
    }

}

class NestedSnailFishNumber implements SnailFishNumber {

    private Integer left;
    private Integer right;
    private NestedSnailFishNumber leftSnailFish;
    private NestedSnailFishNumber rightSnailFish;
    private NestedSnailFishNumber father;
    private boolean isConsistent = true;

    NestedSnailFishNumber() { 
    }

    /**
     * Copy constructor
     * @param copy
     */
    NestedSnailFishNumber(NestedSnailFishNumber copy) {
        this.left = copy.left;
        this.right = copy.right;
        this.leftSnailFish = copy.leftSnailFish;
        this.rightSnailFish = copy.rightSnailFish;
        this.father = copy.father;
        this.isConsistent = copy.isConsistent;
    }

    /**
     * I do have to receive a [NestedSnailFishNumber,NestedSnailFishNumber] structure,
     * so the purpose of the constructor is split it, and keep on calling constructors recursively
     * @param number
     */
    NestedSnailFishNumber(String number) {
        createNestedSnailFishNumber(number);
    }

    public boolean check() {
        if(!isConsistent) {
            return false;
        }

        // update the current node consistency ...
        isConsistent = 
            left  == null && leftSnailFish  != null ||
            left  != null && leftSnailFish  == null ||
            right == null && rightSnailFish != null || 
            right != null && rightSnailFish == null;

        boolean leftCheck = true;
        if(leftSnailFish != null) leftCheck = leftSnailFish.check();
        boolean rightCheck = true;
        if(rightSnailFish != null) rightCheck = rightSnailFish.check();

        return isConsistent && leftCheck && rightCheck;
    }

    @Override
    public String toString() {
        // guard
        if(!isConsistent) return "Inconsistent number";

        String left_str  = left  != null ? left.toString()  : leftSnailFish.toString();
        String right_str = right != null ? right.toString() : rightSnailFish.toString();

        return format(left_str, right_str);
    }

    public void add(SnailFishNumber a) {

        var mini_leftSnailFishNumber  = new NestedSnailFishNumber(this);
        var mini_rightSnailFishNumber = (NestedSnailFishNumber)a;
        
        this.left = null;
        this.right = null;
        this.leftSnailFish  = mini_leftSnailFishNumber;
        this.rightSnailFish = mini_rightSnailFishNumber;
        this.isConsistent = true;

        mini_leftSnailFishNumber.father  = this;
        mini_rightSnailFishNumber.father = this;

        if(mini_leftSnailFishNumber.leftSnailFish != null)
            mini_leftSnailFishNumber.leftSnailFish.father = mini_leftSnailFishNumber;
        if(mini_leftSnailFishNumber.rightSnailFish != null)
            mini_leftSnailFishNumber.rightSnailFish.father = mini_leftSnailFishNumber;
        if(mini_rightSnailFishNumber.leftSnailFish != null)
            mini_rightSnailFishNumber.leftSnailFish.father = mini_rightSnailFishNumber;
        if(mini_rightSnailFishNumber.rightSnailFish != null)
            mini_rightSnailFishNumber.rightSnailFish.father = mini_rightSnailFishNumber;
    }

    public boolean explode(Integer obj) {
        boolean result = false;

        Integer depth = obj;
        
        if(depth > NESTED_LEVEL && isSnailFishRegularNumber(toString())) {
            result = explodeRegularNumber();
        }

        if(!result && leftSnailFish != null) {
            result = leftSnailFish.explode(depth+1);
        }

        if(!result && rightSnailFish != null) {
            result = rightSnailFish.explode(depth+1);
        }

        return result;
    }

    public boolean doExplode() {
        return explode(1);
    }

    public boolean split() {

        boolean splitted = false;

        // left side first
        if(left != null) {
            if(left >= SPLIT_LIMIT) {
                leftSnailFish = new NestedSnailFishNumber(getSplittedSnailFishNumber(left));
                leftSnailFish.father = this;
                left = null;
                splitted = true;
            }
        } else if (leftSnailFish != null) {
            splitted = leftSnailFish.split();
        }

        if(!splitted) {
            // then, right side
            if(right != null) {
                if(right >= SPLIT_LIMIT) {
                    rightSnailFish = new NestedSnailFishNumber(getSplittedSnailFishNumber(right));
                    rightSnailFish.father = this;
                    right = null;
                    splitted = true;
                }
            } else if (rightSnailFish != null) {
                splitted = rightSnailFish.split();
            }
        }

        return splitted;
    }

    public Long magnitude() {
        Long left_magnitude  = (leftSnailFish != null)  ? leftSnailFish.magnitude()  : (long)left;
        Long right_magnitude = (rightSnailFish != null) ? rightSnailFish.magnitude() : (long)right;

        return (long)LEFT_FACTOR * left_magnitude + RIGHT_FACTOR * right_magnitude;
    }

    protected NestedSnailFishNumber getRegularSnailFishNumber(String number) {
        // Assume that the 'number' is a regular number
        if(!isSnailFishRegularNumber(number)) {
            return null;
        }

        return getRecursiveRegularSnailFishNumber(new NestedSnailFishNumber(number));
    }

    private NestedSnailFishNumber getRecursiveRegularSnailFishNumber(NestedSnailFishNumber ref) {

        NestedSnailFishNumber result = null;

        if(result == null && this.equals(ref)) {
            result = this;
        }

        if(result == null && leftSnailFish != null) {
            result = leftSnailFish.getRecursiveRegularSnailFishNumber(ref);
        }

        if(result == null && rightSnailFish != null) {
            result = rightSnailFish.getRecursiveRegularSnailFishNumber(ref);
        }

        return result;
    }

    private boolean explodeRegularNumber() {
        // Trying to explode a snailfish number with no regular numbers
        if(!isSnailFishRegularNumber(toString())) {
            return false;
        }

        // to add the left value, consider the parents right side
        NestedSnailFishNumber iterator = this;
        while(iterator != null && iterator.father != null) {
            var parent = iterator.father;

            if(!iterator.isLeftChild()) {
                if(parent.left != null) {
                    parent.left += this.left;
                } else {
                    parent = parent.leftSnailFish;
                    while(parent.rightSnailFish != null) {
                        parent = parent.rightSnailFish;
                    }
                    parent.right += this.left;
                }

                break;
            }
            
            iterator = iterator.father;
        }

        // adding the right value
        iterator = this;
        while(iterator != null && iterator.father != null) {
            var parent = iterator.father;

            if(iterator.isLeftChild()) {
                if(parent.right != null) {
                    parent.right += this.right;
                } else {
                    parent = parent.rightSnailFish;
                    while(parent.leftSnailFish != null) {
                        parent = parent.leftSnailFish;
                    }
                    parent.left += this.right;
                }

                break;
            }
            
            iterator = iterator.father;
        }

        // Then, replace this as a STR_EXPLODED
        if(isLeftChild()) {
            father.left = Integer.parseInt(STR_EXPLODED);
            father.leftSnailFish = null;
        } else {
            father.right = Integer.parseInt(STR_EXPLODED);
            father.rightSnailFish = null;
        }

        return true;
    }

    private void createNestedSnailFishNumber(String number) {
        if(number.charAt(0) != CH_OPEN) isConsistent = false;
        if(number.charAt(number.length()-1) != CH_CLOSE) isConsistent = false;

        // getting the proper separator
        int nested = 0;
        int index_separator = -1;
        for(int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if(c == CH_OPEN) nested++;
            if(c == CH_CLOSE) nested--;

            if(nested == 1 && c == CH_SEPARATOR) {
                index_separator = i;
                break;
            }
        }

        String left_side  = new String(number.substring(1, index_separator));
        String right_side = new String(number.substring(index_separator+1, number.length()-1));

        // left side
        if(left_side.indexOf(CH_SEPARATOR) != -1) {
            left = null;
            leftSnailFish = new NestedSnailFishNumber(left_side);
            leftSnailFish.father = this;
        } else {
            left = Integer.parseInt(left_side);
            leftSnailFish = null;
        }

        // right side
        if(right_side.indexOf(CH_SEPARATOR) != -1) {
            right = null;
            rightSnailFish = new NestedSnailFishNumber(right_side);
            rightSnailFish.father = this;
        } else {
            right = Integer.parseInt(right_side);
            rightSnailFish = null;
        }
    }

    public boolean equals(NestedSnailFishNumber a) {
        return (this.isConsistent && a.isConsistent) &&
               (this.left != null && a.left != null && this.left == a.left) &&
               (this.right != null && a.right != null && this.right == a.right);
    }

    private boolean isLeftChild() {
        return father != null && father.leftSnailFish == this;
    }

}

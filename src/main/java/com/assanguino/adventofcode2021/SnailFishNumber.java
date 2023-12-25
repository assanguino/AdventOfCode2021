package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface SnailFishNumber {
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
     * @param numberA
     * @param numberB
     * @return
     */
    default String format(String numberA, String numberB) {
        return String.format("%c%s%c%s%c", CH_OPEN, numberA, CH_SEPARATOR, numberB, CH_CLOSE);
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
            modified = doExplode();
            if(modified) {
                logger.printf(Level.DEBUG, "after explode: %s", toString());
                continue;
            }

            modified = split();
            if(modified) {
                logger.printf(Level.DEBUG, "after split  : %s", toString());
            }

        } while(modified);
    }

}

class LiteralSnailFishNumber implements SnailFishNumber {

    private String number;

    public LiteralSnailFishNumber() {
    }

    public LiteralSnailFishNumber(String number) {
        this.number = number;
    }

    public boolean check() {

        String collapsed = number;

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
        number = format(number, a.toString());
    }

    protected boolean explode(Integer obj) {
        // The obj reference is an Integer, pointing out the index of the regular snailfish number
        Integer indexBegin = obj;

        if(indexBegin == -1) {
            return false;
        }

        if(indexBegin == 0) {
            logger.error("Trying to explode an invalid or repeated snailfish number.");
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
        replaceRightNumber(indexBegin, numberValues);

        // replace the number on the left
        replaceLeftNumber(indexBegin, numberValues);

        return true;
    }

    protected void replaceRightNumber(Integer indexBegin, Integer[] numberValues) {
        Integer rightIndex = -1;
        StringBuilder strRightChar = new StringBuilder();
        for(int i = indexBegin+1; i < number.length(); i++) {
            char c = number.charAt(i);
            if(isNumber(c)) {
                if(rightIndex == -1)
                    rightIndex = i;

                strRightChar.append(c);
            } else if(strRightChar.length() > 0) {
                break;
            }
        }

        if(rightIndex != -1) {
            numberValues[1] += Integer.parseInt(strRightChar.toString());
            number = collapse(number, rightIndex, rightIndex + strRightChar.length() - 1, numberValues[1].toString());
        }
    }

    protected void replaceLeftNumber(Integer indexBegin, Integer[] numberValues) {
        Integer leftIndex = -1;
        StringBuilder strLeftChar = new StringBuilder();
        for(int i = indexBegin-1; i >= 0; i--) {
            char c = number.charAt(i);
            if(isNumber(c)) {
                leftIndex = i;
                strLeftChar.insert(0, c);

            } else if(strLeftChar.length() > 0) {
                break;
            }
        }

        if(leftIndex != -1) {
            numberValues[0] += Integer.parseInt(strLeftChar.toString());
            number = collapse(number, leftIndex, leftIndex + strLeftChar.length() - 1, numberValues[0].toString());
        }
    }

    public boolean doExplode() {
        return explode(getNestedIndex());
    }

    public boolean split() {
        int index = -1;
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if(isNumber(c)) {
                if(index == -1) index = i;
                str.append(c);
            } else if(str.length() > 0) {
                int n = Integer.parseInt(str.toString());
                if(n < SPLIT_LIMIT) {
                    index = -1;
                    str.setLength(0);
                } else {
                    number = collapse(number, index, index+str.length()-1, getSplittedSnailFishNumber(n));
                    return true;
                }
            }
        }

        return false;
    }

    public Long magnitude() {
        String strMagnitude = number;

        do {
            int indexBegin = 0;
            do {
                if(isSnailFishRegularNumber(strMagnitude.substring(indexBegin))) {

                    // get the numbers within, and replace the number by the exploded one
                    Integer[] values = getNumbersFromRegularSnailFishNumber(strMagnitude, indexBegin);
                    Long currentMagnitude = (long)(LEFT_FACTOR*values[0] + RIGHT_FACTOR*values[1]);
                    strMagnitude = collapse(strMagnitude, indexBegin, values[2], currentMagnitude.toString());
                    break;
                }
                
            } while(indexBegin++ < strMagnitude.length());
        } while(strMagnitude.indexOf(CH_OPEN) != -1);

        return Long.parseLong(strMagnitude);
    }

    private String collapse(String a, int indexBegin, int indexEnd, String replaceStr) {
        return a.substring(0, indexBegin) + replaceStr + a.substring(indexEnd+1);
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

        int currentDepth = 0;
        for(int i = 0; i < number.length(); i++) {

            char c = number.charAt(i);
            if(c == CH_OPEN) currentDepth++;
            if(c == CH_CLOSE) currentDepth--;

            // check if it is a snailfish number with two single regular numbers
            if(currentDepth > NESTED_LEVEL && isSnailFishRegularNumber(number.substring(i))) {
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

    public NestedSnailFishNumber() { 
    }

    /**
     * Copy constructor
     * @param copy
     */
    public NestedSnailFishNumber(NestedSnailFishNumber copy) {
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
    public NestedSnailFishNumber(String number) {
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

        String leftStr  = left  != null ? left.toString()  : leftSnailFish.toString();
        String rightStr = right != null ? right.toString() : rightSnailFish.toString();

        return format(leftStr, rightStr);
    }

    public void add(SnailFishNumber a) {

        var miniLeftSnailFishNumber  = new NestedSnailFishNumber(this);
        var miniRightSnailFishNumber = (NestedSnailFishNumber)a;
        
        this.left = null;
        this.right = null;
        this.leftSnailFish  = miniLeftSnailFishNumber;
        this.rightSnailFish = miniRightSnailFishNumber;
        this.isConsistent = true;

        miniLeftSnailFishNumber.father  = this;
        miniRightSnailFishNumber.father = this;

        if(miniLeftSnailFishNumber.leftSnailFish != null)
            miniLeftSnailFishNumber.leftSnailFish.father = miniLeftSnailFishNumber;
        if(miniLeftSnailFishNumber.rightSnailFish != null)
            miniLeftSnailFishNumber.rightSnailFish.father = miniLeftSnailFishNumber;
        if(miniRightSnailFishNumber.leftSnailFish != null)
            miniRightSnailFishNumber.leftSnailFish.father = miniRightSnailFishNumber;
        if(miniRightSnailFishNumber.rightSnailFish != null)
            miniRightSnailFishNumber.rightSnailFish.father = miniRightSnailFishNumber;
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
        Long leftMagnitude  = (leftSnailFish != null)  ? leftSnailFish.magnitude()  : (long)left;
        Long rightMagnitude = (rightSnailFish != null) ? rightSnailFish.magnitude() : (long)right;

        return LEFT_FACTOR * leftMagnitude + RIGHT_FACTOR * rightMagnitude;
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

        if(this.equals(ref)) {
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

    private void addingLeftValue() {
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
    }

    private void addingRightValue() {
        NestedSnailFishNumber iterator = this;
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
    }

    private boolean explodeRegularNumber() {
        // Trying to explode a snailfish number with no regular numbers
        if(!isSnailFishRegularNumber(toString())) {
            return false;
        }

        // to add the left value, consider the parents right side
        addingLeftValue();

        // adding the right value
        addingRightValue();

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
        int indexSeparator = -1;
        for(int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if(c == CH_OPEN) nested++;
            if(c == CH_CLOSE) nested--;

            if(nested == 1 && c == CH_SEPARATOR) {
                indexSeparator = i;
                break;
            }
        }

        String leftSide  = number.substring(1, indexSeparator);
        String rightSide = number.substring(indexSeparator+1, number.length()-1);

        // left side
        if(leftSide.indexOf(CH_SEPARATOR) != -1) {
            left = null;
            leftSnailFish = new NestedSnailFishNumber(leftSide);
            leftSnailFish.father = this;
        } else {
            left = Integer.parseInt(leftSide);
            leftSnailFish = null;
        }

        // right side
        if(rightSide.indexOf(CH_SEPARATOR) != -1) {
            right = null;
            rightSnailFish = new NestedSnailFishNumber(rightSide);
            rightSnailFish.father = this;
        } else {
            right = Integer.parseInt(rightSide);
            rightSnailFish = null;
        }
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof NestedSnailFishNumber))
            return false;
        if (obj == this)
            return true;

        NestedSnailFishNumber a = (NestedSnailFishNumber) obj;
        return (this.isConsistent && a.isConsistent) &&
               (this.left != null && a.left != null && this.left.equals(a.left)) &&
               (this.right != null && a.right != null && this.right.equals(a.right));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    private boolean isLeftChild() {
        return father != null && father.leftSnailFish == this;
    }

}

class SnailFishFactory {
    public static final Logger logger = LogManager.getLogger(SnailFishFactory.class.getName());
    private List<Class<? extends SnailFishNumber>> snailfishClasses = new ArrayList<>();
    private int snailfishType;
    private Random rand = new Random();

    public SnailFishFactory(Integer type) {

        populate();

        if(type == null || type > snailfishClasses.size()) {
            logger.error("The SnailFishNumber class is not valid. Get the default one");
            snailfishType = chooseType();
        } else {
            snailfishType = type;
        }
    }
    
    private void populate() {
        snailfishClasses.add(LiteralSnailFishNumber.class);
        snailfishClasses.add(NestedSnailFishNumber.class);
    }

    private Integer chooseType() {
        return rand.nextInt(snailfishClasses.size());
    }

    public SnailFishNumber getNewSnailFishNumber(String string) {
        SnailFishNumber object = null;
        try {
            Class<?>[] constructorParams = { String.class };
            Class<? extends SnailFishNumber> dayClass = snailfishClasses.get(snailfishType);
            object = dayClass.getConstructor(constructorParams).newInstance(string);
        } catch (Exception ex) {
            logger.printf(Level.FATAL, ex.getMessage());
        }

        return object;
    }
}


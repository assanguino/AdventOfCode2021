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
    boolean explode(Object obj);
    boolean split();
    void reduce();
    Long magnitude();

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
    default boolean isSnailFishRegularNumber(String a, int index) {

        int indexEnd = a.indexOf(CH_CLOSE, index);

        String subnumber = a.substring(index, indexEnd+1);

        boolean oneOpenCharacter = subnumber.charAt(0) == CH_OPEN && subnumber.indexOf(CH_OPEN, 1) == -1;
        boolean oneCloseCharacter = subnumber.indexOf(CH_CLOSE) == subnumber.length() - 1;
        boolean oneSeparator = (subnumber.length() - subnumber.replace(String.valueOf(CH_SEPARATOR), "").length() == 1);

        return oneOpenCharacter && oneCloseCharacter && oneSeparator;
    }

    /**
     * Sum involves the actions of addition and reduction of the number
     * @param a the snailfish number to be added to this object number
     */
    default void sum(SnailFishNumber a) {
        add(a);
        reduce();
    }

}

class LiteralSnailFishNumber implements SnailFishNumber {

    protected String number;

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

    public boolean explode(Object obj) {

        // The obj reference is an Integer, pointing out the index of the regular snailfish number
        Integer indexBegin = (Integer)obj;

        if(indexBegin == -1) {
            logger.printf(Level.ERROR, "Trying to explode a snailfish number that doesn't exist.");
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

        return true;
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

        logger.printf(Level.DEBUG, "after sum    : %s", toString());

        do {
            previous_number = new String(number);

            while((index = getNestedPair()) != -1) {
                explode(index);
                logger.printf(Level.DEBUG, "after explode: %s (index %d)", toString(), index);
            }
    
            split();
            logger.printf(Level.DEBUG, "after split  : %s", toString());

        } while(!number.equals(previous_number));
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

class NestedSnailFishNumber implements SnailFishNumber {

    Integer left;
    Integer right;
    protected NestedSnailFishNumber leftSnailFish;
    protected NestedSnailFishNumber rightSnailFish;
    protected NestedSnailFishNumber father;
    protected boolean isConsistent = true;

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

    public boolean explode(Object obj) {
        boolean result = false;

        Integer depth = (Integer)obj;
        
        if(depth > NESTED_LEVEL && isSnailFishRegularNumber(toString(), 0)) {
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

    public boolean split() {

        boolean splitted = false;

        // left side first
        if(left != null) {
            if(left >= SPLIT_LIMIT) {
                Integer new_left = left / 2;
                Integer new_right = new_left + left % 2;
    
                leftSnailFish = new NestedSnailFishNumber(format(new_left.toString(), new_right.toString()));
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
                    Integer new_left = right / 2;
                    Integer new_right = new_left + right % 2;
        
                    rightSnailFish = new NestedSnailFishNumber(format(new_left.toString(), new_right.toString()));
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

    public void reduce() {
        logger.printf(Level.DEBUG, "after sum    : %s", toString());

        boolean x = false;
        do {
            x = explode(1);

            if(x) logger.printf(Level.DEBUG, "after explode: %s", toString());

            if(!x) {
                x = split();
                if(x) logger.printf(Level.DEBUG, "after split  : %s", toString());
            }
        } while(x);
    }

    public Long magnitude() {
        Long left_magnitude  = (leftSnailFish != null)  ? leftSnailFish.magnitude()  : (long)left;
        Long right_magnitude = (rightSnailFish != null) ? rightSnailFish.magnitude() : (long)right;

        return (long)LEFT_FACTOR * left_magnitude + RIGHT_FACTOR * right_magnitude;
    }

    public NestedSnailFishNumber getRegularSnailFishNumber(String number) {
        // Assume that the 'number' is a regular number
        if(!isSnailFishRegularNumber(number, 0)) {
            return null;
        }

        return getRecursiveRegularSnailFishNumber(new NestedSnailFishNumber(number));
    }

    protected boolean explodeRegularNumber() {
        // Trying to explode a snailfish number with no regular numbers
        if(!isSnailFishRegularNumber(this.toString(), 0)) {
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

    protected NestedSnailFishNumber getRecursiveRegularSnailFishNumber(NestedSnailFishNumber ref) {

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

    protected void createNestedSnailFishNumber(String number) {
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

    protected boolean equals(NestedSnailFishNumber a) {
        return (this.isConsistent && a.isConsistent) &&
               (this.left != null && a.left != null && this.left == a.left) &&
               (this.right != null && a.right != null && this.right == a.right);
    }

    protected boolean isLeftChild() {
        return father != null && father.leftSnailFish == this;
    }

}

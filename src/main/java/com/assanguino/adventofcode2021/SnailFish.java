package com.assanguino.adventofcode2021;

public class SnailFish implements Executable {

    protected Part part;

    SnailFishNumber cumulative = null;
    Long magnitude;
    
    public SnailFish(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        // Comment or blank line, so skip
        if(row.length() == 0 || row.charAt(0) == '#')
            return;

        var number = new LiteralSnailFishNumber(row);
        number.check();

        if(cumulative == null) {
            cumulative = number;
        } else {
            cumulative.sum(number);
        }
    }

    public void execute() {
        magnitude = cumulative.magnitude();
    }

    public String printDescription() {
        return "What is the magnitude of the final sum ?";
    }

    public void printResult() {
        System.out.println();
        System.out.println("result of the cumulative sum: " + cumulative.toString());
        System.out.println("magnitude: " + getResult());
    }

    public String getResult() {
        return magnitude.toString();
    }

}

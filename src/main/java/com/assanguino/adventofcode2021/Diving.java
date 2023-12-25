package com.assanguino.adventofcode2021;

public class Diving implements Executable {

    protected Part part;
    protected int noRows = -1;
    protected int horizontal = 0;
    protected int depth = 0;
    protected int aim = 0;

    public Diving(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        String[] chains = row.split(" ");
        if (chains.length != 2)
            return;

        noRows++;

        int value = Integer.parseInt(chains[1]);

        if (part == Part.FIRST) {
            if (chains[0].equals("forward")) {
                horizontal += value;
            } else if (chains[0].equals("up")) {
                depth -= value;
            } else if (chains[0].equals("down")) {
                depth += value;
            }
        } else {
            if (chains[0].equals("forward")) {
                horizontal += value;
                depth += aim * value;
            } else if (chains[0].equals("up")) {
                aim -= value;
            } else if (chains[0].equals("down")) {
                aim += value;
            }
        }
    }

    public void execute() {
        // Do nothing, all done when row processing!
    }

    public String printDescription() {
        return (part == Part.FIRST) ? 
            "Dive! - What do you get if you multiply your final horizontal position by your final depth ?" : 
            "Dumbo Octopus - What do you get if you multiply your final horizontal position by your final depth ?";
    }

    public void printResult() {
        logger.info("measurements: %d", noRows);
        logger.info("horizontal position: %d", horizontal);
        logger.info("depth position: %d", depth);
        logger.info("final value (multiplication): %ld", horizontal * depth);
    }

    public String getResult() {
        return String.valueOf(horizontal * depth);
    }

}

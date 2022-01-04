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

        if (part == Part.first) {
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
        return (part == Part.first) ? 
            "Dive! - What do you get if you multiply your final horizontal position by your final depth ?" : 
            "Dumbo Octopus - What do you get if you multiply your final horizontal position by your final depth ?";
    }

    public void printResult() {
        System.out.println("measurements: " + noRows);
        System.out.println("horizontal position: " + horizontal);
        System.out.println("depth position: " + depth);
        System.out.println("final value (multiplication): " + horizontal * depth);
    }

    public String getResult() {
        return String.valueOf(horizontal * depth);
    }

}

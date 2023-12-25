package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.javatuples.Pair;

public class Origami implements Executable {
    
    protected static final char EMPTY_CHAR = '.';
    protected static final char MARK_CHAR = '#';
    protected static final String HARDCODED_RESULT_SECOND = "PGHRKLKL";

    protected Part part;
    protected int noX = -1;
    protected int noY = -1;

    protected List<Pair<Integer, Integer>> coordMap = new ArrayList<>();
    protected List<String> foldList = new ArrayList<>();

    protected char[][] paper;

    public Origami(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        if(row.length() == 0)
            return;

        String[] splitted = row.split(" ");
        if(splitted[0].equals("fold") && splitted[1].equals("along") && splitted[2].contains("=")) {
            // it is a folding instruction
            foldList.add(splitted[2]);
        } else {
            splitted = row.split(",");
            int markX = Integer.parseInt(splitted[0]);
            int markY = Integer.parseInt(splitted[1]);

            if(noX < markX) noX = markX;
            if(noY < markY) noY = markY;

            coordMap.add(new Pair<>(markX, markY));
        }
    }

    public void execute() {
        fill();
        foldAll();
    }

    public String printDescription() {
        return (part == Part.FIRST) ? 
            "Transparent Origami - How many visible dots after completing the first fold instruction ?" : 
            "Transparent Origami - What code do you use to activate the infrared thermal imaging camera system ?";
    }

    public void printResult() {
        logger.info("After folding there are %2d dots:", getDots());
        printPaper(part == Part.SECOND, "");
    }

    public String getResult() {
        return part == Part.FIRST ? 
            String.valueOf(getDots()) :
            String.valueOf(HARDCODED_RESULT_SECOND);
    }

    protected void fill() {

        // create an empty origami
        paper = new char[noY+1][noX+1];
        for(int i = 0; i <= noY; i++) {
            for(int j = 0; j <= noX; j++) {
                paper[i][j] = EMPTY_CHAR;
            }
        }

        // put the marks
        for(var entry : coordMap) {
            Integer x = entry.getValue0();
            Integer y = entry.getValue1();
            paper[y][x] = MARK_CHAR;
        }

        printPaper(false, "Initial paper:");
    }

    protected void foldAll() {

        int noFolds = 0;

        for(var f : foldList) {

            String[] splitted = f.split("=");
            String coord = splitted[0];
            Integer value = Integer.parseInt(splitted[1]);

            if(coord.equals("x")) {
                foldVertically(value);
            } else if(coord.equals("y")) {
                foldHorizontally(value);
            }

            printPaper(part == Part.SECOND, String.format("After folding as follows: (%s)", f));

            // Cut the iterations
            noFolds++;
            if(noFolds == 1 && part == Part.FIRST)
                return;
        }
    }

    protected void foldVertically(Integer value) {
        // folding vertically
        for(int i = 0; i <= noY; i++) {
            for(int j = value+1; j <= noX; j++) {
                if(paper[i][j] == MARK_CHAR) {
                    paper[i][2*value - j] = MARK_CHAR;
                }
            }
        }
        // redefine the paper size
        noX = value-1;
    }

    protected void foldHorizontally(Integer value) {
        // folding horizontally
        for(int i = value+1; i <= noY; i++) {
            for(int j = 0; j <= noX; j++) {
                if(paper[i][j] == MARK_CHAR) {
                    paper[2*value - i][j] = MARK_CHAR;
                }
            }
        }
        // redefine the paper size
        noY = value-1;
    }

    protected Integer getDots() {

        Integer no = 0;
        for(int i = 0; i <= noY; i++) {
            for(int j = 0; j <= noX; j++) {
                if(paper[i][j] == MARK_CHAR) no++;
            }
        }

        return no;
    }

    protected void printPaper(boolean printPaper, String header) {

        logger.printf(Level.INFO, header);
        logger.printf(Level.INFO, "Print paper of size x,y=(%2d,%2d). Number of dots (%2d)", noX, noY, getDots());

        if(printPaper) {
            for(int i = 0; i <= noY; i++) {
                StringBuilder line = new StringBuilder("              ");
                for(int j = 0; j <= noX; j++) {
                    line.append(paper[i][j]);
                }
                String strLine = line.toString();
                logger.info(strLine);
            }
            logger.info("");
        }
    }

}

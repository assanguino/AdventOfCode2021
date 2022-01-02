package com.assanguino;

import java.util.ArrayList;
import java.util.List;

import com.assanguino.App.Part;

import org.javatuples.Pair;

public class Origami {
    
    protected final char EMPTY_CHAR = '.';
    protected final char MARK_CHAR = '#';

    protected int noX = -1;
    protected int noY = -1;

    protected List<Pair<Integer, Integer>> coordMap = new ArrayList<>();
    protected List<String> foldList = new ArrayList<String>();

    protected char[][] paper;

    public void processRow(String row) {

        if(row.length() == 0)
            return;

        String[] splitted = row.split(" ");
        if(splitted[0].equals("fold") && splitted[1].equals("along") && splitted[2].contains("=")) {
            // it is a folding instruction
            foldList.add(splitted[2]);
        } else {
            splitted = row.split(",");
            int mark_X = Integer.parseInt(splitted[0]);
            int mark_Y = Integer.parseInt(splitted[1]);

            if(noX < mark_X) noX = mark_X;
            if(noY < mark_Y) noY = mark_Y;

            coordMap.add(new Pair<Integer, Integer>(mark_X, mark_Y));
        }
    }

    public void fill() {

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

        System.out.println(String.format("Initial paper:"));
        printPaper(false);
    }

    public void foldAll(Part part) {

        int noFolds = 0;

        for(var f : foldList) {

            String[] splitted = f.split("=");
            String coord = splitted[0];
            Integer value = Integer.parseInt(splitted[1]);

            if(coord.equals("x")) {
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

            } else if(coord.equals("y")) {
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

            System.out.println(String.format("After folding as follows: (%s)", f));
            printPaper(part == Part.second);

            // Cut the iterations
            noFolds++;
            if(noFolds == 1 && part == Part.first)
                return;
        }
    }

    public Integer getDots() {

        Integer no = 0;
        for(int i = 0; i <= noY; i++) {
            for(int j = 0; j <= noX; j++) {
                if(paper[i][j] == MARK_CHAR) no++;
            }
        }

        return no;
    }

    protected void printPaper(boolean printPaper) {

        System.out.println(String.format("   Print paper of size x,y=(%2d,%2d). Number of dots (%2d)", noX, noY, getDots() ));

        if(printPaper) {
            for(int i = 0; i <= noY; i++) {
                System.out.print("              ");
                for(int j = 0; j <= noX; j++) {
                    System.out.print(paper[i][j]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

}

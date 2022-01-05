package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;

public class ChunkReader implements Executable {
    
    protected static final String openChunks = "([{<";
    protected static final String closeChunks = ")]}>";
    protected static final int[] syntax_error_points = {3, 57, 1197, 25137};
    protected static final int[] incompletion_points = {1, 2, 3, 4};

    protected Part part;
    protected int[] chunkCounter;
    protected int syntax_error_score;
    protected List<Long> completionScoreList;
    protected int noRows = 0;

    public ChunkReader(Part part) {
        this.part = part;
        syntax_error_score = 0;
        completionScoreList = new ArrayList<>();
        noRows = 0;
    }

    public void processRow(String row) {

        // It's a counter matrix
        // first row, related to ( )
        // second row, related to [ ]
        // third row, related to { }
        // fourth row, related to < >
        chunkCounter = new int[4];
        chunkCounter[0] = chunkCounter[1] = 0;
        chunkCounter[2] = chunkCounter[3] = 0;

        // It's the future chunk string, expected to be at the end ...
        String expectedChunks = "";

        noRows++;
        char[] lineArray = row.toCharArray();

        for(int i = 0; i < lineArray.length; i++) {

            char c = lineArray[i];

            // open chunks
            int openChunckIndex = openChunks.indexOf(c);
            if(openChunckIndex != -1) {
                // It's a close chunk, so add its open chunk to the expected chunks (at the end of)
                expectedChunks = closeChunks.charAt(openChunckIndex) + expectedChunks;
            } else if(closeChunks.indexOf(c) != -1) {
                // It's an open chunk, so substract its corresponding close chunk from the expected, or declare it as corrupted
                if(c == expectedChunks.charAt(0)) {
                    expectedChunks = expectedChunks.substring(1);
                } else {
                    // Corrupted
                    logger.printf(Level.DEBUG, "Expected %c, but found %c instead", expectedChunks.charAt(0), c);

                    syntax_error_score += getIllegarCharacterScore(c);

                    return;
                }
            }
        }

        // Here there are no corrupted lines, but maybe incompletes
        if(expectedChunks.length() > 0) {
            addCompletionStringScore(expectedChunks);
            logger.printf(Level.DEBUG, "Incomplete line - Complete by adding %s.", expectedChunks);
        }

    }

    public void execute() {
        // An empty method; all is done when reading the file (row by row)
    }

    public String printDescription() {
        return (part == Part.first) ? 
            "Syntax Scoring - What is the total syntax error ?" : 
            "Syntax Scoring - What is the middle score ?";
    }

    public void printResult() {
        System.out.println(String.format("number of measurements (lines): %2d", noRows));

        if(part == Part.first) {
            System.out.println(String.format("syntax error score: %2d", syntax_error_score ));
        } else {
            System.out.println(String.format("competion middle score: %2d", getCompletionStringScore()));
        }
    }

    public String getResult() {
        return part == Part.first ?
            String.valueOf(syntax_error_score) :
            String.valueOf(getCompletionStringScore());
    }

    protected long addCompletionStringScore(String completion) {
        long score = 0;

        for(int i = 0; i < completion.length(); i++) {
            score = 5*score + getIncompleteCharacterScore(completion.charAt(i));
        }

        completionScoreList.add(score);

        return score;
    }

    public long getCompletionStringScore() {
        Object[] arrayCompletion = completionScoreList.toArray();
        Arrays.sort(arrayCompletion);

        int middle = (int)(arrayCompletion.length-1)/2;

        return (long)arrayCompletion[middle];
    }

    protected int getIllegarCharacterScore(char c) {
        return syntax_error_points[closeChunks.indexOf(c)];
    }

    protected int getIncompleteCharacterScore(char c) {
        return incompletion_points[closeChunks.indexOf(c)];
    }

}

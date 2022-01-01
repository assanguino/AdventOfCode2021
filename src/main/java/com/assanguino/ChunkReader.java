package com.assanguino;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChunkReader {
    
    protected static final String openChunks = "([{<";
    protected static final String closeChunks = ")]}>";
    protected static final int[] syntax_error_points = {3, 57, 1197, 25137};
    protected static final int[] incompletion_points = {1, 2, 3, 4};

    protected int[] chunkCounter;
    protected int syntax_error_score;
    protected List<Long> completionScoreList;

    public ChunkReader() {
        syntax_error_score = 0;
        completionScoreList = new ArrayList<>();
    }

    public void processLine(String line) {

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

        char [] lineArray = line.toCharArray();
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
                    // System.out.println(String.format("Expected %c, but found %c instead", expectedChunks.charAt(0), c));
                    syntax_error_score += getIllegarCharacterScore(c);

                    return;
                }
            }
        }

        // Here there are no corrupted lines, but maybe incompletes
        if(expectedChunks.length() > 0) {
            addCompletionStringScore(expectedChunks);
            // System.out.println(String.format("Incomplete line - %s - Complete by adding %s.", line, expectedChunks));
        }

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

    public int getSyntaxErrorScore() {
        return syntax_error_score;
    }

}

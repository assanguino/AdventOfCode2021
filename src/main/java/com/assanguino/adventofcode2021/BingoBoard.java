package com.assanguino.adventofcode2021;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BingoBoard {
    
    public static final Logger logger = LogManager.getLogger(BingoBoard.class.getName());
    
    public static final int BOARD_SIZE = 5;

    protected Integer[][] board = new Integer[BOARD_SIZE][BOARD_SIZE];
    protected Boolean[][] marked = new Boolean[BOARD_SIZE][BOARD_SIZE];
    protected boolean won = false;

    public BingoBoard() {
        init();
    }

    /**
     * Initialize the mark matrix
     */
    protected void init() {
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                marked[i][j] = false;
            }
        }

        won = false;
    }

    /**
     * Populate the board, row by row
     * @param row
     * @param strRow
     */
    public void populateRow(int row, String strRow) {
        int i = 0;
        for(String str : strRow.split(" ")) {
            if(!str.equals("")) {
                board[row][i] = Integer.parseInt(str);
                i++;
            }
        }
    }

    /**
     * Mark if the number is in the bingo board,
     * marking it
     * @param number
     */
    public void mark(int number) {
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                if(board[i][j] == number) {
                    marked[i][j] = true;
                    return;
                }
            }
        }
    }

    /**
     * Check whether a single row is completely marked
     * @param row
     * @return
     */
    protected boolean checkRow(int row) {
        for(int j = 0; j < BOARD_SIZE; j++) {
            if(!marked[row][j].booleanValue())
                return false;
        }
        return true;
    }

    /**
     * Check whether a single column is completely marked
     * @param column
     * @return
     */
    protected boolean checkColumn(int column) {
        for(int i = 0; i < BOARD_SIZE; i++) {
            if(!marked[i][column].booleanValue())
                return false;
        }
        return true;
    }

    /**
     * Whether a single board has marked a complete line or column
     * @return
     */
    public boolean checkBingo() {        
        for(int i = 0; i < BOARD_SIZE; i++) {
            if(checkRow(i)) {
                won = true;
                return true;
            }
        }

        for(int j = 0; j < BOARD_SIZE; j++) {
            if(checkColumn(j)) {
                won = true;
                return true;
            }
        }

        return false;
    }

    public boolean hasWon() {
        return won;
    }

    /**
     * Print put the board
     */
    public void printBoard() {

        String line = "";
        String marksLine = "";
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                line = String.format("%s %2d", line, board[i][j]);
                marksLine = String.format("%s %s", marksLine, marked[i][j].booleanValue() ? "X" : "O");
            }
            logger.info(" |%s |%s | ", line, marksLine);
            line = marksLine = "";
        }
    }

    /**
     * Calculate the board score
     * (the sum of all the unmarked values in thew board)
     * @return
     */
    public int getScore() {
        int score = 0;
        for(int i = 0; i < BOARD_SIZE; i++) {
            for(int j = 0; j < BOARD_SIZE; j++) {
                if(!marked[i][j].booleanValue())
                    score += board[i][j];
            }
        }
        return score;
    }

}

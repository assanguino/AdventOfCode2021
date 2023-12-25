package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.List;

public class GiantSquidBingo implements Executable {

    protected Part part;
    protected List<Integer> randomNumbers = new ArrayList<>();
    protected List<BingoBoard> boardList = new ArrayList<>();
    protected int boardRowCounter = 0;

    protected int currentRandomNumber = -1;
    protected BingoBoard currentBoard = null;

    public GiantSquidBingo(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        // Populate the random numbers list
        if(randomNumbers.isEmpty()) {
            for(String str : row.split(",")) {
                randomNumbers.add(Integer.parseInt(str));
            }
            
        } else if (row.length() == 0) {
            // Reinit counter
            boardRowCounter = 0;
        } else {
            // Populate all the bingo boards
            if(boardRowCounter == 0) {
                BingoBoard board = new BingoBoard();
                boardList.add(board);
                board.populateRow(boardRowCounter, row);
            } else {
                BingoBoard last = boardList.get(boardList.size()-1);
                last.populateRow(boardRowCounter, row);
            }

            // By the way ...
            if(++boardRowCounter == BingoBoard.BOARD_SIZE)
                boardRowCounter = 0;
        }
    }

    public void execute() {
        // Play!
        if(part == Part.FIRST) {
            for(Integer random : randomNumbers) {
                for(BingoBoard board : boardList) {

                    board.mark(random);
                    if(board.checkBingo()) {
                        currentRandomNumber = random;
                        currentBoard = board;
                        return;
                    }
                }
            }
        } else {

            int loserBoards = boardList.size();
            for(Integer random : randomNumbers) {
                for(BingoBoard board : boardList) {
                    // do not process the already winning boards
                    if(board.hasWon())
                        continue;
                    
                    board.mark(random);
                    if(board.checkBingo() && --loserBoards == 0) {
                        currentRandomNumber = random;
                        currentBoard = board;
                        return;
                    }
                }
            }
        }
    }

    public String printDescription() {
        return (part == Part.FIRST) ? 
            "Giant Squid - What will your final score be if you choose that board ?" : 
            "Giant Squid - Figure out which board will win last. Once it wins, what would its final score be ?";
    }

    public void printResult() {
        if(currentBoard == null) {
            logger.info("There is no final result! No bingo board has been chosen!");
            return;
        }

        logger.info("number of random numbers: %d", randomNumbers.size());
        logger.info("number of bingo boards: %d", boardList.size());
        logger.info("winning random number: %d", currentRandomNumber);
        currentBoard.printBoard();
        logger.info("final score: %ld", currentRandomNumber * currentBoard.getScore());
    }

    public String getResult() {
        return String.valueOf(currentRandomNumber * currentBoard.getScore());
    }

}


package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.List;

public class GiantSquidBingo implements Executable {

    protected Part part;
    protected List<Integer> random_numbers = new ArrayList<>();
    protected List<BingoBoard> boardList = new ArrayList<>();
    protected int board_row_counter = 0;

    protected int currentRandomNumber = -1;
    protected BingoBoard currentBoard = null;

    public GiantSquidBingo(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        // Populate the random numbers list
        if(random_numbers.size() == 0) {
            for(String str : row.split(",")) {
                random_numbers.add(Integer.parseInt(str));
            }
            
        } else if (row.length() == 0) {
            // Reinit counter
            board_row_counter = 0;
        } else {
            // Populate all the bingo boards
            if(board_row_counter == 0) {
                BingoBoard board = new BingoBoard();
                boardList.add(board);
                board.populateRow(board_row_counter, row);
            } else {
                BingoBoard last = boardList.get(boardList.size()-1);
                last.populateRow(board_row_counter, row);
            }

            // By the way ...
            if(++board_row_counter == BingoBoard.BOARD_SIZE)
                board_row_counter = 0;
        }
    }

    public void execute() {
        // Play!
        if(part == Part.first) {
            for(Integer random : random_numbers) {
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
            for(Integer random : random_numbers) {
                for(BingoBoard board : boardList) {
                    // do not process the already winning boards
                    if(board.hasWon())
                        continue;
                    
                    board.mark(random);
                    if(board.checkBingo()) {
                        if(-- loserBoards == 0) {
                            currentRandomNumber = random;
                            currentBoard = board;
                            return;
                        }
                    }
                }
            }
        }
    }

    public String printDescription() {
        return (part == Part.first) ? 
            "Giant Squid - What will your final score be if you choose that board ?" : 
            "Giant Squid - Figure out which board will win last. Once it wins, what would its final score be ?";
    }

    public void printResult() {
        if(currentBoard == null) {
            System.out.println("There is no final result! No bingo board has been chosen!");
            return;
        }

        System.out.println("number of random numbers: " + random_numbers.size());
        System.out.println("number of bingo boards: " + boardList.size());
        System.out.println("winning random number: " + currentRandomNumber);
        currentBoard.printBoard();
        System.out.println("final score: " + currentRandomNumber * currentBoard.getScore());
    }

    public String getResult() {
        return String.valueOf(currentRandomNumber * currentBoard.getScore());
    }

}


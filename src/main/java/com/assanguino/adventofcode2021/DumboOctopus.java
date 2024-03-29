package com.assanguino.adventofcode2021;

public class DumboOctopus implements Executable {
    
    protected Part part;
    protected int[][] map;
    protected int mapSize = -1;
    protected int noRows = -1;
    protected long flashes = -1;
    protected long stepNumber = -1;

    public DumboOctopus(Part part) {
        this.part = part;
    }

    public void processRow(String row) {

        if(mapSize == -1) {
            // not processed
            mapSize = row.length();
            map = new int[mapSize][mapSize];
            noRows = 0;
        }

        for(int i = 0; i < mapSize; i++) {
            map[noRows][i] = row.charAt(i) - '0';
        }

        noRows++;
    }

    public void execute() {
        flashes = 0;
        stepNumber = 0;
        if(part == Part.FIRST) {
            while(stepNumber < 100) {
                nextStep();
            }

        } else {
            do {
                nextStep();
            } while(!isFlashSync());
        }
    }

    public String printDescription() {
        return (part == Part.FIRST) ? 
            "Dumbo Octopus - How many flashes are there after 100 steps ?" : 
            "Dumbo Octopus - What is the first step during which all octopuses flash ?";
    }

    public void printResult() {
        logger.info("After step %d: ", stepNumber);
        printMap();
        logger.info("");
        logger.info("number of measurements (lines): %2d", noRows);
        logger.info("number of flashes: %2d", flashes);
    }

    public String getResult() {
        return part == Part.FIRST ? 
            String.valueOf(flashes) :
            String.valueOf(stepNumber);
    }

    protected void nextStep() {
        // first, reset the flashes
        for(int i = 0; i < mapSize; i++) {
            for(int j = 0; j < mapSize; j++) {
                if(map[i][j] == 0) 
                    map[i][j] = -1;
            }
        }

        // then, increase the octopuses energy level
        for(int i = 0; i < mapSize; i++) {
            for(int j = 0; j < mapSize; j++) {
                increaseEnergy(i, j);
            }
        }

        stepNumber++;
    }

    protected void increaseEnergy(int i, int j) {
        // energy can't be increased; it's already lightning
        if(map[i][j] == 0)
            return;

        // increase energy
        if(map[i][j] == -1)
            map[i][j] = 1;
        else
            map[i][j]++;

        // if the energy > 9, it flashes (an maybe the octopuses next to this)
        if(map[i][j] > 9) {
            map[i][j] = 0;
            flashes++;

            // spread the flash to the adjacent octopuses
            for(int a = i-1; a <= i+1; a++) {
                for(int b = j-1; b <= j+1; b++) {
                    // avoid the limits of the map
                    if(a < 0 || a >= mapSize || b < 0 || b >= mapSize )
                        continue;
                    
                    increaseEnergy(a, b);
                }
            }
    
        }
    }

    protected boolean isFlashSync() {
        for(int i = 0; i < mapSize; i++) {
            for(int j = 0; j < mapSize; j++) {
                if(map[i][j] > 0)
                    return false;
            }
        }
        return true;        
    }

    protected void printMap() {
        StringBuilder line = new StringBuilder("");
        for(int i = 0; i < mapSize; i++) {
            for(int j = 0; j < mapSize; j++) {
                line.append(map[i][j]);
            }
            String lineStr = line.toString();
            logger.info("%s", lineStr);
        }
    }

}

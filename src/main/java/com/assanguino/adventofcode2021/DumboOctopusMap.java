package com.assanguino.adventofcode2021;

public class DumboOctopusMap {
    
    protected int[][] map;
    protected int mapSize = -1;
    protected int noRows = -1;
    protected long flashes = -1;
    protected long stepNumber = -1;

    public DumboOctopusMap() {
    }

    public void processRow(String row) {

        if(mapSize == -1) {
            // not processed
            mapSize = row.length();
            map = new int[mapSize][mapSize];
            noRows = 0;
            flashes = 0;
            stepNumber = 0;
        }

        for(int i = 0; i < mapSize; i++) {
            map[noRows][i] = row.charAt(i) - '0';
        }

        noRows++;
    }

    public void nextStep() {
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

    public boolean isFlashSync() {
        for(int i = 0; i < mapSize; i++) {
            for(int j = 0; j < mapSize; j++) {
                if(map[i][j] > 0)
                    return false;
            }
        }
        return true;        
    }

    public void printMap() {
        for(int i = 0; i < mapSize; i++) {
            for(int j = 0; j < mapSize; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

    public long getFlashes() {
        return flashes;
    }

    public long getStepNumber() {
        return stepNumber;
    }

}

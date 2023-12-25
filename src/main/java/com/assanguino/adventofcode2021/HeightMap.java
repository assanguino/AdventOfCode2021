package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Level;

public class HeightMap implements Executable {
    
    protected List<String> heightRows = new ArrayList<>();
    protected Integer[][] heightMatrix;
    protected Part part;

    /**
     * Check map, with several codes:
     * -1 : not used (just initialized)
     *  0 : it's a low point (after calculateBasins() )
     * >0 : it's part of the basin (after calculateBasins() )
     * -2 : scanned looking for the largest basins
     */
    protected Integer[][] lowPointsMap;

    protected Integer noRows = -1;
    protected Integer noColumns = -1;
    protected Integer riskLevelSum = -1;                // not calculated
    protected Long largestBasinsResult = (long)-1;      // not calculated

    public HeightMap(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        heightRows.add(row);
    }

    public void execute() {

        initHeightmap();
        riskLevelSum = 0;

        long[] largestBasinsSize = {0, 0, 0};

        for(int i = 0; i < noRows; i++) {
            for(int j = 0; j < noColumns; j++) {

                logger.info("calculateBasins over [%d][%d]", i, j);

                int mapPoint = heightMatrix[i][j];

                calculateLowPoints(i, j, noRows, noColumns);

                if(lowPointsMap[i][j] == 0) {
                    riskLevelSum += mapPoint + 1;
                    
                    logger.info("Found minimum height. Coordinates [%2d][%2d], height [%2d], risk level [%2d]", i, j, mapPoint, riskLevelSum);

                    if(part == Part.SECOND) {
                        calculateSecondPartBasinSize(i, j, largestBasinsSize);
                    }
                }
            }
        }

        if(part == Part.SECOND) {
            largestBasinsResult = largestBasinsSize[0] * largestBasinsSize[1] * largestBasinsSize[2];
        }

    }

    private void calculateLowPoints(int i, int j, int noRows, int noColumns) {

        int mapPoint = heightMatrix[i][j];

        lowPointsMap[i][j] = 0;
        if(j+1 < noColumns && heightMatrix[i][j+1] <= mapPoint) {
            lowPointsMap[i][j]++;
        }
        if(j-1 >= 0 && heightMatrix[i][j-1] <= mapPoint) {
            lowPointsMap[i][j]++;
        }
        if(i-1 >= 0 && heightMatrix[i-1][j] <= mapPoint) {
            lowPointsMap[i][j]++;
        }
        if(i+1 < noRows && heightMatrix[i+1][j] <= mapPoint) {
            lowPointsMap[i][j]++;
        }
    }

    public String printDescription() {
        return (part == Part.FIRST) ? 
            "Smoke Basin - Sum of the risk levels of all low points" : 
            "Smoke Basin - Multiplying the sizes of the three largest basins";
    }

    public void printResult() {
        logger.info("number of measurements: %2d rows - %2d columns", noRows, noColumns);

        if(part == Part.FIRST) {
            logger.info("Sum of all risk levels: %d", riskLevelSum);
        } else {
            logger.info("Result of multiplying the three largest basins: %ld", largestBasinsResult);
        }
    }

    public String getResult() {
        return part == Part.FIRST ? 
            String.valueOf(riskLevelSum) :
            String.valueOf(largestBasinsResult);
    }
    
    protected void initHeightmap() {

        noRows = heightRows.size();
        noColumns = heightRows.get(0).length();

        heightMatrix = new Integer[noRows][noColumns];
        lowPointsMap = new Integer[noRows][noColumns];

        for(int i = 0; i < noRows; i++) {
            char[] temp = heightRows.get(i).toCharArray();
            for(int j = 0; j < noColumns; j++) {
                // from char to int
                heightMatrix[i][j] = temp[j] - '0';
                // Init 'low points' map
                lowPointsMap[i][j] = -1;
            }    
        }
    }

    // Recursive method.
    // To know if the point has been scanned, use 'lowPointsMap'
    protected long calculateBasinSize(int i, int j, int lastHeight) {

        long noLocations = 0;
        int currentHeight = heightMatrix[i][j];

        // Just scanned, so stop
        if(lowPointsMap[i][j] == -2)
            return 0;

        // It's not part of any basin
        if(currentHeight == 9)
            return 0;

        // if first point
        if(currentHeight >= lastHeight) {

            // the point is already scanned
            noLocations++;
            lowPointsMap[i][j] = -2;

            logger.printf(Level.DEBUG, "      ... calculateBasinSize of [%2d][%2d] height [%2d] - noLocations [%2d]", i, j, currentHeight, noLocations);

            if(i-1 >= 0) 
                noLocations += calculateBasinSize(i-1, j, currentHeight);
            
            if(i+1 < noRows)
                noLocations += calculateBasinSize(i+1, j, currentHeight);

            if(j-1 >= 0) 
                noLocations += calculateBasinSize(i, j-1, currentHeight);

            if(j+1 < noColumns)
                noLocations += calculateBasinSize(i, j+1, currentHeight);
        }

        return noLocations;
    }

    private void calculateSecondPartBasinSize(int i, int j, long[] largestBasinsSize) {
        long thisBasinSize = calculateBasinSize(i, j, heightMatrix[i][j]);

        if(thisBasinSize > largestBasinsSize[0])
            largestBasinsSize[0] = thisBasinSize;

        Arrays.sort(largestBasinsSize);

        logger.info("Calculated basin size of [%2d]. So the 3 largest are [%2d][%2d][%2d]", 
                thisBasinSize, largestBasinsSize[0], largestBasinsSize[1], largestBasinsSize[2]);
    }

}

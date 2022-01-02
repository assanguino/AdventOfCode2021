package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeightMap {
    
    protected List<String> heightRows = new ArrayList<>();
    protected Integer[][] heightMap;

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

    public void getHeightMapRow(String row) {
        heightRows.add(row);
    }

    public Integer getRiskLevelSum() {

        if(riskLevelSum == -1)
            calculateBasins(Part.first);
        
        return riskLevelSum;
    }

    public long getLargestBasinsResult() {
        if(largestBasinsResult == -1)
            calculateBasins(Part.second);

        return largestBasinsResult;
    }

    protected void initHeightmap() {

        noRows = heightRows.size();
        noColumns = heightRows.get(0).length();

        heightMap = new Integer[noRows][noColumns];
        lowPointsMap = new Integer[noRows][noColumns];

        for(int i = 0; i < noRows; i++) {
            char[] temp = heightRows.get(i).toCharArray();
            for(int j = 0; j < noColumns; j++) {
                // from char to int
                heightMap[i][j] = temp[j] - '0';
                // Init 'low points' map
                lowPointsMap[i][j] = -1;
            }    
        }
    }

    protected void calculateBasins(Part part) {

        initHeightmap();
        riskLevelSum = 0;

        long[] largestBasinsSize = {0, 0, 0};

        for(int i = 0; i < noRows; i++) {
            for(int j = 0; j < noColumns; j++) {

                // System.out.println(String.format("calculateBasins over [%d][%d]", i, j));

                int mapPoint = heightMap[i][j];
                lowPointsMap[i][j] = 0;
                if(j+1 < noColumns && heightMap[i][j+1] <= mapPoint) {
                    lowPointsMap[i][j]++;
                }
                if(j-1 >= 0 && heightMap[i][j-1] <= mapPoint) {
                    lowPointsMap[i][j]++;
                }
                if(i-1 >= 0 && heightMap[i-1][j] <= mapPoint) {
                    lowPointsMap[i][j]++;
                }
                if(i+1 < noRows && heightMap[i+1][j] <= mapPoint) {
                    lowPointsMap[i][j]++;
                }

                if(lowPointsMap[i][j] == 0) {
                    riskLevelSum += heightMap[i][j] + 1;
                    
                    // System.out.println(String.format("Found minimum height. Coordinates [%2d][%2d], height [%2d], risk level [%2d]", i, j, heightMap[i][j], riskLevelSum));

                    if(part == Part.second) {
                        long thisBasinSize = calculateBasinSize(i, j, mapPoint);

                        if(thisBasinSize > largestBasinsSize[0])
                            largestBasinsSize[0] = thisBasinSize;

                        Arrays.sort(largestBasinsSize);

                        /*
                        System.out.println(String.format("Calculated basin size of [%2d]. So the 3 largest are [%2d][%2d][%2d]", 
                                thisBasinSize, largestBasinsSize[0], largestBasinsSize[1], largestBasinsSize[2]));
                        */
                    }
                }
            }
        }

        if(part == Part.second) {
            largestBasinsResult = largestBasinsSize[0] * largestBasinsSize[1] * largestBasinsSize[2];
        }

    }

    // Recursive method.
    // To know if the point has been scanned, use 'lowPointsMap'
    protected long calculateBasinSize(int i, int j, int lastHeight) {

        long noLocations = 0;
        int currentHeight = heightMap[i][j];

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

            // System.out.println(String.format("      ... calculateBasinSize of [%2d][%2d] height [%2d] - noLocations [%2d]", i, j, currentHeight, noLocations));

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

}

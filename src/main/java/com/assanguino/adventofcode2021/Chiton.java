package com.assanguino.adventofcode2021;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Level;

public class Chiton implements Executable {

    protected Part part;
    protected int noRows = 0;
    protected int rowLength = 0;
    protected int[][] riskLevelMap;
    int[] tentativeDistance;

    public Chiton(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        if(rowLength == 0) {
            rowLength = row.length();
            riskLevelMap = new int[rowLength][rowLength];
        }

        for(int i = 0; i < rowLength; i++) {
            riskLevelMap[noRows][i] = row.charAt(i) - '0';
        }

        noRows++;
    }

    public void execute() {
        if(part == Part.FIRST) {
            dijkstra(riskLevelMap, 0);
        } else if(part == Part.SECOND) {
            int factor = 5;
            int[][] largerRiskLevelMap = createLargerRiskLevelMap(riskLevelMap, factor);
            dijkstra(largerRiskLevelMap, 0);
            rowLength *= factor;
        }
    }

    public String printDescription() {
        return "Chiton - What is the lowest total risk of any path from the top left to the bottom right ?";
    }

    public void printResult() {
        int lastIndex = rowLength * rowLength - 1;

        logger.info("");
        logger.info("The distance from node %d to node %d is: %d", 0, lastIndex, tentativeDistance[lastIndex]);
    }

    public String getResult() {
        return String.valueOf(tentativeDistance[rowLength*rowLength - 1]);
    }

    /**
     * Calculate the distance, based on the risk_level_map:
     * - 0 means that it's the node itself (no distance)
     * - -1 means that there is no link between the nodes (infinity)
     * - x means the real distance between nodes is x
     * @param i first node index
     * @param j second node index
     * @return distance
     */
    protected int calcDistance(int[][] matrix, int i, int j) {

        int finalMatrixLength = matrix.length;

        int iX = i / finalMatrixLength;
        int iY = i % finalMatrixLength;
        int jX = j / finalMatrixLength;
        int jY = j % finalMatrixLength;

        // The node itself (no distance)
        if(i == j)
            return 0;

        // distance of the node on the right
        if(iX == jX && jY == iY + 1) {
            return matrix[jX][jY];
        }

        // distance of the node on the left
        if(iX == jX && jY == iY - 1) {
            return matrix[jX][jY];
        }

        // distance of the node above
        if(iX == jX + 1 && jY == iY) {
            return matrix[jX][jY];
        }

        // distance of the node below
        if(iX == jX - 1 && jY == iY) {
            return matrix[jX][jY];
        }

        // else, the distance is bigger than 1, or diagonal, so infinite
        return -1;
    }

    /**
     * Dijkstra algorithm implementation
     * @param input matrix
     * @param initNode
     */
    protected void dijkstra(int[][] input, int initNode) {

        // init variables
        int noNodes = input.length * input.length;
        tentativeDistance = new int[noNodes];
        Set<Integer> unvisitedNodes = new HashSet<>();
        for(int i = 0; i < noNodes; i++) {
            tentativeDistance[i] = (i == initNode) ? 0 : Integer.MAX_VALUE;
            unvisitedNodes.add(i);
        }
        int currentNode = initNode;

        // loop over the unvisited nodes
        while(!unvisitedNodes.isEmpty()) {

            int min = Integer.MAX_VALUE;
            int minIndex = currentNode;

            for(int i = 0; i < noNodes; i++) {
                if(tentativeDistance[i] < min && unvisitedNodes.contains(i)) {
                    min = tentativeDistance[i];
                    minIndex = i;
                }
            }
    
            unvisitedNodes.remove(minIndex);

            logger.printf(Level.DEBUG, "Current node [%3d] tentative_distance [%3d] unvisited_nodes size [%3d]", 
                currentNode, tentativeDistance[noNodes - 1], unvisitedNodes.size());

            // recalculate the tentative distance
            recalculateTentativeDistance(noNodes, input, minIndex, unvisitedNodes);
        }

        // print the solution
        printTentativeDistances();
    }

    protected void recalculateTentativeDistance(int noNodes, int[][] input, int minIndex, Set<Integer> unvisitedNodes) {
        for(int j = 0; j < noNodes; j++) {
            int distance = calcDistance(input, minIndex, j);
            if(distance > 0 && unvisitedNodes.contains(j) && tentativeDistance[j] > tentativeDistance[minIndex] + distance) {
                tentativeDistance[j] = tentativeDistance[minIndex] + distance;
            }
        }
    }

    protected int[][] createLargerRiskLevelMap(int[][] riskLevelMap, int factor) {

        int size = riskLevelMap.length;
        int newSize = factor * size;
        int[][] result = new int[newSize][newSize];

        // We're assuming there are square maps
        for(int i = 0; i < newSize; i++) {
            for(int j = 0; j < newSize; j++) {
                int iLow = i % size;
                int jLow = j % size;
                int offset = i/size + j/size;
                result[i][j] = riskLevelMap[iLow][jLow] + offset;
                if(result[i][j] > 9) result[i][j] -= 9;
            }
        }
        
        return result;
    }

    protected void printMap(int [][] map) {

        for(int i = 0; i < map.length; i++) {
            String line = "";
            for(int j = 0; j < map.length; j++) {
                line = String.format("%s%1d", line, map[i][j]);
            }
            logger.error(line);
        }
    }

    protected void printTentativeDistances() {
        for(int i = 0; i < tentativeDistance.length; i++) {
            logger.info("node %d distance %d", i, tentativeDistance[i]);
        }
    }

}


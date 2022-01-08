package com.assanguino.adventofcode2021;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.Level;

public class Chiton implements Executable {

    protected Part part;
    protected int noRows = 0;
    protected int row_length = 0;
    protected int[][] risk_level_map;
    int[] tentative_distance;

    public Chiton(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        if(row_length == 0) {
            row_length = row.length();
            risk_level_map = new int[row_length][row_length];
        }

        for(int i = 0; i < row_length; i++) {
            risk_level_map[noRows][i] = row.charAt(i) - '0';
        }

        noRows++;
    }

    public void execute() {
        if(part == Part.first) {
            dijkstra(risk_level_map, 0);
        } else if(part == Part.second) {
            int factor = 5;
            int[][] larger_risk_level_map = createLargerRiskLevelMap(risk_level_map, factor);
            dijkstra(larger_risk_level_map, 0);
            row_length *= factor;
        }
    }

    public String printDescription() {
        return "Chiton - What is the lowest total risk of any path from the top left to the bottom right ?";
    }

    public void printResult() {
        int lastIndex = row_length * row_length - 1;
        System.out.println();
        System.out.println(String.format("The distance from node %d to node %d is: %d", 0, lastIndex, tentative_distance[lastIndex]));
    }

    public String getResult() {
        return String.valueOf(tentative_distance[row_length*row_length - 1]);
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

        int final_matrix_length = matrix.length;

        int i_X = i / final_matrix_length;
        int i_Y = i % final_matrix_length;
        int j_X = j / final_matrix_length;
        int j_Y = j % final_matrix_length;

        // The node itself (no distance)
        if(i == j)
            return 0;

        // distance of the node on the right
        if(i_X == j_X && j_Y == i_Y + 1) {
            return matrix[j_X][j_Y];
        }

        // distance of the node on the left
        if(i_X == j_X && j_Y == i_Y - 1) {
            return matrix[j_X][j_Y];
        }

        // distance of the node above
        if(i_X == j_X + 1 && j_Y == i_Y) {
            return matrix[j_X][j_Y];
        }

        // distance of the node below
        if(i_X == j_X - 1 && j_Y == i_Y) {
            return matrix[j_X][j_Y];
        }

        // else, the distance is bigger than 1, or diagonal, so infinite
        return -1;
    }

    /**
     * Dijkstra algorithm implementation
     * @param input matrix
     * @param init_node
     */
    protected void dijkstra(int[][] input, int init_node) {

        // init variables
        int no_nodes = input.length * input.length;
        tentative_distance = new int[no_nodes];
        Set<Integer> unvisited_nodes = new HashSet<Integer>();
        for(int i = 0; i < no_nodes; i++) {
            tentative_distance[i] = (i == init_node) ? 0 : Integer.MAX_VALUE;
            unvisited_nodes.add(i);
        }
        int current_node = init_node;

        // loop over the unvisited nodes
        while(unvisited_nodes.size() > 0) {

            int min = Integer.MAX_VALUE;
            int min_index = current_node;

            for(int i = 0; i < no_nodes; i++) {
                if(tentative_distance[i] < min && unvisited_nodes.contains(i)) {
                    min = tentative_distance[i];
                    min_index = i;
                }
            }
    
            unvisited_nodes.remove(min_index);

            logger.printf(Level.DEBUG, "Current node [%3d] tentative_distance [%3d] unvisited_nodes size [%3d]", 
                current_node, tentative_distance[no_nodes - 1], unvisited_nodes.size());

            // recalculate the tentative distance
            for(int j = 0; j < no_nodes; j++) {
                int distance = calcDistance(input, min_index, j);
                if(distance > 0 && unvisited_nodes.contains(j) && tentative_distance[j] > tentative_distance[min_index] + distance) {
                    tentative_distance[j] = tentative_distance[min_index] + distance;
                }
            }

        }

        // print the solution
        printTentativeDistances();
    }

    protected int[][] createLargerRiskLevelMap(int[][] risk_level_map, int factor) {

        int size = risk_level_map.length;
        int new_size = factor * size;
        int[][] result = new int[new_size][new_size];

        // We're assuming there are square maps
        for(int i = 0; i < new_size; i++) {
            for(int j = 0; j < new_size; j++) {
                int low_i = i % size;
                int low_j = j % size;
                int offset = i/size + j/size;
                result[i][j] = risk_level_map[low_i][low_j] + offset;
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
            logger.printf(Level.ERROR, line);
        }
    }

    protected void printTentativeDistances() {
        for(int i = 0; i < tentative_distance.length; i++) {
            logger.printf(Level.INFO, "node " + i + " distance " + tentative_distance[i]);
        }
    }

}


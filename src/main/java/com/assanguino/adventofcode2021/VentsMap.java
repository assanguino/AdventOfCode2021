package com.assanguino.adventofcode2021;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

public class VentsMap implements Executable {
    
    // names of coordinates
    protected static final int X1 = 0;
    protected static final int Y1 = 1;
    protected static final int X2 = 2;
    protected static final int Y2 = 3;

    protected Part part;
    protected List<Integer[]> ventList = new ArrayList<>();
    protected Integer[][] map;
    protected int maximum = 0;
    protected int dangerousAreas = -1;

    public VentsMap(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        String[] ventCoordinatesStr = row.split(" -> ");
        String[] ventEnd1 = ventCoordinatesStr[0].split(",");
        String[] ventEnd2 = ventCoordinatesStr[1].split(",");

        Integer[] coordinates = new Integer[4];
        coordinates[X1] = Integer.parseInt(ventEnd1[0]);
        coordinates[Y1] = Integer.parseInt(ventEnd1[1]);
        coordinates[X2] = Integer.parseInt(ventEnd2[0]);
        coordinates[Y2] = Integer.parseInt(ventEnd2[1]);
        ventList.add(coordinates);

        // calculate maximum
        int currentMax = Math.max(Math.max(coordinates[0], coordinates[1]), 
                                  Math.max(coordinates[2], coordinates[3]));
        if(currentMax > maximum)
            maximum = currentMax;
    }

    public void execute() {
        maximum++;
        map = new Integer[maximum][maximum];

        ventList.forEach(this::addVent);
        dangerousAreas = getDangerousAreas();
    }

    public String printDescription() {
        return "Hydrothermal Venture - At how many points do at least two lines overlap ?";
    }

    public void printResult() {
        logger.info("number of vents: %d", ventList.size());
        logger.info("size of the map: %d", maximum);
        logger.info("number of dangerous areas: %d", dangerousAreas);
    }

    public String getResult() {
        return String.valueOf(dangerousAreas);
    }

    protected void addVent(Integer[] c) {

        int minX = Math.min(c[X1], c[X2]);
        int maxX = Math.max(c[X1], c[X2]);
        int minY = Math.min(c[Y1], c[Y2]);
        int maxY = Math.max(c[Y1], c[Y2]);

        if(c[X1].equals(c[X2])) {
            // case of horizontal vent
            horizontalVent(minY, maxY, c[X1]);
        } else if(c[Y1].equals(c[Y2])) {
            // case of vertical vent
            verticalVent(minX, maxX, c[Y1]);
        } else if(part == Part.SECOND) {
            // diagonals (if this is the case)
            boolean isBackslashDirection = isBackslashDirection(c);
            diagonalVent(minX, maxX, minY, isBackslashDirection);
        }
    }

    private boolean isBackslashDirection(Integer[] c) {
        return (c[X1] < c[X2] && c[Y1] < c[Y2]) || (c[X1] > c[X2] && c[Y1] > c[Y2]);
    }

    private void proccessMapValue(int x, int y) {
        if(map[x][y] == null) {
            map[x][y] = 1;
        } else {
            map[x][y]++;
        }
    }

    private void horizontalVent(int min, int max, int constantValue) {
        for(int i = min; i <= max; i++) {
            proccessMapValue(constantValue, i);
        }
    }

    private void verticalVent(int min, int max, int constantValue) {
        for(int i = min; i <= max; i++) {
            proccessMapValue(i, constantValue);
        }
    }

    private void diagonalVent(int minX, int maxX, int yValue, boolean isBackslashDirection) {
        int length = maxX - minX;
        for(int i = 0; i <= length; i++) {
            int xValue = isBackslashDirection ? minX + i : maxX - i;
            proccessMapValue(xValue, yValue + i);
        }
    }

    protected int getDangerousAreas() {
        int areas = 0;
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map.length; j++) {
                if(map[i][j] != null && map[i][j] >= 2)
                    areas++;
            }
        }

        return areas;
    }

    protected void printMap() {
        for(int i = 0; i < map.length; i++) {
            StringBuilder line = new StringBuilder();
            for(int j = 0; j < map.length; j++) {
                String c = ".";
                if(map[j][i] != null)
                    c = (map[j][i]).toString();
                line.append(c);
            }
            logger.printf(Level.DEBUG, line.toString());
        }
    }

}

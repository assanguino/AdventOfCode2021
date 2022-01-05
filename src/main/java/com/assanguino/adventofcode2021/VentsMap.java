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
    protected List<Integer[]> ventList = new ArrayList<Integer[]>();
    protected Integer[][] map;
    protected int maximum = 0;
    protected int dangerousAreas = -1;

    public VentsMap(Part part) {
        this.part = part;
    }

    public void processRow(String row) {
        String[] vent_coordinates_str = row.split(" -> ");
        String[] vent_end_1 = vent_coordinates_str[0].split(",");
        String[] vent_end_2 = vent_coordinates_str[1].split(",");

        Integer[] coordinates = new Integer[4];
        coordinates[X1] = Integer.parseInt(vent_end_1[0]);
        coordinates[Y1] = Integer.parseInt(vent_end_1[1]);
        coordinates[X2] = Integer.parseInt(vent_end_2[0]);
        coordinates[Y2] = Integer.parseInt(vent_end_2[1]);
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

        ventList.forEach(c -> addVent(c));
        dangerousAreas = getDangerousAreas();
    }

    public String printDescription() {
        return "Hydrothermal Venture - At how many points do at least two lines overlap ?";
    }

    public void printResult() {
        System.out.println("number of vents: " + ventList.size());
        System.out.println("size of the map: " + maximum);
        System.out.println("number of dangerous areas: " + dangerousAreas);
    }

    public String getResult() {
        return String.valueOf(dangerousAreas);
    }

    protected void addVent(Integer[] c) {
        if(c[X1].equals(c[X2])) {

            // case of horizontal vent
            int min = Math.min(c[Y1], c[Y2]);
            int max = Math.max(c[Y1], c[Y2]);
            for(int i = min; i <= max; i++) {
                if(map[c[X1]][i] == null) {
                    map[c[X1]][i] = 1;
                } else {
                    map[c[X1]][i]++;                    
                }
            }
        } else if(c[Y1].equals(c[Y2])) {

            // case of vertical vent
            int min = Math.min(c[X1], c[X2]);
            int max = Math.max(c[X1], c[X2]);
            for(int j = min; j <= max; j++) {
                if(map[j][c[Y1]] == null) {
                    map[j][c[Y1]] = 1;
                } else {
                    map[j][c[Y1]]++;
                }

            }
        } else {

            // diagonals (if this is the case)
            if(part == Part.second) {

                if((c[X1] < c[X2] && c[Y1] < c[Y2]) || (c[X1] > c[X2] && c[Y1] > c[Y2])) {
                    // \\ this way diagonal 
                    int minX = Math.min(c[X1], c[X2]);
                    int maxX = Math.max(c[X1], c[X2]);
                    int minY = Math.min(c[Y1], c[Y2]);
                    int length = maxX - minX;

                    for(int i = 0; i <= length; i++) {
                        if(map[minX + i][minY + i] == null) {
                            map[minX + i][minY + i] = 1;
                        } else {
                            map[minX + i][minY + i]++;                    
                        }
                    }    
                } else {
                    // // this way diagonal 
                    int minX = Math.min(c[X1], c[X2]);
                    int maxX = Math.max(c[X1], c[X2]);
                    int minY = Math.min(c[Y1], c[Y2]);
                    int length = maxX - minX;
    
                    for(int i = 0; i <= length; i++) {
                        if(map[maxX - i][minY + i] == null) {
                            map[maxX - i][minY + i] = 1;
                        } else {
                            map[maxX - i][minY + i]++;                    
                        }
                    }
                }
            }

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
            String line = "";
            for(int j = 0; j < map.length; j++) {
                String c = ".";
                if(map[j][i] != null)
                    c = (map[j][i]).toString();
                line = line + c;
            }
            logger.log(Level.DEBUG, line);
        }
    }

}

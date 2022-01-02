package com.assanguino.adventofcode2021;

import com.assanguino.adventofcode2021.App.Part;

public class VentsMap {
    
    // names of coordinates
    protected static final int X1 = 0;
    protected static final int Y1 = 1;
    protected static final int X2 = 2;
    protected static final int Y2 = 3;

    protected Integer[][] map;

    public VentsMap(Integer dim) {
        map = new Integer[dim][dim];
    }

    public static Integer[] getCoordinatesFromString(String string) {
        String[] vent_coordinates_str = string.split(" -> ");
        String[] vent_end_1 = vent_coordinates_str[0].split(",");
        String[] vent_end_2 = vent_coordinates_str[1].split(",");
        Integer[] vent_coordinates = new Integer[4];
        vent_coordinates[X1] = Integer.parseInt(vent_end_1[0]);
        vent_coordinates[Y1] = Integer.parseInt(vent_end_1[1]);
        vent_coordinates[X2] = Integer.parseInt(vent_end_2[0]);
        vent_coordinates[Y2] = Integer.parseInt(vent_end_2[1]);

        return vent_coordinates;
    }

    public void addVent(Integer[] c, Part part) {
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

    public int getDangerousAreas() {
        int areas = 0;
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map.length; j++) {
                if(map[i][j] != null && map[i][j] >= 2)
                    areas++;
            }
        }

        return areas;
    }

    public void printMap() {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map.length; j++) {
                String c = ".";
                if(map[j][i] != null)
                    c = (map[j][i]).toString();
                System.out.print(c);
            }
            System.out.println();
        }
    }

}
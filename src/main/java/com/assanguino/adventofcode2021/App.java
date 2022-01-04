package com.assanguino.adventofcode2021;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;

public class App {

    @Deprecated
    protected static Map<Pair<Integer, Part>, String> methodMap = new HashMap<>( );

    protected static Map<Integer, Class<? extends Executable>> classMap = new HashMap<>();

    public static void main(String[] args) throws Exception {

        // Populate the methods map
        methodMap.put(new Pair<Integer, Part>( 1, Part.first),  "depth_measurement_increments");
        methodMap.put(new Pair<Integer, Part>( 1, Part.second), "three_measurements_sliding_window");
        // methodMap.put(new Pair<Integer, Part>( 2, Part.first),  "horizontal_and_depth");
        // methodMap.put(new Pair<Integer, Part>( 2, Part.second), "horizontal_and_depth_with_aim");
        // methodMap.put(new Pair<Integer, Part>( 3, Part.first),  "binary_diagnostic_power_consumption");
        // methodMap.put(new Pair<Integer, Part>( 3, Part.second), "life_support_rating");
        // methodMap.put(new Pair<Integer, Part>( 4, Part.first),  "playing_bingo_with_giant_squid_to_win");
        // methodMap.put(new Pair<Integer, Part>( 4, Part.second), "playing_bingo_with_giant_squid_to_lose");
        // methodMap.put(new Pair<Integer, Part>( 5, Part.first),  "hydrothermal_venture_first");
        // methodMap.put(new Pair<Integer, Part>( 5, Part.second), "hydrothermal_venture_second");
        // methodMap.put(new Pair<Integer, Part>( 6, Part.first),  "lanternfish_part_1");
        // methodMap.put(new Pair<Integer, Part>( 6, Part.second), "lanternfish_part_2");
        // methodMap.put(new Pair<Integer, Part>( 7, Part.first),  "crabs_horizontal_positioning_part_first");
        // methodMap.put(new Pair<Integer, Part>( 7, Part.second), "crabs_horizontal_positioning_part_second");
        // methodMap.put(new Pair<Integer, Part>( 8, Part.first),  "seven_segment_search");
        // methodMap.put(new Pair<Integer, Part>( 8, Part.second), "seven_segment_search_solved");
        // methodMap.put(new Pair<Integer, Part>( 9, Part.first),  "smoke_basin");
        // methodMap.put(new Pair<Integer, Part>( 9, Part.second), "find_largest__basins");
        // methodMap.put(new Pair<Integer, Part>(10, Part.first),  "syntax_scoring_corrupted_chunks");
        // methodMap.put(new Pair<Integer, Part>(10, Part.second), "syntax_scoring_incomplete_chunks");
        // methodMap.put(new Pair<Integer, Part>(11, Part.first),  "dumbo_octopus");
        // methodMap.put(new Pair<Integer, Part>(11, Part.second), "dumbo_octopus_sync");
        // methodMap.put(new Pair<Integer, Part>(12, Part.first),  "caves_path_first");
        // methodMap.put(new Pair<Integer, Part>(12, Part.second), "caves_path_second");
        methodMap.put(new Pair<Integer, Part>(13, Part.first),  "transparent_origami_first");
        methodMap.put(new Pair<Integer, Part>(13, Part.second), "transparent_origami_second");

        // execute(12, Part.second);

        classMap.put( 2, Diving.class);
        classMap.put( 3, BinaryDiagnostic.class);
        classMap.put( 4, GiantSquidBingo.class);
        classMap.put( 5, VentsMap.class);
        classMap.put( 6, Lanternfish.class);
        classMap.put( 7, Crabs.class);
        classMap.put( 8, SevenSegmentDisplay.class);
        classMap.put( 9, HeightMap.class);
        classMap.put(10, ChunkReader.class);
        classMap.put(11, DumboOctopus.class);
        classMap.put(12, CavesPath.class);

        execute_new_version( 2, Part.second);
    }

    @Deprecated
    protected static Path getFilePath(String fileName) throws Exception {
        URL resource = App.class.getClassLoader().getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        }

        return Path.of(resource.getPath());
    }
    
    @Deprecated
    protected static void execute(int day, Part part) throws Exception {

        try {
            var key = new Pair<Integer, Part>(day, part);
            String methodName = methodMap.get(key);
            var method = App.class.getDeclaredMethod(methodName);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("************************************************ Advent of Code 2021");
            System.out.println("************************************************ day " + day + ", " + part.toString() + " part");
            System.out.println("************************************************ " + methodName);
            method.invoke(null);
            System.out.println("************************************************");
            System.out.println();
            System.out.println();
            System.out.println();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static void execute_new_version(int day, Part part) {
        try {
            Class<?>[] constructorParams = { Part.class };

            Class<? extends Executable> dayClass = classMap.get(day);
            Executable dayObject = dayClass.getConstructor(constructorParams).newInstance(part);

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("************************************************ Advent of Code 2021");
            System.out.println("************************************************ Day " + day + ", " + part.toString() + " part");
            System.out.println("************************************************ " + dayObject.printDescription() );

            dayObject.processInput(Executable.getInputFile(day));
            dayObject.execute();
            dayObject.printResult();

            System.out.println("************************************************");
            System.out.println();
            System.out.println();
            System.out.println();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static void depth_measurement_increments() throws Exception {

        Integer current, last = -1;
        Integer measurements = 0;
        Integer increments = 0;

        // read file
        for (String string : Files.readAllLines(getFilePath("AoC_01_input.txt"))) {

            if(measurements == 0) {
                if(string == null)
                throw new Exception("Not enough measurements to compare with.");

                last = Integer.parseInt(string);
                measurements++;
            }
            else
            {
                current = Integer.parseInt(string);
                measurements++;

                if(current > last)
                    increments++;

                last = current;
            }

        }

        // Output
        System.out.println("measurements: " + measurements);
        System.out.println("increments: " + increments);
    }

    protected static void three_measurements_sliding_window() throws Exception {

        var measures = new ArrayList<Integer>();
        int index = 0;

        for (String string : Files.readAllLines(getFilePath("AoC_01_input.txt"))) {
            int current = Integer.parseInt(string);

            // three-measurement sliding window
            measures.add(index, current);
            if (index - 1 >= 0)
                measures.set(index - 1, measures.get(index - 1) + current);
            if (index - 2 >= 0)
                measures.set(index - 2, measures.get(index - 2) + current);

            index++;
        }

        // because the last two measurements can't fill a three-measurements
        // sliding-window
        Integer increments = 0;
        for (int i = 1; i < index - 2; i++) {
            if (measures.get(i) > measures.get(i - 1))
                increments++;
        }

        // Output
        System.out.println("measurements: " + index);
        System.out.println("increments: " + increments);
    }

    // TODO remove
    /*
    protected static void day_02_basic_method(Part part) throws Exception {

        int measurements = 0;
        int horizontal = 0;
        int depth = 0;
        int aim = 0;

        for (String string : Files.readAllLines(getFilePath("AoC_02_input.txt"))) {

            String[] chains = string.split(" ");
            if(chains.length != 2)
                continue;

            measurements++;

            int value = Integer.parseInt(chains[1]);

            if(part == Part.first) {
                if(chains[0].equals("forward")) {
                    horizontal += value;
                } else if(chains[0].equals("up")) {
                    depth -= value;
                } else if(chains[0].equals("down")) {
                    depth += value;
                }
            }
            else
            {
                if(chains[0].equals("forward")) {
                    horizontal += value;
                    depth += aim*value;
                } else if(chains[0].equals("up")) {
                    aim -= value;
                } else if(chains[0].equals("down")) {
                    aim += value;
                }
            }
        }

        // Output
        System.out.println("measurements: " + measurements);
        System.out.println("horizontal position: " + horizontal);
        System.out.println("depth position: " + depth);
        System.out.println("final value (multiplication): " + horizontal*depth);
    }

    protected static void horizontal_and_depth() throws Exception {
        day_02_basic_method(Part.first);
    }

    protected static void horizontal_and_depth_with_aim() throws Exception {
        day_02_basic_method(Part.second);
    }
    */

    // TODO remove
    /*
    protected static void binary_diagnostic_power_consumption() throws Exception {

        int measurements = 0;
        int binaryLength = 0;
        ArrayList<Integer> zeros = new ArrayList<>();
        ArrayList<Integer> ones = new ArrayList<>();

        for (String string : Files.readAllLines(getFilePath("AoC_03_input.txt"))) {

            // initialize arrays if this is first iteration
            if(binaryLength == 0)
            {
                binaryLength = string.length();
                zeros.addAll(Collections.nCopies(binaryLength, 0));
                ones.addAll(Collections.nCopies(binaryLength, 0));
            }

            for(int i = 0; i < binaryLength; i++) {
                if(string.charAt(i) == '0') {
                    zeros.set(i, zeros.get(i)+1);
                } else if(string.charAt(i) == '1') {
                    ones.set(i, ones.get(i)+1);
                }
            }

            measurements++;
        }

        String gamma_rate_binary = "", epsilon_rate_binary = "";
        
        for(int i = 0; i < binaryLength; i++) {
            if(zeros.get(i) > ones.get(i)) {
                gamma_rate_binary = gamma_rate_binary + "0";
                epsilon_rate_binary = epsilon_rate_binary + "1";
            } else {
                gamma_rate_binary = gamma_rate_binary + "1";
                epsilon_rate_binary = epsilon_rate_binary + "0";
            }
        }

        int gamma_rate = Integer.parseInt(gamma_rate_binary, 2);
        int epsilon_rate = Integer.parseInt(epsilon_rate_binary, 2);       

        // Output
        System.out.println("measurements: " + measurements);
        System.out.println("gamma_rate_binary:   " + gamma_rate_binary +  " - gamma_rate: " + gamma_rate);
        System.out.println("epsilon_rate_binary: " + epsilon_rate_binary +  " - epsilon_rate: " + epsilon_rate);
        System.out.println("final value (multiplication): " + gamma_rate*epsilon_rate);    
    }

    protected static void life_support_rating() throws Exception {

        int measurements = 0;
        int binaryLength = 0;
        ArrayList<String> oxygen_generator_list = new ArrayList<>();
        ArrayList<String> CO2_scrubber_list = new ArrayList<>();

        // Get the '0' and '1' counters for each position
        for (String string : Files.readAllLines(getFilePath("AoC_03_input.txt"))) {

            // initialize arrays if this is first iteration
            if(binaryLength == 0)
            {
                binaryLength = string.length();
            }

            oxygen_generator_list.add(string);
            CO2_scrubber_list.add(string);

            measurements++;
        }

        int index = 0;
        do {

            int noZeros = 0, noOnes = 0;
            for(String str : oxygen_generator_list) {
                if(str.charAt(index) == '0') {
                    noZeros++;
                } else {
                    noOnes++;
                }
            }

            final int finalIndex = index;
            if(noOnes >= noZeros) {
                oxygen_generator_list.removeIf(str -> str.charAt(finalIndex) == '0');
            } else {
                oxygen_generator_list.removeIf(str -> str.charAt(finalIndex) == '1');
            }

            // Iteration
            System.out.println("oxygen_generator_list - Iteration #" + index + " list size " + oxygen_generator_list.size() + " elements.");

            index++;

        } while(oxygen_generator_list.size() > 1 && index < binaryLength);

        // Middle report
        System.out.println("oxygen_generator_list size: " + oxygen_generator_list.size());
        System.out.println("oxygen_generator_list first value: " + (oxygen_generator_list.size() > 0 ? oxygen_generator_list.get(0) : "[No value]"));
        System.out.println("index reached: " + index + " / " + binaryLength + " bits.");

        index = 0;
        do {

            int noZeros = 0, noOnes = 0;
            for(String str : CO2_scrubber_list) {
                if(str.charAt(index) == '0') {
                    noZeros++;
                } else {
                    noOnes++;
                }
            }

            final int finalIndex = index;
            if(noOnes >= noZeros) {
                CO2_scrubber_list.removeIf(str -> str.charAt(finalIndex) == '1');
            } else {
                CO2_scrubber_list.removeIf(str -> str.charAt(finalIndex) == '0');
            }

            // Iteration
            System.out.println("CO2_scrubber_list - Iteration #" + index + " list size " + CO2_scrubber_list.size() + " elements.");

            index++;

        // } while(index < binaryLength);
        } while(CO2_scrubber_list.size() > 1 && index < binaryLength);

        // Middle report
        System.out.println("CO2_scrubber_list size: " + CO2_scrubber_list.size());
        System.out.println("CO2_scrubber_list first value: " + (CO2_scrubber_list.size() > 0 ? CO2_scrubber_list.get(0) : "[No value]"));
        System.out.println("index reached: " + index + " / " + binaryLength + " bits.");

        int oxygen_generator_rating = Integer.parseInt(oxygen_generator_list.get(0), 2);
        int CO2_scrubber_rating = Integer.parseInt(CO2_scrubber_list.get(0), 2);

        // Output
        System.out.println();
        System.out.println("measurements: " + measurements);
        System.out.println("oxygen_generator_rating: " + oxygen_generator_rating);
        System.out.println("CO2_scrubber_rating:     " + CO2_scrubber_rating);
        System.out.println("life_support_rating (multiplication): " + oxygen_generator_rating*CO2_scrubber_rating);    
    }
    */

    // TODO remove
    /*
    protected static void playing_bingo_with_giant_squid(Part part) throws Exception {

        List<Integer> random_numbers = new ArrayList<>();
        List<BingoBoard> boardList = new ArrayList<>();
        int board_row_counter = 0;

        // Get the random numbers, and all the bingo boards from the input file
        for (String string : Files.readAllLines(getFilePath("AoC_04_input.txt"))) {

            // Populate the random numbers list
            if(random_numbers.size() == 0) {
                for(String str : string.split(",")) {
                    random_numbers.add(Integer.parseInt(str));
                }
                
            } else if (string.length() == 0) {
                // Reinit counter
                board_row_counter = 0;
            } else {
                // Populate all the bingo boards
                if(board_row_counter == 0) {
                    BingoBoard board = new BingoBoard();
                    boardList.add(board);
                    board.populateRow(board_row_counter, string);
                } else {
                    BingoBoard last = boardList.get(boardList.size()-1);
                    last.populateRow(board_row_counter, string);
                }

                // By the way ...
                if(++board_row_counter == BingoBoard.BOARD_SIZE)
                    board_row_counter = 0;
            }
        }

        // Play!
        if(part == Part.first) {
            for(Integer random : random_numbers) {
                for(BingoBoard board : boardList) {
                    board.mark(random);
                    if(board.checkBingo()) {
                        System.out.println("number of random numbers: " + random_numbers.size());
                        System.out.println("number of bingo boards: " + boardList.size());
                        System.out.println("winning random number: " + random);
                        board.printBoard();
                        System.out.println("final score: " + random*board.getScore());
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
                            System.out.println("number of random numbers: " + random_numbers.size());
                            System.out.println("number of bingo boards: " + boardList.size());
                            System.out.println("loser random number: " + random);
                            board.printBoard();
                            System.out.println("final score: " + random*board.getScore());
                            return;
                        }
                    }
                }
            }
        }
    }

    protected static void playing_bingo_with_giant_squid_to_win() throws Exception {
        playing_bingo_with_giant_squid(Part.first);
    }

    protected static void playing_bingo_with_giant_squid_to_lose() throws Exception {
        playing_bingo_with_giant_squid(Part.second);
    }
    */

    // TODO remove
    /*
    protected static void hydrothermal_venture(Part part) throws Exception {

        int maximum = 0;
        List<Integer[]> ventList = new ArrayList<Integer[]>();
        for (String string : Files.readAllLines(getFilePath("AoC_05_input.txt"))) {
            Integer[] coordinates = VentsMap.getCoordinatesFromString(string);
            ventList.add(coordinates);

            int currentMax = Math.max(Math.max(coordinates[0], coordinates[1]), 
                                      Math.max(coordinates[2], coordinates[3]));
            if(currentMax > maximum)
                maximum = currentMax;
        }

        var ventsMap = new VentsMap(++maximum);
        ventList.forEach(c -> ventsMap.addVent(c, part));

        System.out.println("number of vents: " + ventList.size());
        System.out.println("size of the map: " + maximum);
        System.out.println("number of dangerous areas: " + ventsMap.getDangerousAreas());
    }

    protected static void hydrothermal_venture_first() throws Exception { 
        hydrothermal_venture(Part.first);
    }

    protected static void hydrothermal_venture_second() throws Exception { 
        hydrothermal_venture(Part.second);
    }
    */

    // TODO remove
    /*
    protected static void lanternfish_part_1() throws Exception { 
        lanternfish(80);
    }

    protected static void lanternfish_part_2() throws Exception { 
        lanternfish(256);
    }

    protected static void lanternfish(Integer number_of_days) throws Exception { 

        int initial_lanternfishes = 0;

        // There is a way to codify this.
        // The index of the lanternfish_day list is the day;
        // the amount (the value) of lanternfish_day.get(day) is the number
        // of fishes
        List<Long> lanternfish_day = new ArrayList<>();
        lanternfish_day.addAll(Collections.nCopies(9, (long)0));

        for (String string : Files.readAllLines(getFilePath("AoC_06_input.txt"))) {
                for(String c : string.split(",")) {
                int value = Integer.parseInt(c);
                lanternfish_day.set(value, lanternfish_day.get(value)+1);
                initial_lanternfishes++;
            }
        }

        for(int day = 1; day <= number_of_days; day++) {
            // lanternfish_day
            long newFishes = lanternfish_day.get(0);
            lanternfish_day.remove(0);
            lanternfish_day.set(6, lanternfish_day.get(6) + newFishes);
            lanternfish_day.add(newFishes);

            // long totalFishes = 0;
            // for(int i = 0; i < lanternfish_day.size(); i++) {
            //     totalFishes += lanternfish_day.get(i);
            // }
            // System.out.println(String.format("      day #%3d, number of lanternfishes: %d", day, totalFishes));
        }

        long totalFishes = 0;
        for(int i = 0; i < lanternfish_day.size(); i++) {
            totalFishes += lanternfish_day.get(i);
        }

        System.out.println();
        System.out.println("number of initial lanternfishes: " + initial_lanternfishes);
        System.out.println("number of days: " + number_of_days);
        System.out.println("number of final lanternfishes: " + totalFishes);
    }
    */

    // TODO remove
    /*
    protected static void crabs_horizontal_positioning_part_first() throws Exception { 
        crabs_horizontal_positioning(Part.first);
    }

    protected static void crabs_horizontal_positioning_part_second() throws Exception { 
        crabs_horizontal_positioning(Part.second);
    }

    protected static void crabs_horizontal_positioning(Part part) throws Exception { 

        List<Integer> crabs_input = new ArrayList<>();
        for (String string : Files.readAllLines(getFilePath("AoC_07_input.txt"))) {
            for(String c : string.split(",")) {
                crabs_input.add(Integer.parseInt(c));
            }
        }

        Collections.sort(crabs_input);

        // begin in the middle...
        int middle_index = (int)((crabs_input.size() - 1) / 2);
        int min_value = crabs_input.get(middle_index);
        long min_distance = calculate_distance(crabs_input, min_value, part);

        // System.out.println(String.format("Iterating MIDDLE position %d, fuel cost %d", min_value, min_distance));

        // going up
        int upper_value = crabs_input.get(middle_index);
        do {
            upper_value++;
            long upper_distance = calculate_distance(crabs_input, upper_value, part);

            // System.out.println(String.format("Iterating UP position %d, fuel cost %d", upper_value, upper_distance));

            if(upper_distance <= min_distance) {
                min_value = upper_value;
                min_distance = upper_distance;
            } else {
                break;
            }

        } while (upper_value < crabs_input.get(crabs_input.size() - 1));

        // going down
        int lower_value = crabs_input.get(middle_index);
        do {
            lower_value--;
            long lower_distance = calculate_distance(crabs_input, lower_value, part);

            // System.out.println(String.format("Iterating DOWN position %d, fuel cost %d", lower_value, lower_distance));

            if(lower_distance <= min_distance) {
                min_value = lower_value;
                min_distance = lower_distance;
            } else {
                break;
            }

        } while (lower_value > crabs_input.get(0));

        System.out.println("number of crabs: " + crabs_input.size());
        System.out.println("position with less fuel consumption: " + min_value);
        System.out.println("total of fuel consumption: " + min_distance);
    }

    protected static int calculate_distance(List<Integer> list, int value, Part part) {
        int total_distance = 0;

        for(int i = 0; i < list.size(); i++) {

            int distance = Math.abs(list.get(i) - value);
            if(part == Part.second) {
                int progressive_distance = 0;
                for(int x = distance; x > 0; x--) {
                    progressive_distance += x;
                }
                distance = progressive_distance;
            }
            total_distance += distance;
        }

        return total_distance;
    }
    */

    // TODO remove
    /*
    protected static void seven_segment_search() throws Exception {

        int counter = 0;
        int measurements = 0;
        for (String string : Files.readAllLines(getFilePath("AoC_08_input.txt"))) {
            String[] output = string.split(" ");

            for(int i = output.length - 1; i > output.length - 5; i--) {
                int len = output[i].length();
                if(len == 2 || len == 3 || len == 4 || len == 7) {
                    counter++;
                }
            }

            measurements++;
        }

        System.out.println("number of measurements: " + measurements);
        System.out.println("number of 1, 4, 7 and 8 outputs: " + counter);
    }

    protected static void seven_segment_search_solved() throws Exception { 

        int sum = 0;
        int measurements = 0;
        SevenSegmentDisplay sevenSegmentDisplay = new SevenSegmentDisplay(Part.second);
        for (String string : Files.readAllLines(getFilePath("AoC_08_input.txt"))) {

            int number = sevenSegmentDisplay.processRow(string);
            sum += number;
            measurements++;

            // System.out.println("Decoding each number. Measure #" + measurements + ". Number: " + number + ". Sum: " + sum);
        }

        System.out.println("number of measurements: " + measurements);
        System.out.println("sum of all outputs: " + sum);
    }
    */

    // TODO remove
    /*
    protected static void smoke_basin() throws Exception { 

        int measurementsColumns = 0, measurementsRows = 0;
        HeightMap basinMap = new HeightMap();
        for (String string : Files.readAllLines(getFilePath("AoC_09_input.txt"))) {
            basinMap.processRow(string);

            if(measurementsColumns == 0) {
                measurementsColumns = string.length();
            }
            measurementsRows++;
        }

        int riskLevelSum = basinMap.getRiskLevelSum();

        System.out.println(String.format("number of measurements: %2d rows - %2d columns", measurementsRows, measurementsColumns));
        System.out.println("sum of all risk levels: " + riskLevelSum);
    }

    protected static void find_largest__basins() throws Exception { 

        int measurementsColumns = 0, measurementsRows = 0;
        HeightMap basinMap = new HeightMap();
        for (String string : Files.readAllLines(getFilePath("AoC_09_input.txt"))) {
            basinMap.processRow(string);

            if(measurementsColumns == 0) {
                measurementsColumns = string.length();
            }
            measurementsRows++;
        }

        long largestBasinsResult = basinMap.getLargestBasinsResult();

        System.out.println(String.format("number of measurements: %2d rows - %2d columns", measurementsRows, measurementsColumns));
        System.out.println("Result of multiplying the three largest basins: " + largestBasinsResult);
    }
    */

    // TODO remove
    /*
    protected static void syntax_scoring_corrupted_chunks() throws Exception { 

        int measurements = 0;
        ChunkReader chunkReader = new ChunkReader();
        for (String string : Files.readAllLines(getFilePath("AoC_10_input.txt"))) {

            chunkReader.processLine(string);

            measurements++;
        }

        System.out.println(String.format("number of measurements (lines): %2d", measurements));
        System.out.println(String.format("syntax error score: %2d", chunkReader.getSyntaxErrorScore()));
    }
    */

    /*
    protected static void syntax_scoring_incomplete_chunks() throws Exception { 

        int measurements = 0;
        ChunkReader chunkReader = new ChunkReader();
        for (String string : Files.readAllLines(getFilePath("AoC_10_input.txt"))) {

            chunkReader.processLine(string);

            measurements++;
        }

        System.out.println(String.format("number of measurements (lines): %2d", measurements));
        System.out.println(String.format("competion middle score: %2d", chunkReader.getCompletionStringScore()));
    }
    */

    // TODO remove
    /*
    protected static void dumbo_octopus() throws Exception { 

        int measurements = 0;
        DumboOctopus dumboOctopus = new DumboOctopus();
        for (String string : Files.readAllLines(getFilePath("AoC_11_input.txt"))) {

            dumboOctopus.processRow(string);

            measurements++;
        }

        int step = 0;
        for(; step < 100; step++) {
            dumboOctopus.nextStep();
        }

        System.out.println("After step " + (step) + ": ");
        dumboOctopus.printMap();
        System.out.println();
        System.out.println(String.format("number of measurements (lines): %2d", measurements));
        System.out.println(String.format("number of flashes: %2d", dumboOctopus.getFlashes()));
    }
    */

    // TODO remove
    /*
    protected static void dumbo_octopus_sync() throws Exception { 

        int measurements = 0;
        DumboOctopus dumboOctopus = new DumboOctopus();

        for (String string : Files.readAllLines(getFilePath("AoC_11_input.txt"))) {

            dumboOctopus.processRow(string);

            measurements++;
        }

        do {
            dumboOctopus.nextStep();
        } while(!dumboOctopus.isFlashSync());

        System.out.println("After step " + dumboOctopus.getStepNumber() + ": ");
        dumboOctopus.printMap();
        System.out.println();
        System.out.println(String.format("number of measurements (lines): %2d", measurements));
        System.out.println(String.format("number of flashes: %2d", dumboOctopus.getFlashes()));
    }
    */

    // TODO remove
    /*
    protected static void caves_path_first() throws Exception {
        caves_path(Part.first);
    }

    protected static void caves_path_second() throws Exception {
        caves_path(Part.second);
    }

    protected static void caves_path(Part part) throws Exception { 
        CavesPath cavesPath = new CavesPath();
        cavesPath.processInput("AoC_12_input.txt");
        cavesPath.execute(part);
        cavesPath.printResult();
    }
    */

    protected static void transparent_origami_first() throws Exception { 
        transparent_origami(Part.first);
    }

    protected static void transparent_origami_second() throws Exception { 
        transparent_origami(Part.second);
    }

    protected static void transparent_origami(Part part) throws Exception { 

        String fileName = part == Part.first ? "test_input.txt" : "AoC_13_input.txt";

        Origami origami = new Origami();
        for (String string : Files.readAllLines(getFilePath(fileName))) {
            origami.processRow(string);
        }

        origami.fill();
        origami.foldAll(part);
    }

}



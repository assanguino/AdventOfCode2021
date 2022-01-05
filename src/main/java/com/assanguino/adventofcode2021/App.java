package com.assanguino.adventofcode2021;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    public static final Logger logger = LogManager.getLogger(App.class.getName());

    protected static Map<Integer, Class<? extends Executable>> classMap = new HashMap<>();

    public static void main(String[] args) throws Exception {

        populateClassMap();
        execute(14, Part.second);
    }

    protected static void populateClassMap() {

        // Populate the classes hash table
        classMap.put( 1, SonarSweep.class);
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
        classMap.put(13, Origami.class);
        classMap.put(14, Polymerization.class);
    }

    protected static void execute(int day, Part part) {
        try {
            // Get an instance of the corresponding task to execute (dayObject)
            Class<?>[] constructorParams = { Part.class };
            Class<? extends Executable> dayClass = classMap.get(day);
            Executable dayObject = dayClass.getConstructor(constructorParams).newInstance(part);

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("************************************************ Advent of Code 2021");
            System.out.println("************************************************ Day " + day + ", " + part.toString() + " part");
            System.out.println("************************************************ " + dayObject.printDescription() );

            String fileName = Executable.getInputFile(day, isTestFile(day, part));
            dayObject.processInput(fileName);
            dayObject.execute();
            dayObject.printResult();

            System.out.println("************************************************");
            System.out.println();
            System.out.println();
            System.out.println();

        } catch (Exception ex) {
            logger.log(Level.FATAL, ex.getMessage());
        }
    }

    protected static boolean isTestFile(int day, Part part) {
        return (day == 13 && part == Part.first)
            || (day == 14 && part == Part.first);
    }

}



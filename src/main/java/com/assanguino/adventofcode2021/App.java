package com.assanguino.adventofcode2021;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    public static final Logger logger = LogManager.getLogger(App.class.getName());

    protected static Map<Integer, Class<? extends Executable>> classMap = new HashMap<>();

    public static void main(String[] args) {

        populateClassMap();
        execute(18, Part.SECOND);
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
        classMap.put(15, Chiton.class);
        classMap.put(16, PacketDecoder.class);
        classMap.put(17, TrickShot.class);
        classMap.put(18, SnailFish.class);
    }

    protected static void execute(int day, Part part) {
        try {
            final String separation = "************************************************";

            // Get an instance of the corresponding task to execute (dayObject)
            Class<?>[] constructorParams = { Part.class };
            Class<? extends Executable> dayClass = classMap.get(day);
            Executable dayObject = dayClass.getConstructor(constructorParams).newInstance(part);

            String strPart = part.toString();
            String strDay = dayObject.printDescription();

            logger.info("");
            logger.info("");
            logger.info("");

            logger.info("%s Advent of Code 2021", separation);
            logger.info("%s  Day %s, part %s", separation, day, strPart);
            logger.info("%s %s", separation, strDay);

            String fileName = Executable.getInputFile(day, isTestFile(day, part));
            dayObject.processInput(fileName);
            dayObject.execute();
            dayObject.printResult();

            logger.info(separation);
            logger.info("");
            logger.info("");
            logger.info("");

        } catch (Exception ex) {
            logger.printf(Level.FATAL, ex.getMessage());
        }
    }

    protected static boolean isTestFile(int day, Part part) {
        return (day == 13 && part == Part.FIRST);
    }

}



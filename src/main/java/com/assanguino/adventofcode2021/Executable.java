package com.assanguino.adventofcode2021;

import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface Executable {

    public static final Logger logger = LogManager.getLogger(Executable.class.getName());

    default void processInput(String input) {

        try {
            var resource = App.class.getClassLoader().getResource(input);
            Path path = Path.of(resource.getPath());
            Files.readAllLines(path).forEach(this::processRow);

        } catch (Exception e) {
            logger.printf(Level.FATAL, "Exception while reading [%s] file: %s", input, e.getMessage());
        }
    }

    static String getInputFile(int day, boolean isTest) {
        String sufix = isTest ? "test" : "AofC";
        return String.format("input_%02d_%s.txt", day, sufix);
    }

    /**
     * All the test an input file, that has to be read.
     * This is the method that process it.
     * @param row, of the input file
     */
    void processRow(String row);

    /**
     * Do the math indeed
     * @param part that would be executed
     */
    void execute();

    /**
     * 
     * @param part Exercise part
     * @return A brief description of what it is going to do
     */
    String printDescription();

    /**
     * Several lines of information that explains the final result
     * @param part
     */
    void printResult();

    /**
     * Final result required to overcome the test
     * @param part
     * @return final result as a string
     */
    String getResult();
}

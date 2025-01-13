package com.assanguino.adventofcode2021;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class App {

    public static final Logger logger = LogManager.getLogger(App.class.getName());

    public static void main(String[] args) {
        execute(18, Part.SECOND);
    }

    protected static void execute(int day, Part part) {
        try {
            final String separation = "************************************************";

            // Get an instance of the corresponding task to execute (dayObject)
            Class<?>[] constructorParams = { Part.class };
            Class<? extends Executable> dayClass = ClassMap.getInstance().getClass(null);

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



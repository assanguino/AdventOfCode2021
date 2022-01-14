package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class SonarSweepTest {

    protected static SonarSweep first;
    protected static SonarSweep second;
    protected static String fileName;

    @BeforeClass
    public static void initProperties() {
        first = new SonarSweep(Part.first);
        second = new SonarSweep(Part.second);
        
        fileName = Executable.getInputFile(1, true);
    }

    @Test
    public void testFirstPart() {
        first.processInput(fileName);
        first.execute();

        assertEquals(first.getResult(), "7");
    }

    @Test
    public void testSecondPart() {
        second.processInput(fileName);
        second.execute();

        assertEquals(second.getResult(), "5");
    }

    @Test
    public void testPrintDescription() {
        assertNotEquals(first.printDescription(), "");

        assertNotEquals(second.printDescription(), "");
    }

}

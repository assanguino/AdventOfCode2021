package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.BeforeClass;
import org.junit.Test;

public class DivingTest {

    protected static Diving first;
    protected static Diving second;
    protected static String fileName;

    @BeforeClass
    public static void initProperties() {
        first = new Diving(Part.first);
        second = new Diving(Part.second);
        
        fileName = Executable.getInputFile(2, true);
    }

    @Test
    public void testFirstPart() {
        first.processInput(fileName);
        first.execute();

        assertEquals(first.getResult(), "150");
    }

    @Test
    public void testSecondPart() {
        second.processInput(fileName);
        second.execute();

        assertEquals(second.getResult(), "900");
    }

    @Test
    public void testPrintDescription() {
        assertNotEquals(first.printDescription(), "");

        assertNotEquals(second.printDescription(), "");
    }

}

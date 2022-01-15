package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CrabsTest extends ExecutableTest<Crabs> {

    @Before
    public void init() {
        first = new Crabs(Part.first);
        second = new Crabs(Part.second);        
        fileName = Executable.getInputFile(7, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("37"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("168"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}


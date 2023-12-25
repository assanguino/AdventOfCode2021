package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SonarSweepTest extends ExecutableTest<SonarSweep> {

    @Before
    public void init() {
        first = new SonarSweep(Part.FIRST);
        second = new SonarSweep(Part.SECOND);        
        fileName = Executable.getInputFile(1, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("7"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("5"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}

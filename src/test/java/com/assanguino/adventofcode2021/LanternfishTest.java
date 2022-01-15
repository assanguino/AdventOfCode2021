package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class LanternfishTest extends ExecutableTest<Lanternfish> {

    @Before
    public void init() {
        first = new Lanternfish(Part.first);
        second = new Lanternfish(Part.second);        
        fileName = Executable.getInputFile(6, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("5934"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("26984457539"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}


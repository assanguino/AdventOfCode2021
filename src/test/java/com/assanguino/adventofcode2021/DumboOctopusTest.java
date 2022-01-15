package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DumboOctopusTest extends ExecutableTest<DumboOctopus> {

    @Before
    public void init() {
        first = new DumboOctopus(Part.first);
        second = new DumboOctopus(Part.second);        
        fileName = Executable.getInputFile(11, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("1656"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("195"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}


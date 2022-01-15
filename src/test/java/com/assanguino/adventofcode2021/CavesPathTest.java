package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CavesPathTest extends ExecutableTest<CavesPath> {

    @Before
    public void init() {
        first = new CavesPath(Part.first);
        second = new CavesPath(Part.second);        
        fileName = Executable.getInputFile(12, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("10"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("36"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}


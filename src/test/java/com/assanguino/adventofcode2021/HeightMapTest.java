package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class HeightMapTest extends ExecutableTest<HeightMap> {

    @Before
    public void init() {
        first = new HeightMap(Part.FIRST);
        second = new HeightMap(Part.SECOND);        
        fileName = Executable.getInputFile(9, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("15"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("1134"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}


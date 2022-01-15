package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class VentsMapTest extends ExecutableTest<VentsMap> {

    @Before
    public void init() {
        first = new VentsMap(Part.first);
        second = new VentsMap(Part.second);        
        fileName = Executable.getInputFile(5, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("5"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("12"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}

package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PolymerizationTest extends ExecutableTest<Polymerization> {

    @Before
    public void init() {
        first = new Polymerization(Part.FIRST);
        second = new Polymerization(Part.SECOND);        
        fileName = Executable.getInputFile(14, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("1588"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("2188189693529"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}



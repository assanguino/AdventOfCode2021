package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OrigamiTest extends ExecutableTest<Origami> {

    @Before
    public void init() {
        first = new Origami(Part.first);
        second = new Origami(Part.second);        
        fileName = Executable.getInputFile(13, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("17"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("PGHRKLKL"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}



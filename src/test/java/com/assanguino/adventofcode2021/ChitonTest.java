package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ChitonTest extends ExecutableTest<Chiton> {

    @Before
    public void init() {
        first = new Chiton(Part.FIRST);
        second = new Chiton(Part.SECOND);        
        fileName = Executable.getInputFile(15, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("40"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("315"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}



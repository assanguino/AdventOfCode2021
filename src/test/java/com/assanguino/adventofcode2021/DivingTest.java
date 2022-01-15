package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class DivingTest extends ExecutableTest<Diving> {

    @Before
    public void init() {
        first = new Diving(Part.first);
        second = new Diving(Part.second);        
        fileName = Executable.getInputFile(2, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("150"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("900"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }
}

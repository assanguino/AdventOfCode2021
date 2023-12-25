package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TrickShotTest extends ExecutableTest<TrickShot> {

    @Before
    public void init() {
        first = new TrickShot(Part.FIRST);
        second = new TrickShot(Part.SECOND);        
        fileName = Executable.getInputFile(17, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("45"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("112"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}


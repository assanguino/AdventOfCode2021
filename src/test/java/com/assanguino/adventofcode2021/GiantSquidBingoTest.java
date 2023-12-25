package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GiantSquidBingoTest extends ExecutableTest<GiantSquidBingo> {

    @Before
    public void init() {
        first = new GiantSquidBingo(Part.FIRST);
        second = new GiantSquidBingo(Part.SECOND);        
        fileName = Executable.getInputFile(4, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("4512"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("1924"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}

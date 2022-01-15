package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GiantSquidBingoTest extends ExecutableTest<GiantSquidBingo> {

    @Before
    public void init() {
        first = new GiantSquidBingo(Part.first);
        second = new GiantSquidBingo(Part.second);        
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

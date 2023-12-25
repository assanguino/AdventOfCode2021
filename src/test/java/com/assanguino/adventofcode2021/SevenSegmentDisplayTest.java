package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SevenSegmentDisplayTest extends ExecutableTest<SevenSegmentDisplay> {

    @Before
    public void init() {
        first = new SevenSegmentDisplay(Part.FIRST);
        second = new SevenSegmentDisplay(Part.SECOND);        
        fileName = Executable.getInputFile(8, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("26"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("61229"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}


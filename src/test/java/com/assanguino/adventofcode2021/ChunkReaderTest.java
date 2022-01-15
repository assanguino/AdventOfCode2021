package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ChunkReaderTest extends ExecutableTest<ChunkReader> {

    @Before
    public void init() {
        first = new ChunkReader(Part.first);
        second = new ChunkReader(Part.second);        
        fileName = Executable.getInputFile(10, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("26397"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("288957"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}


package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class PacketDecoderTest extends ExecutableTest<PacketDecoder> {

    @Before
    public void init() {
        first = new PacketDecoder(Part.FIRST);
        second = new PacketDecoder(Part.SECOND);        
        fileName = Executable.getInputFile(16, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("20"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("0"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}



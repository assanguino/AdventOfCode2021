package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BinaryDiagnosticTest extends ExecutableTest<BinaryDiagnostic> {

    @Before
    public void init() {
        first = new BinaryDiagnostic(Part.FIRST);
        second = new BinaryDiagnostic(Part.SECOND);        
        fileName = Executable.getInputFile(3, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("198"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("230"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

}

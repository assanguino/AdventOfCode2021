package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class BinaryDiagnosticTest extends ExecutableTest<BinaryDiagnostic> {

    @Before
    public void init() {
        first = new BinaryDiagnostic(Part.FIRST);
        second = new BinaryDiagnostic(Part.SECOND);        
        day = ClassMap.getInstance().getDay(BinaryDiagnostic.class);
        fileName = Executable.getInputFile(day, true);
    }

}

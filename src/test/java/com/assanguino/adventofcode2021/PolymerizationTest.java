package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class PolymerizationTest extends ExecutableTest<Polymerization> {

    @Before
    public void init() {
        first = new Polymerization(Part.FIRST);
        second = new Polymerization(Part.SECOND);        
        day = ClassMap.getInstance().getDay(Polymerization.class);
        fileName = Executable.getInputFile(day, true);
    }

}



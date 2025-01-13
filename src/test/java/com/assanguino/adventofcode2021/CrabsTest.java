package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class CrabsTest extends ExecutableTest<Crabs> {

    @Before
    public void init() {
        first = new Crabs(Part.FIRST);
        second = new Crabs(Part.SECOND);        
        day = ClassMap.getInstance().getDay(Crabs.class);
        fileName = Executable.getInputFile(day, true);
    }

}


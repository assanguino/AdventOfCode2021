package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class SonarSweepTest extends ExecutableTest<SonarSweep> {

    @Before
    public void init() {
        first = new SonarSweep(Part.FIRST);
        second = new SonarSweep(Part.SECOND);        
        day = ClassMap.getInstance().getDay(SonarSweep.class);
        fileName = Executable.getInputFile(day, true);
    }

}

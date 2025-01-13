package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class CavesPathTest extends ExecutableTest<CavesPath> {

    @Before
    public void init() {
        first = new CavesPath(Part.FIRST);
        second = new CavesPath(Part.SECOND);        
        day = ClassMap.getInstance().getDay(CavesPath.class);
        fileName = Executable.getInputFile(day, true);
    }

}


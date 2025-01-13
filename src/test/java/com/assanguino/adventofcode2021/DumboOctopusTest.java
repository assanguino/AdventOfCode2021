package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class DumboOctopusTest extends ExecutableTest<DumboOctopus> {

    @Before
    public void init() {
        first = new DumboOctopus(Part.FIRST);
        second = new DumboOctopus(Part.SECOND);        
        day = ClassMap.getInstance().getDay(DumboOctopus.class);
        fileName = Executable.getInputFile(day, true);
    }

}


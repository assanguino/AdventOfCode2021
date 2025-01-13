package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class VentsMapTest extends ExecutableTest<VentsMap> {

    @Before
    public void init() {
        first = new VentsMap(Part.FIRST);
        second = new VentsMap(Part.SECOND);        
        day = ClassMap.getInstance().getDay(VentsMap.class);
        fileName = Executable.getInputFile(day, true);
    }

}

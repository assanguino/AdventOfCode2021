package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class HeightMapTest extends ExecutableTest<HeightMap> {

    @Before
    public void init() {
        first = new HeightMap(Part.FIRST);
        second = new HeightMap(Part.SECOND);        
        day = ClassMap.getInstance().getDay(HeightMap.class);
        fileName = Executable.getInputFile(day, true);
    }

}

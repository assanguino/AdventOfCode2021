package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class LanternfishTest extends ExecutableTest<Lanternfish> {

    @Before
    public void init() {
        first = new Lanternfish(Part.FIRST);
        second = new Lanternfish(Part.SECOND);        
        day = ClassMap.getInstance().getDay(Lanternfish.class);
        fileName = Executable.getInputFile(day, true);
    }

}


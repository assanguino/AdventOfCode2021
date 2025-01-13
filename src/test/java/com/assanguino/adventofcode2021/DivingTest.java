package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class DivingTest extends ExecutableTest<Diving> {

    @Before
    public void init() {
        first = new Diving(Part.FIRST);
        second = new Diving(Part.SECOND);        
        day = ClassMap.getInstance().getDay(Diving.class);
        fileName = Executable.getInputFile(day, true);
    }

}

package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class ChitonTest extends ExecutableTest<Chiton> {

    @Before
    public void init() {
        first = new Chiton(Part.FIRST);
        second = new Chiton(Part.SECOND);        
        day = ClassMap.getInstance().getDay(Chiton.class);
        fileName = Executable.getInputFile(day, true);
    }

}



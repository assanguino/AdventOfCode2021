package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class OrigamiTest extends ExecutableTest<Origami> {

    @Before
    public void init() {
        first = new Origami(Part.FIRST);
        second = new Origami(Part.SECOND);        
        day = ClassMap.getInstance().getDay(Origami.class);
        fileName = Executable.getInputFile(day, true);
    }

}


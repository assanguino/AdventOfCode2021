package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class TrickShotTest extends ExecutableTest<TrickShot> {

    @Before
    public void init() {
        first = new TrickShot(Part.FIRST);
        second = new TrickShot(Part.SECOND);        
        day = ClassMap.getInstance().getDay(TrickShot.class);
        fileName = Executable.getInputFile(day, true);
    }

}

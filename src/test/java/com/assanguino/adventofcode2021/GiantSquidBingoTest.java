package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class GiantSquidBingoTest extends ExecutableTest<GiantSquidBingo> {

    @Before
    public void init() {
        first = new GiantSquidBingo(Part.FIRST);
        second = new GiantSquidBingo(Part.SECOND);        
        day = ClassMap.getInstance().getDay(GiantSquidBingo.class);
        fileName = Executable.getInputFile(day, true);
    }

}

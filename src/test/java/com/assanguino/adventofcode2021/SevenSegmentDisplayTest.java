package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class SevenSegmentDisplayTest extends ExecutableTest<SevenSegmentDisplay> {

    @Before
    public void init() {
        first = new SevenSegmentDisplay(Part.FIRST);
        second = new SevenSegmentDisplay(Part.SECOND);        
        day = ClassMap.getInstance().getDay(SevenSegmentDisplay.class);
        fileName = Executable.getInputFile(day, true);
    }

}


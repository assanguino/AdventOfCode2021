package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class ChunkReaderTest extends ExecutableTest<ChunkReader> {

    @Before
    public void init() {
        first = new ChunkReader(Part.FIRST);
        second = new ChunkReader(Part.SECOND);        
        day = ClassMap.getInstance().getDay(ChunkReader.class);
        fileName = Executable.getInputFile(day, true);
    }

}


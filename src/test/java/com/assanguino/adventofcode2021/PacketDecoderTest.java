package com.assanguino.adventofcode2021;

import org.junit.Before;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public class PacketDecoderTest extends ExecutableTest<PacketDecoder> {

    @Before
    public void init() {
        first = new PacketDecoder(Part.FIRST);
        second = new PacketDecoder(Part.SECOND);        
        day = ClassMap.getInstance().getDay(PacketDecoder.class);
        fileName = Executable.getInputFile(day, true);
    }

}


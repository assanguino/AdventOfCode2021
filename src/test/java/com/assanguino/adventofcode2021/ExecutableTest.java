package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.assanguino.adventofcode2021.util.ClassMap;
import com.assanguino.adventofcode2021.util.Part;

public abstract class ExecutableTest<T extends Executable> {

    protected T first;
    protected T second;
    protected Integer day;
    protected static String fileName;

    protected void testPart(T obj, Part part) {
        obj.processInput(fileName);
        obj.execute();       

        assertEquals(obj.getResult(), ClassMap.getInstance().getResult(day, part));
    }

    @Test
    public void testFirstPart() {
        testPart(first, Part.FIRST);
    }

    @Test
    public void testSecondPart() {
        testPart(second, Part.SECOND);
    }

    @Test
    public void testPrintDescription() {
        assertNotEquals(first.printDescription(), "");
        assertNotEquals(second.printDescription(), "");
    }

}

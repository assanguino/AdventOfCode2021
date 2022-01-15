package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertNotEquals;

public abstract class ExecutableTest<T extends Executable> {

    protected T first;
    protected T second;
    protected static String fileName;

    public boolean executeFirstPart(String result) {
        first.processInput(fileName);
        first.execute();
        return first.getResult().equals(result);
    }

    public boolean executeSecondPart(String result) {
        second.processInput(fileName);
        second.execute();
        return second.getResult().equals(result);
    }

    public void testPrintDescription() {
        assertNotEquals(first.printDescription(), "");
        assertNotEquals(second.printDescription(), "");
    }

}

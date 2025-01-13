package com.assanguino.adventofcode2021.util;

import com.assanguino.adventofcode2021.Executable;

public class Triple {
    
    public Class<? extends Executable> executableClass;
    public String firstResult;
    public String secondResult;

    public static Triple of(Class<? extends Executable> executableClass, String firstResult, String secondResult) {

        Triple obj = new Triple();
        obj.executableClass = executableClass;
        obj.firstResult = firstResult;
        obj.secondResult = secondResult;
        
        return obj;
    }
}

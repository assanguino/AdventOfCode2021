package com.assanguino.adventofcode2021.util;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.assanguino.adventofcode2021.BinaryDiagnostic;
import com.assanguino.adventofcode2021.CavesPath;
import com.assanguino.adventofcode2021.Chiton;
import com.assanguino.adventofcode2021.ChunkReader;
import com.assanguino.adventofcode2021.Crabs;
import com.assanguino.adventofcode2021.Diving;
import com.assanguino.adventofcode2021.DumboOctopus;
import com.assanguino.adventofcode2021.Executable;
import com.assanguino.adventofcode2021.GiantSquidBingo;
import com.assanguino.adventofcode2021.HeightMap;
import com.assanguino.adventofcode2021.Lanternfish;
import com.assanguino.adventofcode2021.Origami;
import com.assanguino.adventofcode2021.PacketDecoder;
import com.assanguino.adventofcode2021.Polymerization;
import com.assanguino.adventofcode2021.SevenSegmentDisplay;
import com.assanguino.adventofcode2021.SnailFish;
import com.assanguino.adventofcode2021.SonarSweep;
import com.assanguino.adventofcode2021.TrickShot;
import com.assanguino.adventofcode2021.VentsMap;

public class ClassMap {
    private Map<Integer, Triple> map = new HashMap<>();

    // Singleton
    private static ClassMap instance;
    
    private ClassMap() {
        populateMap();
    }

    public static ClassMap getInstance() {
        if(instance == null) {
            instance = new ClassMap();
        }
        
        return instance;
    }

    public Class<? extends Executable> getClass(Integer day) {
        return map.get(day).executableClass;
    }

    public Integer getDay(Class<? extends Executable> executableClass) {
        return map.entrySet()
                  .stream()
                  .filter(c -> c.getValue().executableClass.equals(executableClass))
                  .map(Map.Entry::getKey)
                  .collect(Collectors.toList()).get(0);
    }

    public String getResult(Integer day, Part part) {
        Triple element = map.get(day);
        return part == Part.FIRST ? element.firstResult : element.secondResult;
    }

    protected void populateMap() {
        
        map.put(1, Triple.of(SonarSweep.class, "7", "5"));
        map.put(2, Triple.of(Diving.class, "150", "900"));
        map.put(3, Triple.of(BinaryDiagnostic.class, "198", "230"));
        map.put(4, Triple.of(GiantSquidBingo.class, "4512", "1924"));
        map.put(5, Triple.of(VentsMap.class, "5", "12"));
        map.put(6, Triple.of(Lanternfish.class, "5934", "26984457539"));
        map.put(7, Triple.of(Crabs.class, "37", "168"));
        map.put(8, Triple.of(SevenSegmentDisplay.class, "26", "61229"));
        map.put(9, Triple.of(HeightMap.class, "15", "1134"));
        map.put(10, Triple.of(ChunkReader.class, "26397", "288957"));
        map.put(11, Triple.of(DumboOctopus.class, "1656", "195"));
        map.put(12, Triple.of(CavesPath.class, "10", "36"));
        map.put(13, Triple.of(Origami.class, "17", "PGHRKLKL"));
        map.put(14, Triple.of(Polymerization.class, "1588", "2188189693529"));
        map.put(15, Triple.of(Chiton.class, "40", "315"));
        map.put(16, Triple.of(PacketDecoder.class, "20", "0"));
        map.put(17, Triple.of(TrickShot.class, "45", "112"));
        map.put(18, Triple.of(SnailFish.class, "4140", "3993"));
    }

}


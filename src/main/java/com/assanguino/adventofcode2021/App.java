package com.assanguino.adventofcode2021;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.javatuples.Pair;

public class App {

    @Deprecated
    protected static Map<Pair<Integer, Part>, String> methodMap = new HashMap<>( );

    protected static Map<Integer, Class<? extends Executable>> classMap = new HashMap<>();

    public static void main(String[] args) throws Exception {

        // Populate the methods map
        methodMap.put(new Pair<Integer, Part>(13, Part.first),  "transparent_origami_first");
        methodMap.put(new Pair<Integer, Part>(13, Part.second), "transparent_origami_second");

        // execute(12, Part.second);

        classMap.put( 1, SonarSweep.class);
        classMap.put( 2, Diving.class);
        classMap.put( 3, BinaryDiagnostic.class);
        classMap.put( 4, GiantSquidBingo.class);
        classMap.put( 5, VentsMap.class);
        classMap.put( 6, Lanternfish.class);
        classMap.put( 7, Crabs.class);
        classMap.put( 8, SevenSegmentDisplay.class);
        classMap.put( 9, HeightMap.class);
        classMap.put(10, ChunkReader.class);
        classMap.put(11, DumboOctopus.class);
        classMap.put(12, CavesPath.class);

        execute_new_version( 1, Part.second);
    }

    @Deprecated
    protected static Path getFilePath(String fileName) throws Exception {
        URL resource = App.class.getClassLoader().getResource(fileName);

        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        }

        return Path.of(resource.getPath());
    }
    
    @Deprecated
    protected static void execute(int day, Part part) throws Exception {

        try {
            var key = new Pair<Integer, Part>(day, part);
            String methodName = methodMap.get(key);
            var method = App.class.getDeclaredMethod(methodName);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("************************************************ Advent of Code 2021");
            System.out.println("************************************************ day " + day + ", " + part.toString() + " part");
            System.out.println("************************************************ " + methodName);
            method.invoke(null);
            System.out.println("************************************************");
            System.out.println();
            System.out.println();
            System.out.println();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static void execute_new_version(int day, Part part) {
        try {
            Class<?>[] constructorParams = { Part.class };

            Class<? extends Executable> dayClass = classMap.get(day);
            Executable dayObject = dayClass.getConstructor(constructorParams).newInstance(part);

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("************************************************ Advent of Code 2021");
            System.out.println("************************************************ Day " + day + ", " + part.toString() + " part");
            System.out.println("************************************************ " + dayObject.printDescription() );

            dayObject.processInput(Executable.getInputFile(day));
            dayObject.execute();
            dayObject.printResult();

            System.out.println("************************************************");
            System.out.println();
            System.out.println();
            System.out.println();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static void transparent_origami_first() throws Exception { 
        transparent_origami(Part.first);
    }

    protected static void transparent_origami_second() throws Exception { 
        transparent_origami(Part.second);
    }

    protected static void transparent_origami(Part part) throws Exception { 

        String fileName = part == Part.first ? "test_input.txt" : "AoC_13_input.txt";

        Origami origami = new Origami();
        for (String string : Files.readAllLines(getFilePath(fileName))) {
            origami.processRow(string);
        }

        origami.fill();
        origami.foldAll(part);
    }

}



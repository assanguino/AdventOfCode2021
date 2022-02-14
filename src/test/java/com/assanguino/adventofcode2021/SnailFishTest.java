package com.assanguino.adventofcode2021;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SnailFishTest extends ExecutableTest<SnailFish> {

    @Before
    public void init() {
        first = new SnailFish(Part.first);
        second = new SnailFish(Part.second);        
        fileName = Executable.getInputFile(18, true);
    }

    @Test
    public void testFirstPart() {
        assertTrue(executeFirstPart("4140"));
    }

    @Test
    public void testSecondPart() {
        assertTrue(executeSecondPart("3993"));
    }

    @Test
    public void testPrintDescription() {
        super.testPrintDescription();
    }

    @Test
    public void testChecks() {
        List<String> numbers = new ArrayList<>();

        numbers.add("[1,2]");
        numbers.add("[[1,2],3]");
        numbers.add("[9,[8,7]]");
        numbers.add("[[1,9],[8,5]]");
        numbers.add("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]");
        numbers.add("[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]");
        numbers.add("[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]");

        for (var n : numbers) {
            var snailFishNumber = new LiteralSnailFishNumber(n);
            assertTrue(snailFishNumber.check());
        }
    }

    @Test
    public void testAddition() {
        var a = new LiteralSnailFishNumber("[1,2]");
        var b = new LiteralSnailFishNumber("[[3,4],5]");
        a.add(b);
        assertTrue(a.toString().equals("[[1,2],[[3,4],5]]"));
    }

    @Test
    public void testExplodes() {

        List<List<String>> numbers = new ArrayList<>();

        numbers.add(new ArrayList<String>(Arrays.asList("[[[[4,2],2],6],[8,7]]", "[8,7]", "[[[[4,2],2],14],0]")));
        numbers.add(new ArrayList<String>(Arrays.asList("[[[[[9,8],1],2],3],4]", "[9,8]", "[[[[0,9],2],3],4]")));
        numbers.add(new ArrayList<String>(Arrays.asList("[7,[6,[5,[4,[3,2]]]]]", "[3,2]", "[7,[6,[5,[7,0]]]]")));
        numbers.add(new ArrayList<String>(Arrays.asList("[[6,[5,[4,[3,2]]]],1]", "[3,2]", "[[6,[5,[7,0]]],3]")));
        numbers.add(new ArrayList<String>(Arrays.asList("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[7,3]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")));
        numbers.add(new ArrayList<String>(Arrays.asList("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[3,2]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")));

        for (List<String> values : numbers) {
            var snailFishNumber = new LiteralSnailFishNumber(values.get(0));
            Integer index = values.get(0).indexOf(values.get(1));
            snailFishNumber.explode(index);
            assertTrue(values.get(2).equals(snailFishNumber.toString()));
        }
    }

    @Test
    public void testSplits() {
        List<String> numbers = new ArrayList<>();

        numbers.add("[[[[0,7],4],[15,[0,13]]],[1,1]]");
        numbers.add("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]");
        numbers.add("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]");

        var snailFishNumber = new LiteralSnailFishNumber(numbers.get(0));
        assertTrue(snailFishNumber.split());
        assertTrue(snailFishNumber.toString().equals(numbers.get(1)));
        assertTrue(snailFishNumber.split());
        assertTrue(snailFishNumber.toString().equals(numbers.get(2)));
    }

    @Test
    public void testSum1() {
        var a = new LiteralSnailFishNumber("[[[[4,3],4],4],[7,[[8,4],9]]]");
        var b = new LiteralSnailFishNumber("[1,1]");
        a.sum(b);

        assertTrue(a.toString().equals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"));
    }

    @Test
    public void testSum2() {
        List<String> numbers = new ArrayList<>();

        numbers.add("[1,1]");
        numbers.add("[2,2]");
        numbers.add("[3,3]");
        numbers.add("[4,4]");
        numbers.add("[5,5]");
        numbers.add("[6,6]");

        Map<Integer, String> results = new HashMap<>();
        results.put(4, "[[[[1,1],[2,2]],[3,3]],[4,4]]");
        results.put(5, "[[[[3,0],[5,3]],[4,4]],[5,5]]");
        results.put(6, "[[[[5,0],[7,4]],[5,5]],[6,6]]");

        var snailFishNumber = new LiteralSnailFishNumber(numbers.get(0));
        for(int i = 1; i < numbers.size(); i++) {
            snailFishNumber.sum(new LiteralSnailFishNumber(numbers.get(i)));

            if(results.containsKey(i+1)) {
                assertTrue(snailFishNumber.toString().equals(results.get(i+1)));
            }
        }
    }

    @Test
    public void testSum3() {
        List<String> numbers = new ArrayList<>();

        numbers.add("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]");
        numbers.add("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]");
        numbers.add("[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]");
        numbers.add("[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]");
        numbers.add("[7,[5,[[3,8],[1,4]]]]");
        numbers.add("[[2,[2,2]],[8,[8,1]]]");
        numbers.add("[2,9]");
        numbers.add("[1,[[[9,3],9],[[9,0],[0,7]]]]");
        numbers.add("[[[5,[7,4]],7],1]");
        numbers.add("[[[[4,2],2],6],[8,7]]");
        // ... and the final result
        numbers.add("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]");

        var snailFishNumber = new LiteralSnailFishNumber(numbers.get(0));
        for(int i = 1; i < numbers.size()-1; i++) {
            snailFishNumber.sum(new LiteralSnailFishNumber(numbers.get(i)));
        }

        assertTrue(snailFishNumber.toString().equals(numbers.get(numbers.size()-1)));
    }

    protected void testSumAndCompare(String a, String b, String c) {
        var one = new LiteralSnailFishNumber(a);
        var two = new LiteralSnailFishNumber(b);

        one.sum(two);
        assertTrue(one.toString().equals(c));
    }

    @Test
    public void testRandomSum1() {
        testSumAndCompare(
                "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]", 
                "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]", 
                "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]");
    }

    @Test
    public void testRandomSum2() {
        testSumAndCompare(
                "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]",
                "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
                "[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]");
    }

    @Test
    public void testRandomSum3() {
        testSumAndCompare(
                "[[[[6,7],[6,7]],[[7,7],[0,7]]],[[[8,7],[7,7]],[[8,8],[8,0]]]]",
                "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
                "[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]");
    }

    @Test
    public void testRandomSum4() {
        testSumAndCompare(
                "[[[[7,0],[7,7]],[[7,7],[7,8]]],[[[7,7],[8,8]],[[7,7],[8,7]]]]",
                "[7,[5,[[3,8],[1,4]]]]",
                "[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]");
    }

    @Test
    public void testRandomSum5() {
        testSumAndCompare(
                "[[[[7,7],[7,8]],[[9,5],[8,7]]],[[[6,8],[0,8]],[[9,9],[9,0]]]]",
                "[[2,[2,2]],[8,[8,1]]]",
                "[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]");
    }

    @Test
    public void testRandomSum6() {
        testSumAndCompare(
                "[[[[6,6],[6,6]],[[6,0],[6,7]]],[[[7,7],[8,9]],[8,[8,1]]]]",
                "[2,9]",
                "[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]");
    }

    @Test
    public void testRandomSum7() {
        testSumAndCompare(
                "[[[[6,6],[7,7]],[[0,7],[7,7]]],[[[5,5],[5,6]],9]]",
                "[1,[[[9,3],9],[[9,0],[0,7]]]]",
                "[[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]");
    }

    @Test
    public void testRandomSum8() {
        testSumAndCompare(
                "[[[[7,8],[6,7]],[[6,8],[0,8]]],[[[7,7],[5,0]],[[5,5],[5,6]]]]",
                "[[[5,[7,4]],7],1]",
                "[[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]");
    }

    @Test
    public void testRandomSum9() {
        testSumAndCompare(
                "[[[[7,7],[7,7]],[[8,7],[8,7]]],[[[7,0],[7,7]],9]]",
                "[[[[4,2],2],6],[8,7]]",
                "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]");
    }

    @Test
    public void testMagnitude() {
        Map<String, Long> testMap = new HashMap<>();

        testMap.put("[[9,1],[1,9]]", (long)129);
        testMap.put("[[1,2],[[3,4],5]]", (long)143);
        testMap.put("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", (long)1384);
        testMap.put("[[[[1,1],[2,2]],[3,3]],[4,4]]", (long)445);
        testMap.put("[[[[3,0],[5,3]],[4,4]],[5,5]]", (long)791);
        testMap.put("[[[[5,0],[7,4]],[5,5]],[6,6]]", (long)1137);
        testMap.put("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", (long)3488);

        for(var e : testMap.entrySet()) {
            var snailFishNumber = new LiteralSnailFishNumber(e.getKey());
            assertTrue(snailFishNumber.magnitude().equals(e.getValue()));
        }
        
    }

}

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

            var nestedSnailFishNumber = new NestedSnailFishNumber(n);
            assertTrue(nestedSnailFishNumber.check());
        }
    }

    @Test
    public void testAddition() {

        String a = "[1,2]";
        String b = "[[3,4],5]";
        String c = "[[1,2],[[3,4],5]]";

        var literal_a = new LiteralSnailFishNumber(a);
        var literal_b = new LiteralSnailFishNumber(b);
        literal_a.add(literal_b);
        assertTrue(literal_a.toString().equals(c));

        var nested_a = new NestedSnailFishNumber(a);
        var nested_b = new NestedSnailFishNumber(b);
        nested_a.add(nested_b);
        assertTrue(nested_a.toString().equals(c));
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

            // new (with nested snailfish numbers)
            var nestedSnailFishNumber = new NestedSnailFishNumber(values.get(0));
            var regular = nestedSnailFishNumber.getRegularSnailFishNumber(values.get(1));
            regular.explode(SnailFishNumber.NESTED_LEVEL+1);
            assertTrue(values.get(2).equals(nestedSnailFishNumber.toString()));
        }
    }

    @Test
    public void testSplits() {
        List<String> numbers = new ArrayList<>();

        numbers.add("[[[[0,7],4],[15,[0,13]]],[1,1]]");
        numbers.add("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]");
        numbers.add("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]");

        var literalSnailFishNumber = new LiteralSnailFishNumber(numbers.get(0));
        assertTrue(literalSnailFishNumber.split());
        assertTrue(literalSnailFishNumber.toString().equals(numbers.get(1)));
        assertTrue(literalSnailFishNumber.split());
        assertTrue(literalSnailFishNumber.toString().equals(numbers.get(2)));

        var nestedSnailFishNumber = new NestedSnailFishNumber(numbers.get(0));
        assertTrue(nestedSnailFishNumber.split());
        assertTrue(nestedSnailFishNumber.toString().equals(numbers.get(1)));
        assertTrue(nestedSnailFishNumber.split());
        assertTrue(nestedSnailFishNumber.toString().equals(numbers.get(2)));
    }

    @Test
    public void testSum1() {
        String str_a = "[[[[4,3],4],4],[7,[[8,4],9]]]";
        String str_b = "[1,1]";
        String str_result = "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]";

        var literal_a = new LiteralSnailFishNumber(str_a);
        var literal_b = new LiteralSnailFishNumber(str_b);
        literal_a.sum(literal_b);
        assertTrue(literal_a.toString().equals(str_result));

        var nested_a = new NestedSnailFishNumber(str_a);
        var nested_b = new NestedSnailFishNumber(str_b);
        nested_a.sum(nested_b);
        assertTrue(nested_a.toString().equals(str_result));
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
        var nestedSnailFishNumber = new NestedSnailFishNumber(numbers.get(0));
        for(int i = 1; i < numbers.size(); i++) {
            snailFishNumber.sum(new LiteralSnailFishNumber(numbers.get(i)));
            nestedSnailFishNumber.sum(new NestedSnailFishNumber(numbers.get(i)));

            if(results.containsKey(i+1)) {
                assertTrue(snailFishNumber.toString().equals(results.get(i+1)));
                assertTrue(nestedSnailFishNumber.toString().equals(results.get(i+1)));
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
        var nestedSnailFishNumber = new NestedSnailFishNumber(numbers.get(0));
        for(int i = 1; i < numbers.size()-1; i++) {
            snailFishNumber.sum(new LiteralSnailFishNumber(numbers.get(i)));
            nestedSnailFishNumber.sum(new NestedSnailFishNumber(numbers.get(i)));
        }

        assertTrue(snailFishNumber.toString().equals(numbers.get(numbers.size()-1)));
        assertTrue(nestedSnailFishNumber.toString().equals(numbers.get(numbers.size()-1)));
    }

    protected void testSumAndCompare(String a, String b, String c) {
        var literal_one = new LiteralSnailFishNumber(a);
        var literal_two = new LiteralSnailFishNumber(b);

        var nested_one = new NestedSnailFishNumber(a);
        var nested_two = new NestedSnailFishNumber(b);

        literal_one.sum(literal_two);
        assertTrue(literal_one.toString().equals(c));
        nested_one.sum(nested_two);
        assertTrue(nested_one.toString().equals(c));
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
            var literalSnailFishNumber = new LiteralSnailFishNumber(e.getKey());
            assertTrue(literalSnailFishNumber.magnitude().equals(e.getValue()));
            var nestedSnailFishNumber = new NestedSnailFishNumber(e.getKey());
            assertTrue(nestedSnailFishNumber.magnitude().equals(e.getValue()));
        }
        
    }

}

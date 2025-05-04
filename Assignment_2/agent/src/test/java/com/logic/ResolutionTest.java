package com.logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ResolutionTest {

    @Test
    public void test1(){
        Expression expected = new Literal("B");

        Expression implication = new Implication(
                new Literal("A"),
                new Literal("B")
        );
        Expression literal = new Literal("A");

        try {
                List<Expression> resolutions = implication.resolution(literal);
                Assert.assertEquals(1, resolutions.size());
                Assert.assertTrue(util.falseEquality(expected, resolutions.get(0)), expected.isEqual(resolutions.get(0)));
        } catch (Contradiction e) {
                Assert.assertTrue(false);
        }
    }

    @Test
    public void test2(){
        Expression expected = new Negation(
                new Literal("A")
        );

        Expression implication = new Implication(
                new Literal("A"),
                new Literal("B")
        );

        Expression negation = new Negation(new Literal("B"));

        try {
        List<Expression> resolutions = implication.resolution(negation);

        Assert.assertEquals(1, resolutions.size());
        Assert.assertTrue(util.falseEquality(expected, resolutions.get(0)), expected.isEqual(resolutions.get(0)));
        } catch (Contradiction e) {
                Assert.assertTrue(false);
        }
    }

    @Test
    public void test3(){
        Expression expected = new Literal("A");

        Expression disjunction = new Disjunction(
                Arrays.asList(
                        new Literal("A"),
                        new Literal("B")
                )
        );
        Expression negation = new Negation(new Literal("B"));

        try {
        List<Expression> resolutions = disjunction.resolution(negation);
        Assert.assertEquals(1, resolutions.size());
        Assert.assertTrue(util.falseEquality(expected, resolutions.get(0)), expected.isEqual(resolutions.get(0)));
        } catch (Contradiction e) {
                Assert.assertTrue(false);
        }
    }

    @Test
    public void test4(){
        List<Expression> expected = List.of(
                new Literal("A"),
                new Negation(
                        new Literal("B")),
                new Negation(
                        new Literal("C"))
        );

        Expression disjunction = new Disjunction(
                Arrays.asList(
                        new Literal("A"),
                        new Literal("B"),
                        new Literal("C")
                )
        );

        Expression conjunction = new Conjunction(
                Arrays.asList(
                        new Negation(
                                new Literal("B")
                        ),
                        new Negation(
                                new Literal("C")
                        )
                )
        );

        try {
        List<Expression> resolutions = disjunction.resolution(conjunction);
        Assert.assertTrue("List not same content", util.sameContent(expected, resolutions));
        } catch (Contradiction e) {
                Assert.assertTrue(false);
        }
    }

    @Test
    public void test5(){
        boolean contradictionCaught = false;

        Expression conjunction = new Conjunction(
                Arrays.asList(
                        new Literal("A"),
                        new Literal("B")
                )
        );
        Expression negation = new Negation(new Literal("B"));

        try {
            List<Expression> resolutions = conjunction.resolution(negation);
        } catch (Contradiction e) {
            contradictionCaught = true;
        }
        Assert.assertTrue("Contradiction not caught", true);
    }
}

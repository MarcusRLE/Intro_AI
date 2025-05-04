package com.logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ContractionTest {

    @Test
    public void test1(){
        Expression literalA = new Literal("A");
        Expression literalA2 = new Literal("A");

        boolean implies = literalA2.implies(literalA);
        Assert.assertTrue(implies);
    }

    @Test
    public void test2(){
        Expression literalA = new Literal("A");
        Expression literalB = new Literal("B");

        boolean implies = literalA.implies(literalB);

        Assert.assertFalse(implies);
    }

    @Test
    public void test3(){
        Expression literalA = new Literal("A");
        Expression conjunction = new Conjunction(
                Arrays.asList(
                        new Literal("A"),
                        new Literal("B")
                )
        );

        boolean implies = conjunction.implies(literalA);

        Assert.assertTrue(implies);
    }

    @Test
    public void test4(){
        Expression literalA = new Literal("A");
        Expression conjunction = new Disjunction(
                Arrays.asList(
                        new Literal("A"),
                        new Literal("B")
                )
        );

        boolean implies = conjunction.implies(literalA);

        Assert.assertTrue(implies);
    }

    @Test
    public void test5(){
        Expression literalA = new Literal("A");
        Expression implication = new Implication(
            new Literal("A"),
            new Literal("B")
        );

        boolean implies = implication.implies(literalA);

        Assert.assertTrue(implies);
    }

    @Test
    public void test6(){
        Expression conjunction = new Conjunction(
                Arrays.asList(
                        new Literal("A"),
                        new Literal("B")
                )
        );

        Expression disjunction = new Disjunction(
                Arrays.asList(
                        new Conjunction(
                                Arrays.asList(
                                        new Literal("B"),
                                        new Literal("A")
                                )
                        ), new Literal("C")
                )
        );

        boolean implies = disjunction.implies(conjunction);

        Assert.assertTrue(implies);
    }

    @Test
    public void test7(){
        Expression literalA = new Literal("A");
        Expression negationA = new Negation(new Literal("A"));

        boolean implies = negationA.implies(literalA);
        Assert.assertFalse(implies);
    }

    @Test
    public void test8(){
        Expression literalA = new Literal("A");
        Expression nestedNeg = new Negation(new Negation(new Literal("A")));

        boolean implies = nestedNeg.implies(literalA);
        Assert.assertTrue(implies);
    }

    @Test
    public void test9(){
        Expression literalA = new Literal("A");

        Expression negConj = new Negation(new Conjunction(
                Arrays.asList(
                        new Literal("A"),
                        new Literal("B")
                )
        ));

        boolean implies = negConj.implies(literalA);
        Assert.assertTrue(implies);
    }
}

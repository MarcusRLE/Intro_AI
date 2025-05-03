package com.logic;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;


public class EqualityTest {
    @Test
    public void test1(){
        Expression disjunction = new Disjunction(
                Arrays.asList(new Negation(
                        new Literal("A")),
                new Literal("B")));

        Expression implication = new Implication(
                new Literal("A"),
                new Literal("B"));

        assertTrue(util.falseEquality(disjunction, implication), implication.isEqual(disjunction));
    }

    @Test
    public void test2(){
        Expression nestedConj = new Disjunction(
                Arrays.asList(new Conjunction(
                        Arrays.asList(new Literal("A"),
                        new Literal("B"))),
                new Literal("C")));

        Expression doubleConj = new Conjunction(
                Arrays.asList(new Disjunction(
                        Arrays.asList(new Literal("C"),
                        new Literal("A"))),
                new Disjunction(
                        Arrays.asList(new Literal("C"),
                        new Literal("B")))));

        assertTrue(util.falseEquality(nestedConj,doubleConj), nestedConj.isEqual(doubleConj));
    }

    @Test
    public void test3(){
        Expression disjunction1 = new Disjunction(
                Arrays.asList(new Disjunction(
                        Arrays.asList(new Literal("A"),
                        new Literal("B"))),
                new Literal("C")));

        Expression disjunction2 = new Disjunction(
                Arrays.asList(new Disjunction(
                        Arrays.asList(new Literal("C"),
                        new Literal("B"))),
                new Literal("A")));

        assertTrue(util.falseEquality(disjunction1, disjunction2), disjunction1.isEqual(disjunction2));
    }
    
}

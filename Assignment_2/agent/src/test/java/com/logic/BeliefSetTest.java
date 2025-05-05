package com.logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class BeliefSetTest {

    @Test
    public void test1() {
        BeliefSet beliefSet = new BeliefSetImpl();

        Expression newBelief = new Disjunction(
            List.of(new Literal("A"), new Literal("B"))
        );
        ArrayList<Expression> expected = new ArrayList<>(List.of(newBelief));
        beliefSet.expansion(newBelief);
        Assert.assertTrue("List not same content", util.sameContent(expected, beliefSet.getBeliefs()));

        newBelief = new Literal("A");
        beliefSet.expansion(newBelief);
        expected.add(newBelief);
        Assert.assertTrue("List not same content", util.sameContent(expected, beliefSet.getBeliefs()));

        newBelief = new Negation(new Literal("A"));
        beliefSet.expansion(newBelief);
        Assert.assertTrue("List not same content", util.sameContent(expected, beliefSet.getBeliefs()));

        newBelief = new Implication(
                new Literal("A"),
                new Literal("C")
        );
        beliefSet.expansion(newBelief);
        expected.add(newBelief);
        expected.add(new Literal("C"));
        Assert.assertTrue("List not same content", util.sameContent(expected, beliefSet.getBeliefs()));
    }

    @Test
    public void test2() {
        BeliefSet beliefSet = new BeliefSetImpl();

        ArrayList<Expression> expected = new ArrayList<>(List.of());
        beliefSet.expansion(new Implication(new Literal("A"),new Literal("B")));
        beliefSet.expansion(new Conjunction(List.of(new Literal("A"),new Literal("B"))));
        // Assert.assertTrue("List not same content", util.sameContent(expected, beliefSet.getBeliefs()));

        for (Expression exp: expected) {System.err.println("from test1 in BeliefSetTest (1): " + exp.toString(false));};
        for (Expression exp: beliefSet.getBeliefs()) {System.err.println("from test1 in BeliefSetTest (2): " + exp.toString(false));};
    }
}

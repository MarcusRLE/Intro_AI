package com.logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class BeliefSetTest {

    @Test
    public void test1() {
        BeliefSet beliefSet = new BeliefSet();

        Expression newBelief = new Disjunction(
            List.of(new Literal("A"), new Literal("B"))
        );
        ArrayList<Expression> expected = new ArrayList<>(List.of(newBelief));
        beliefSet.addBelief(newBelief, true);
        // for (Expression exp: expected) {System.err.println("from test1 in BeliefSetTest (1): " + exp.toString(false));};
        // for (Expression exp: beliefSet.getBeliefs()) {System.err.println("from test1 in BeliefSetTest (2): " + exp.toString(false));};
        Assert.assertTrue("List not same content", util.sameContent(expected, beliefSet.getBeliefs()));

        newBelief = new Literal("A");
        beliefSet.addBelief(newBelief, true);
        expected.add(newBelief);
        Assert.assertTrue("List not same content", util.sameContent(expected, beliefSet.getBeliefs()));

        newBelief = new Negation(new Literal("A"));
        beliefSet.addBelief(newBelief, true);
        Assert.assertTrue("List not same content", util.sameContent(expected, beliefSet.getBeliefs()));

        newBelief = new Implication(
                new Literal("A"),
                new Literal("C")
        );
        beliefSet.addBelief(newBelief, true);
        expected.add(newBelief);
        expected.add(new Literal("C"));
        for (Expression exp: expected) {System.err.println("from test1 in BeliefSetTest (expected): " + exp.toString(false));};
        for (Expression exp: beliefSet.getBeliefs()) {System.err.println("from test1 in BeliefSetTest (beliefset): " + exp.toString(false));};
        Assert.assertTrue("List not same content", util.sameContent(expected, beliefSet.getBeliefs()));

    }
}

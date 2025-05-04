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
        List<Expression> expected = List.of(newBelief);

        List<Expression> logicalEntailments = new ArrayList<>();

        try {
            logicalEntailments = beliefSet.logicalEntailment(Arrays.asList(newBelief), logicalEntailments);
            for (Expression exp : logicalEntailments) { System.out.println("from test1 in BeliefSetTest: " + exp.toString(false)); }
            Assert.assertTrue("List not same content", util.sameContent(expected, logicalEntailments));
        } catch (Contradiction c) { Assert.assertTrue(false); }

        // newBelief = new Literal("A");
        // expected = List.of(newBelief);
        //
        // try {
        //     logicalEntailments = beliefSet.logicalEntailment(Arrays.asList(newBelief), logicalEntailments);
        //     System.out.println("test");
        //     for (Expression exp : logicalEntailments) { System.out.println("from test1 in BeliefSetTest: " + exp.toString(false)); }
        //     Assert.assertTrue("List not same content", util.sameContent(expected, logicalEntailments));
        // } catch (Contradiction c) { Assert.assertTrue(false); }
    }
}

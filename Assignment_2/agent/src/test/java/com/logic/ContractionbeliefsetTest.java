package com.logic;

import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList; 
import java.util.List;
import java.util.Arrays;

public class ContractionbeliefsetTest {

    @Test
    public void test1() {
        BeliefSet beliefSet = new BeliefSet();
        Expression literalA = new Literal("A");
        Expression literalB = new Literal("B");
    
        beliefSet.addBelief(literalA, false);
        beliefSet.addBelief(literalB, false);
    
        BeliefSet conBeliefSet = beliefSet.contraction(literalA);
    
        Assert.assertFalse(conBeliefSet.getBeliefs().contains(literalA));
        Assert.assertTrue(conBeliefSet.getBeliefs().contains(literalB));
        Assert.assertEquals(1, conBeliefSet.getBeliefs().size());

    }
    @Test
    public void test2(){
        BeliefSet beliefSet = new BeliefSet();
        Expression literalA = new Literal("A");
        Expression literalB = new Literal("B");
        Expression implication = new Implication(literalA, literalB);
    
        beliefSet.addBelief(literalA, false);
        beliefSet.addBelief(implication, false);
    
        BeliefSet contractd = beliefSet.contraction(literalA);

        
    
        Assert.assertFalse(contractd.getBeliefs().contains(literalA));
        Assert.assertFalse(contractd.getBeliefs().contains(implication));
        Assert.assertTrue(contractd.getBeliefs().isEmpty());
        
    }
    @Test
    public void test3(){
        BeliefSet beliefSet = new BeliefSet();
        Expression literalA = new Literal("A");
        Expression negation = new Negation(literalA);

        beliefSet.addBelief(literalA, false);
        beliefSet.addBelief(negation, false);

        BeliefSet contractd = beliefSet.contraction(literalA);

        Assert.assertTrue(contractd.getBeliefs().isEmpty());
}


    @Test
    public void test4(){
        BeliefSet beliefSet = new BeliefSet();
        Expression literalA = new Literal("A");
        Expression literalB = new Literal("B");
        Expression conjunction = new Conjunction(Arrays.asList(literalA, literalB));
    
        beliefSet.addBelief(conjunction, false);
    
        BeliefSet contractd = beliefSet.contraction(literalA);
    
        Assert.assertTrue(contractd.getBeliefs().contains(conjunction));
        Assert.assertEquals(1, contractd.getBeliefs().size());

    }

    @Test
    public void test5(){
        BeliefSet beliefSet = new BeliefSet();
        Expression literalA = new Literal("A");
        Expression literalB = new Literal("B");
        Expression disjunction = new Disjunction(Arrays.asList(literalA, literalB));

        beliefSet.addBelief(disjunction, false);

        BeliefSet contractd = beliefSet.contraction(literalA);
    
        // Expect disjunction to remain, as it does not imply literalA
        Assert.assertTrue(contractd.getBeliefs().contains(disjunction));
        Assert.assertEquals(1, contractd.getBeliefs().size());

    }

    @Test
    public void test6(){
        BeliefSet beliefSet = new BeliefSet();
        Expression literalA = new Literal("A");
        Expression literalB = new Literal("B");
        Expression literalC = new Literal("C");
        Expression conjunction = new Conjunction(Arrays.asList(literalA, literalB));
        Expression disjunction = new Disjunction(Arrays.asList(conjunction, literalC));
    
        beliefSet.addBelief(conjunction, false);
        beliefSet.addBelief(disjunction, false);
    
        BeliefSet contractd = beliefSet.contraction(literalA);
    
        // Expect both conjunction and disjunction to remain
        Assert.assertTrue(contractd.getBeliefs().contains(conjunction));
        Assert.assertTrue(contractd.getBeliefs().contains(disjunction));
        Assert.assertEquals(2, contractd.getBeliefs().size());

    }

    
}

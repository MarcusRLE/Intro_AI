package com.logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class AmgTest {
    
    @Test
    public void ContractionSuccess() {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSet();
        beliefSet.addBelief(new Literal("A"), false);
        beliefSet.addBelief(new Literal("B"), false);
        beliefSet.addBelief(new Implication(new Literal("B"), new Literal("P")), false);

        // Belief to be contracted
        Expression beliefToContract = new Literal("P");

        // Contract the belief
        BeliefSet contractedBeliefSet = beliefSet.contraction(beliefToContract);

        // Convert to CNF
        contractedBeliefSet.convertToCNF();

        // Assert that the belief set does not contain the contracted belief
        Assert.assertFalse("Belief set should not contain the contracted belief", contractedBeliefSet.contains(beliefToContract));
    }

    @Test 
    public void RevisionSuccess() {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSet();
        beliefSet.addBelief(new Literal("A"), false);
        beliefSet.addBelief(new Literal("B"), false);
        beliefSet.addBelief(new Implication(new Literal("B"), new Negation(new Literal("P"))), false);

        // Belief to be revised
        Expression beliefToRevise = new Literal("P");

        // Revise the belief using Levi identity
        BeliefSet revisedBeliefSet = beliefSet.contraction(new Negation(beliefToRevise));
        revisedBeliefSet.addBelief(beliefToRevise, false);

        // Assert that the belief set contains the revised belief
        Assert.assertTrue("Belief set should contain the revised belief", revisedBeliefSet.contains(beliefToRevise));
    }

    @Test
    public void ContractionInclusion() {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSet();
        beliefSet.addBelief(new Literal("A"), false);
        beliefSet.addBelief(new Literal("B"), false);
        beliefSet.addBelief(new Implication(new Literal("B"), new Literal("P")), false);

        // Belief to be contracted
        Expression beliefToContract = new Literal("P");

        // Contract the belief
        BeliefSet contractedBeliefSet = beliefSet.contraction(beliefToContract);

        // Contains the contracted belief set
        boolean containsContractedBeliefSet = true;
        for (Expression belief : contractedBeliefSet.getBeliefs()) {
            if (!beliefSet.contains(belief)) {
                containsContractedBeliefSet = false;
                break;
            }
        }

        // Assert that the contracted belief set is included in the original belief set
        Assert.assertTrue("Contracted belief set should be included in the original belief set", containsContractedBeliefSet);
    }

    @Test 
    public void RevisionInclusion() {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSet();
        beliefSet.addBelief(new Literal("A"), false);
        beliefSet.addBelief(new Literal("B"), false);
        beliefSet.addBelief(new Implication(new Literal("B"), new Negation(new Literal("P"))), false);

        // Belief to be revised
        Expression beliefToRevise = new Literal("P");

        // Revise the belief using Levi identity
        BeliefSet revisedBeliefSet = beliefSet.contraction(new Negation(beliefToRevise));
        revisedBeliefSet.addBelief(beliefToRevise, false);

        // Add the revised belief to the original belief set
        beliefSet.addBelief(beliefToRevise, false);

        // Contains the revised belief set
        boolean containsRevisedBeliefSet = true;
        for (Expression belief : revisedBeliefSet.getBeliefs()) {
            if (!beliefSet.contains(belief)) {
                containsRevisedBeliefSet = false;
                break;
            }
        }

        // Assert that the revised belief set is included in the original belief set
        Assert.assertTrue("Revised belief set should be included in the original belief set", containsRevisedBeliefSet);

    }

    @Test
    public void ContractionVacuity() {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSet();
        beliefSet.addBelief(new Literal("A"), false);
        beliefSet.addBelief(new Literal("B"), false);
        beliefSet.addBelief(new Implication(new Literal("B"), new Negation(new Literal("P"))), false);

        // Belief to be contracted not in the CNF belief set
        Expression beliefToContract = new Literal("P");

        // Convert to CNF
        BeliefSet cnfBeliefSet = new BeliefSet();
        cnfBeliefSet.setBeliefs(new ArrayList<>(beliefSet.getBeliefs()));
        cnfBeliefSet.convertToCNF();

        // Contract the belief
        BeliefSet contractedBeliefSet = beliefSet.contraction(beliefToContract);

        // Assert that the CNF belief set does not contain the belief to be contracted
        Assert.assertFalse("CNF belief set should not contain the belief to be contracted", cnfBeliefSet.contains(beliefToContract));

        // Assert that the belief set contains the same beliefs after contraction
        Assert.assertTrue("The belief set  should contain the same beliefs after contraction", util.sameContent(beliefSet.getBeliefs(),contractedBeliefSet.getBeliefs()));
    }

    @Test
    public void RevisionVacuity() {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSet();
        beliefSet.addBelief(new Literal("A"), false);
        beliefSet.addBelief(new Literal("B"), false);
        beliefSet.addBelief(new Implication(new Literal("B"), new Literal("P")), false);

        // Belief to be revised
        Expression beliefToRevise = new Literal("P");

        // Revise the belief using Levi identity
        BeliefSet revisedBeliefSet = beliefSet.contraction(new Negation(beliefToRevise));
        revisedBeliefSet.addBelief(beliefToRevise, false);

        // Expand the belief set to include the revised belief
        BeliefSet expandedBeliefSet = new BeliefSet();
        expandedBeliefSet.setBeliefs(new ArrayList<>(beliefSet.getBeliefs()));
        expandedBeliefSet.addBelief(beliefToRevise, false);

        // Assert that the belief set does not contain the negated belief to be revised
        Assert.assertFalse("CNF belief set should not contain the belief to be contracted", beliefSet.contains(beliefToRevise));

        // Assert that the revised belief set contains the same beliefs as the expanded belief set
        Assert.assertTrue("The belief set  should contain the same beliefs after contraction", util.sameContent(revisedBeliefSet.getBeliefs(),expandedBeliefSet.getBeliefs()));
    }

    @Test
    public void RevisionConsistency() {

    }

    @Test
    public void ContractionExtensionality() {

    }

    @Test
    public void RevisionExtensionality() {

    }
}

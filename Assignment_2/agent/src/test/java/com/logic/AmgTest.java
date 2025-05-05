package com.logic;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

public class AmgTest {
    
    @Test
    public void ContractionSuccess() throws Contradiction {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSetImpl();
        beliefSet.expansion(new Literal("A"));
        beliefSet.expansion(new Literal("B"));
        beliefSet.expansion(new Implication(new Literal("B"), new Literal("P")));

        // Belief to be contracted
        Expression beliefToContract = new Literal("P");

        // Contract the belief
        BeliefSet contractedBeliefSet = new BeliefSetImpl(beliefSet.contraction(beliefToContract));

        // Get CN
        contractedBeliefSet.setBeliefs(contractedBeliefSet.CN());

        // Assert that the belief set does not contain the contracted belief
        Assert.assertFalse("Belief set should not contain the contracted belief", contractedBeliefSet.contains(beliefToContract));
    }

    @Test 
    public void RevisionSuccess() {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSetImpl();
        beliefSet.expansion(new Literal("A"));
        beliefSet.expansion(new Literal("B"));
        beliefSet.expansion(new Implication(new Literal("B"), new Negation(new Literal("P"))));

        // Belief to be revised
        Expression beliefToRevise = new Literal("P");

        // Revise the belief set
        BeliefSet revisedBeliefSet = new BeliefSetImpl(beliefSet.getBeliefs());
        revisedBeliefSet.revision(beliefToRevise);

        // Assert that the belief set contains the revised belief
        Assert.assertTrue("Belief set should contain the revised belief", revisedBeliefSet.contains(beliefToRevise));
    }

    @Test
    public void ContractionInclusion() {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSetImpl();
        beliefSet.expansion(new Literal("A"));
        beliefSet.expansion(new Literal("B"));
        beliefSet.expansion(new Implication(new Literal("B"), new Literal("P")));

        // Belief to be contracted
        Expression beliefToContract = new Literal("P");

        // Contract the belief
        BeliefSet contractedBeliefSet = new BeliefSetImpl(beliefSet.contraction(beliefToContract));

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
        BeliefSet beliefSet = new BeliefSetImpl();
        beliefSet.expansion(new Literal("A"));
        beliefSet.expansion(new Literal("B"));
        beliefSet.expansion(new Implication(new Literal("B"), new Negation(new Literal("P"))));

        // Belief to be revised
        Expression beliefToRevise = new Literal("P");

        // Revise the belief set
        BeliefSet revisedBeliefSet = new BeliefSetImpl(beliefSet.getBeliefs());
        revisedBeliefSet.revision(beliefToRevise);

        // Add the revised belief to the original belief set
        beliefSet.expansion(beliefToRevise);

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
    public void ContractionVacuity() throws Contradiction {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSetImpl();
        beliefSet.expansion(new Literal("A"));
        beliefSet.expansion(new Literal("B"));
        beliefSet.expansion(new Implication(new Literal("B"), new Negation(new Literal("P"))));

        // Belief to be contracted not in the CN belief set
        Expression beliefToContract = new Literal("P");

        // Convert to CN
        BeliefSet cnBeliefSet = new BeliefSetImpl();
        cnBeliefSet.setBeliefs(new ArrayList<>(beliefSet.CN()));

        // Contract the belief
        BeliefSet contractedBeliefSet = new BeliefSetImpl(beliefSet.contraction(beliefToContract));

        // Assert that the CN belief set does not contain the belief to be contracted
        Assert.assertFalse("CN belief set should not contain the belief to be contracted", cnBeliefSet.contains(beliefToContract));

        // Assert that the belief set contains the same beliefs after contraction
        Assert.assertTrue("The belief set  should contain the same beliefs after contraction", util.sameContent(beliefSet.getBeliefs(),contractedBeliefSet.getBeliefs()));
    }

    @Test
    public void RevisionVacuity() {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSetImpl();
        beliefSet.expansion(new Literal("A"));
        beliefSet.expansion(new Literal("B"));
        beliefSet.expansion(new Implication(new Literal("B"), new Literal("P")));

        // Belief to be revised
        Expression beliefToRevise = new Literal("P");

        // Revise the belief set
        BeliefSet revisedBeliefSet = new BeliefSetImpl(beliefSet.getBeliefs());
        revisedBeliefSet.revision(beliefToRevise);

        // Expand the belief set to include the revised belief
        BeliefSet expandedBeliefSet = new BeliefSetImpl();
        expandedBeliefSet.setBeliefs(new ArrayList<>(beliefSet.getBeliefs()));
        expandedBeliefSet.expansion(beliefToRevise);

        // Assert that the belief set does not contain the negated belief to be revised
        Assert.assertFalse("belief set should not contain the belief to be revision", beliefSet.contains(new Negation(beliefToRevise)));

        // Assert that the revised belief set contains the same beliefs as the expanded belief set
        Assert.assertTrue("The belief set should contain the same beliefs after revision", util.sameContent(revisedBeliefSet.getBeliefs(),expandedBeliefSet.getBeliefs()));
    }

    @Test
    public void RevisionConsistency() {
        // Set up the initial belief set
        BeliefSet beliefSet = new BeliefSetImpl();
        beliefSet.expansion(new Literal("A"));
        beliefSet.expansion(new Literal("B"));
        beliefSet.expansion(new Implication(new Literal("B"), new Negation(new Literal("P"))));

        // Belief to be revised
        Expression beliefToRevise = new Literal("P");

        // Revise the belief set
        BeliefSet revisedBeliefSet = new BeliefSetImpl(beliefSet.getBeliefs());
        revisedBeliefSet.revision(beliefToRevise);

        // Assert that the revised belief is consistent
        Assert.assertTrue("Revised belief should be consistent", beliefToRevise.isConsistent());

        // Assert that the revised belief set is consistent
        Assert.assertTrue("Revised belief set should be consistent", revisedBeliefSet.isConsistent());
    }

    @Test
    public void ContractionExtensionality() {
        // Set up the initial belief sets
        BeliefSet beliefSet1 = new BeliefSetImpl();
        beliefSet1.expansion(new Literal("A"));
        beliefSet1.expansion(new Literal("B"));
        beliefSet1.expansion(new Implication(new Literal("B"), new Literal("P")));
        BeliefSet beliefSet2 = new BeliefSetImpl();
        beliefSet2.setBeliefs(new ArrayList<>(beliefSet1.getBeliefs()));

        // Belief to be contracted
        Expression beliefToContract = new Literal("P");

        // Contract the belief
        BeliefSet firstContractedBeliefSet = new BeliefSetImpl(beliefSet1.contraction(beliefToContract));
        BeliefSet secondContractedBeliefSet = new BeliefSetImpl(beliefSet2.contraction(beliefToContract));

        // Assert that the contracted belief sets contains the same beliefs
        Assert.assertTrue("The belief set should contain the same beliefs after contraction", util.sameContent(firstContractedBeliefSet.getBeliefs(),secondContractedBeliefSet.getBeliefs()));

    }

    @Test
    public void RevisionExtensionality() {
        // Set up the initial belief sets
        BeliefSet beliefSet1 = new BeliefSetImpl();
        beliefSet1.expansion(new Literal("A"));
        beliefSet1.expansion(new Literal("B"));
        beliefSet1.expansion(new Implication(new Literal("B"), new Negation(new Literal("P"))));
        BeliefSet beliefSet2 = new BeliefSetImpl();
        beliefSet2.setBeliefs(new ArrayList<>(beliefSet1.getBeliefs()));

        // Belief to be revised
        Expression beliefToRevise = new Literal("P");

        // Revise the belief set
        BeliefSet firstRevisedBeliefSet = new BeliefSetImpl(beliefSet1.getBeliefs());
        firstRevisedBeliefSet.revision(beliefToRevise);
        BeliefSet secondRevisedBeliefSet = new BeliefSetImpl(beliefSet2.getBeliefs());
        secondRevisedBeliefSet.revision(beliefToRevise);

        // Assert that the revised belief sets contains the same beliefs
        Assert.assertTrue("The belief set should contain the same beliefs after revision", util.sameContent(firstRevisedBeliefSet.getBeliefs(),secondRevisedBeliefSet.getBeliefs()));
    }
}

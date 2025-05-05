package com.logic.controller;

import com.logic.*;
import com.logic.model.BeliefType;

import java.util.ArrayList;
import java.util.List;

public class BeliefController {
    BeliefSet beliefs;
    Expression currentNewBelief;
    Expression currentTerm;
    Expression contradiction;
    boolean currentHasMultipleTerms;
    boolean currentTermIsLiteral;
    boolean buildingNewBelief;
    boolean exitProgram = false;
    boolean abortBuilder = false;

    public BeliefController() {
        beliefs = new BeliefSetImpl();
    }

    public void addNewBelief(Expression exp) throws Contradiction {
        try {
            beliefs.revision(exp);
            beliefs.expansion(exp);
            beliefs.setBeliefs(beliefs.CN());
        } catch (Contradiction c) {
            throw c;
        }
    }

    public void setCurrentNewBelief(BeliefType belief) {
        currentNewBelief = belief.getNullExp().copy();
        buildingNewBelief = true;
        currentTerm = currentNewBelief;
        currentHasMultipleTerms = currentTerm instanceof MultipleTermed;
        currentTermIsLiteral = currentTerm instanceof Literal;
    }

    public void setCurrentTerm(BeliefType belief) {
        currentTerm = belief.getNullExp().copy();
        currentHasMultipleTerms = currentTerm instanceof MultipleTermed;
        currentTermIsLiteral = currentTerm instanceof Literal;
    }

    public void setSizeOfBelief(int size){
        if (currentHasMultipleTerms){
            List<Expression> expressions = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                expressions.add(null);
            }
            ((MultipleTermed) currentTerm).setExpressions(expressions);
        }
    }


    public void setLiteralTerm(String name) {
        if(currentTermIsLiteral){
            ((Literal) currentTerm).setName(name);
            currentTermIsLiteral = false;
        }
    }

    public boolean isComplete(){
        buildingNewBelief = currentNewBelief.hasEmptyTerm();
        return !buildingNewBelief;
    }

    public boolean currentHasMultipleTerms(){
        return currentHasMultipleTerms;
    }

    public Expression getCurrentNewBelief() {
        return currentNewBelief;
    }

    public void defineNextTerm() {
        currentNewBelief.setNextTerm(currentTerm);
    }

    public boolean currentTermIsLiteral(){
        return currentTermIsLiteral;
    }

    public BeliefSet getBeliefs() {
        return beliefs;
    }

    public boolean getExitProgram() {
        return exitProgram;
    }

    public void setExitProgram(boolean exitProgram) {
        this.exitProgram = exitProgram;
    }

    public boolean hasContradiction(Expression exp){
        BeliefSet copy = new BeliefSetImpl(beliefs.getBeliefs());
        try {
            copy.expansion(exp);
            copy.CN();
        } catch (Contradiction c) {
            contradiction = c.getContradictingConclusion();
            return true;
        }
        return false;
    }

    public List<Expression> logicalConclusion(Expression exp) {
        List<Expression> conclusions;
        try {
            conclusions = beliefs.logicalEntailmentFromOneBelief(exp);
        } catch (Contradiction c) {
            contradiction = c.getContradictingConclusion();
            return c.getConclusions();
        }
        return conclusions;
    }

    public Expression getContradiction() {
        return contradiction;
    }

    public void setAbortBuilder(boolean abortBuilder) {
        this.abortBuilder = abortBuilder;
    }

    public boolean getAbortBuilder() {
        return abortBuilder;
    }
}

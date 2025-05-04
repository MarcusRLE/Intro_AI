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
    boolean currentNewBeliefComplete;
    boolean currentTermIsLiteral;
    boolean buildingNewBelief;
    boolean exitProgram = false;

    public BeliefController() {
        beliefs = new BeliefSet();
    }

    public void addNewBelief(Expression newBelief) {
        beliefs.addBelief(newBelief, false);
    }

    public void setCurrentNewBelief(BeliefType belief) {
        switch (belief) {
            case LITERAL:
                currentNewBelief = new Literal(null);
                break;
            case NEGATION:
                currentNewBelief = new Negation(null);
                break;
            case CONJUNCTION:
                currentNewBelief = new Conjunction(null);
                break;
            case DISJUNCTION:
                currentNewBelief = new Disjunction(null);
                break;
            case IMPLICATION:
                currentNewBelief = new Implication(null, null);
                break;
        }
        buildingNewBelief = true;
        currentTerm = currentNewBelief;
        currentHasMultipleTerms = currentTerm instanceof MultipleTermed;
        currentTermIsLiteral = currentTerm instanceof Literal;
    }

    public void setCurrentTerm(BeliefType belief) {
        switch (belief) {
            case LITERAL:
                currentTerm = new Literal(null);
                break;
            case NEGATION:
                currentTerm = new Negation(null);
                break;
            case CONJUNCTION:
                currentTerm = new Conjunction(null);
                break;
            case DISJUNCTION:
                currentTerm = new Disjunction(null);
                break;
            case IMPLICATION:
                currentTerm = new Implication(null, null);
                break;
        }
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
        List<Expression> expressions = new ArrayList<>();
        try {
            beliefs.logicalEntailment(List.of(exp), expressions);
        } catch (Contradiction contradiction) {
            return true;
        }
        return false;
    }

    public List<Expression> logicalConclusion(Expression exp) {
        List<Expression> expressions = new ArrayList<>();
        try {
            expressions = beliefs.logicalEntailment(List.of(exp), expressions);
        } catch (Contradiction c) {
            contradiction = c.getContradictingConclusion();
            return c.getConclusions();
        }
        return expressions;
    }

    public Expression getContradiction() {
        return contradiction;
    }
}

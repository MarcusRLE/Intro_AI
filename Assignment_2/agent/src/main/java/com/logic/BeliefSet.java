package com.logic;

import java.util.ArrayList;
import java.util.List;

public class BeliefSet {
    private List<Expression> beliefs;

    public BeliefSet() {
        this.beliefs = new ArrayList<>();
    }

    public BeliefSet contraction(){
        BeliefSet contractedBeliefSet = new BeliefSet();

        // TODO: (Benjamin) Implementér kode her - jeg foreslår at bruge "implies()" funkitonen fra Expression interface

        return contractedBeliefSet;
    }

    public void convertToCNF(){
        // Copy the beliefs to a new list
        List<Expression> oldBeliefs = new ArrayList<>(beliefs);
        // Clear the current beliefs
        beliefs.clear();
        for (Expression belief : oldBeliefs) {
            belief = belief.CNF();
            addBelief(belief, false);
        }
    }

    public void setBeliefs(List<Expression> beliefs) {
        this.beliefs = beliefs;
    }

    public boolean contains(Expression Exp) {
        for (Expression belief : beliefs) {
            if (belief.isEqual(Exp)) {
                return true;
            }
        }
        return false;
    }

    public void addBelief(Expression belief, boolean convertToCNF) {
        if (convertToCNF) {
            belief = belief.CNF();
        }
        if (!this.contains(belief)) {
            beliefs.add(belief);   
        }
        // ERROR HANDLING FOR CONTRADICTION
    }

    public void addBeliefsWithConclusion(Expression exp){
        BeliefSet conclusions = this.logicalConclusion(exp);
        for (Expression conclusion : conclusions.getBeliefs()){
            beliefs.add(conclusion);
        }
    }

    public void removeBelief(Expression belief) {
        beliefs.remove(belief);
    }

    public BeliefSet logicalConclusion(Expression exp) {
        BeliefSet conclusions = new BeliefSet();
        for (Expression belief : beliefs) {
            List<Expression> currentConclusions = belief.resolution(exp);
            for (Expression conclusion: currentConclusions) {
                conclusions.addBelief(conclusion, false);
            }
        }

        if(conclusions.beliefs.isEmpty()){
            conclusions.addBelief(exp, false);
            return conclusions;
        } else {
            BeliefSet nextConclusions = new BeliefSet();
            for (Expression conclusion : conclusions.beliefs){
                BeliefSet currentConclusions = this.logicalConclusion(conclusion);
                for (Expression currentConclusion: currentConclusions.beliefs){
                    nextConclusions.addBelief(currentConclusion, false);
                }
            }
            for (Expression conclusion : nextConclusions.beliefs){
                conclusions.addBelief(conclusion, false);
            }
        }

        return conclusions;
    }

    public List<Expression> getBeliefs() {
        return beliefs;
    }

    public String toString() {
        String result = "{";
        for (int i = 0; i < beliefs.size(); i++) {
            result += beliefs.get(i).toString(false);
            if (i < beliefs.size() - 1) {
                result += ", ";
            }
        }
        result += "}";
        return result;
    }
} 
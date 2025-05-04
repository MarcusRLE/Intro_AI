package com.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BeliefSet {
    private List<Expression> beliefs;

    public BeliefSet() {
        this.beliefs = new ArrayList<>();
    }

    public BeliefSet(List<Expression> beliefs) {
        this.beliefs = beliefs;
    }

    public BeliefSet contraction(Expression exp) {
        BeliefSet contractedBeliefSet = new BeliefSet();

        // TODO: (Benjamin) Implementér kode her - jeg foreslår at bruge "implies()"
        // funkitonen fra Expression interface
        for (Expression belief : this.beliefs) {
            if (belief.implies(exp)) {
               contractedBeliefSet.removeBelief(belief);
            }

            contractedBeliefSet.addBelief(belief, false);
            


        }

        return contractedBeliefSet;
    }

    public void convertToCNF() {
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
            try {
                List<Expression> logicalEntailments = new ArrayList<>(beliefs);
                logicalEntailments = logicalEntailment(Arrays.asList(belief), logicalEntailments);
                beliefs.addAll(logicalEntailments);
                beliefs = beliefs.stream().distinct().collect(Collectors.toList());
            } catch (Contradiction e) {
                // Handle error
                System.err.println(
                        "Contradiction occured adding '" + belief.toString(false) + "' to belief set: " + toString());
            }
        }
    }

    public void addBeliefsWithConclusion(Expression exp) {
        try {
            BeliefSet conclusions = this.logicalConclusion(exp);
            for (Expression conclusion : conclusions.getBeliefs()) {
                beliefs.add(conclusion);
            }
        } catch (Contradiction e) {
            System.out.println(e);
        }
    }

    public void removeBelief(Expression belief) {
        beliefs.remove(belief);
    }

    public BeliefSet logicalConclusion(Expression exp) throws Contradiction {
        BeliefSet conclusions = new BeliefSet();
        for (Expression belief : beliefs) {
            List<Expression> currentConclusions = belief.resolution(exp);
            for (Expression conclusion : currentConclusions) {
                conclusions.addBelief(conclusion, false);
            }
        }

        if (conclusions.beliefs.isEmpty()) {
            conclusions.addBelief(exp, false);
            return conclusions;
        } else {
            BeliefSet nextConclusions = new BeliefSet();
            for (Expression conclusion : conclusions.beliefs) {
                BeliefSet currentConclusions = this.logicalConclusion(conclusion);
                for (Expression currentConclusion : currentConclusions.beliefs) {
                    nextConclusions.addBelief(currentConclusion, false);
                }
            }
            for (Expression conclusion : nextConclusions.beliefs) {
                conclusions.addBelief(conclusion, false);
            }
        }

        return conclusions;
    }

    public List<Expression> logicalEntailment(List<Expression> newBeliefs, List<Expression> currentEntailments)
            throws Contradiction {
        List<Expression> conclusions = new ArrayList<>();


        for (Expression newBelief : newBeliefs) {
            for (Expression current : currentEntailments) {
                try {
                    conclusions.addAll(current.resolution(newBelief));
                } catch (Contradiction c) {
                    conclusions.add(newBelief);
                    c.setContradictingConclusion(newBelief);
                    c.setConclusions(currentEntailments);
                    throw c;
                }
            }
        }

        for (Expression newBelief : newBeliefs) {
            currentEntailments.add(newBelief);
        }

        if (!conclusions.isEmpty()) {
            currentEntailments = logicalEntailment(conclusions, currentEntailments);
        }

        return currentEntailments;
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

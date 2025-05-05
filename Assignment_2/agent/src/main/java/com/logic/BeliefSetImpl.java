package com.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BeliefSetImpl implements BeliefSet {
    private List<Expression> beliefs;

    public BeliefSetImpl() {
        this.beliefs = new ArrayList<>();
    }

    public BeliefSetImpl(List<Expression> beliefs) {
        this.beliefs = beliefs;
    }

    @Override
    public void revision(Expression exp) {
        beliefs = this.contraction(new Negation(exp));
        expansion(exp);
    }

    public List<Expression> contraction(Expression exp) {
        List<Expression> expressions = new ArrayList<>(beliefs);

        return expressions.stream()
                .filter(e -> !e.implies(exp))
                .collect(Collectors.toList());
    }

    @Override
    public void expansion(Expression exp) {
        Expression expToCnf = exp.CNF();
        if (expToCnf instanceof Conjunction) {
            Conjunction conjunction = (Conjunction) expToCnf;
            for (Expression expression : conjunction.getExpressions()) {
                if(!this.contains(expression)) {
                    this.beliefs.add(expression);
                }
            }
        } else if (!this.contains(expToCnf)) {
            beliefs.add(expToCnf);
        }
    }

    @Override
    public List<Expression> CN() throws Contradiction {
        List<Expression> resolutions = new ArrayList<>();
        for (Expression exp : beliefs) {
            for (Expression exp2 : beliefs) {
                if (!exp.isEqual(exp2)) {
                    resolutions.addAll(exp.resolution(exp2));
                }
            }
        }
        resolutions = logicalEntailment(resolutions, new ArrayList<>(beliefs));
        return resolutions;
    }

    @Override
    public boolean isConsistent() {
        return false;
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
        throw new UnsupportedOperationException("Function addBelieft is deprecated - use 'expansion()' for simple adding of belief.");
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
        if(beliefs.isEmpty()) {
            result += "∅";
        } else {
            for (int i = 0; i < beliefs.size(); i++) {
                result += beliefs.get(i).toString(false);
                if (i < beliefs.size() - 1) {
                    result += ", ";
                }
            }
        }
        result += "}";
        return result;
    }
}

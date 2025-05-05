package com.logic;

import java.util.List;

public interface BeliefSet {
    void revision(Expression exp);
    BeliefSet contraction(Expression exp);
    void expansion(Expression exp);

    BeliefSet CN() throws Contradiction;
    boolean isConsistent();

    // Getter / Setter
    List<Expression> getBeliefs();
    void setBeliefs(List<Expression> beliefs);

    boolean contains(Expression Exp);
    List<Expression> logicalEntailment(List<Expression> newBeliefs, List<Expression> currentEntailments) throws Contradiction;

    // Redundant compared to 'expansion'
    void addBelief(Expression belief, boolean convertToCNF);


}

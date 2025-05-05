package com.logic;

import java.util.List;

public interface BeliefSet {
    void revision(Expression exp);
    List<Expression> contraction(Expression exp);
    void expansion(Expression exp);

    List<Expression> CN() throws Contradiction;
    boolean isConsistent();

    // Getter / Setter
    List<Expression> getBeliefs();
    void setBeliefs(List<Expression> beliefs);
    List<Expression> logicalEntailmentFromOneBelief(Expression newBelief) throws Contradiction;

    boolean contains(Expression Exp);
    List<Expression> logicalEntailment(List<Expression> newBeliefs, List<Expression> currentEntailments) throws Contradiction;



}

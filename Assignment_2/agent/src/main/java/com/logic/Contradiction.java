package com.logic;

import java.util.List;

public class Contradiction extends Exception {
    List<Expression> conclusions;
    Expression ContradictingConclusion;
    Contradiction(String errorMessage) {
        super(errorMessage);
    }

    public void setConclusions(List<Expression> conclusions) {
        this.conclusions = conclusions;
    }

    public List<Expression> getConclusions() {
        return conclusions;
    }

    public void setContradictingConclusion(Expression ContradictingConclusion) {
        this.ContradictingConclusion = ContradictingConclusion;
    }

    public Expression getContradictingConclusion() {
        return ContradictingConclusion;
    }
}

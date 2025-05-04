package com.logic;

import java.util.List;

public class Contradiction extends Exception {
    List<Expression> conclusions;
    Expression contradiction;
    Contradiction(String errorMessage) {
        super(errorMessage);
    }

    public void setConclusions(List<Expression> conclusions) {
        this.conclusions = conclusions;
    }

    public List<Expression> getConclusions() {
        return conclusions;
    }

    public void setContradictingConclusion(Expression contradictingConclusion) {
        this.contradiction = contradictingConclusion;
    }

    public Expression getContradictingConclusion() {
        return contradiction;
    }
}

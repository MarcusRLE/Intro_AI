package com.logic;

import java.util.List;

public class Literal implements Expression {
    protected String name;

    public Literal(String name) {
        this.name = name;
    }

    @Override
    public Expression CNF() {
        return this;
    }

    @Override
    public String toString(boolean withParentheses) {
        return name;
    }

    @Override
    public List<Expression> logicalConclusions(Expression other, List<Expression> logicalConclusions, int callIteration) {
        if(callIteration >= 2){
            return logicalConclusions;
        }
        return other.logicalConclusions(this, logicalConclusions, callIteration + 1);
    }
} 
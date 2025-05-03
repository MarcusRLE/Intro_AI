package com.logic;

import java.util.List;

public class Implication implements Expression {
    protected Expression left;
    protected Expression right;

    public Implication(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Expression CNF() {
        this.left = left.CNF();
        this.right = right.CNF();
        return new Disjunction(new Negation(left), right);
    }

    @Override
    public String toString(boolean withParentheses) {
        if (withParentheses) {
            return "(" + left.toString(true) + " => " + right.toString(true) + ")";
        } else {
            return left.toString(true) + " => " + right.toString(true);
        }
    }

    @Override
    public List<Expression> logicalConclusions(Expression other, List<Expression> logicalConclusions, int callIteration) {
        if (callIteration >= 2) {
            return logicalConclusions;
        }
        if(this.left.isEqual(other)){
            logicalConclusions.add(this.right);
        } else if(this.right.isEqual(new Negation(other))){
            logicalConclusions.add(new Negation(this.left));
        }
        logicalConclusions = other.logicalConclusions(this, logicalConclusions, callIteration + 1);
        return logicalConclusions;
    }
} 
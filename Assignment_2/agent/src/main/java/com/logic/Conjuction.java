package com.logic;

import java.util.List;

public class Conjuction implements Expression {
    protected Expression left;
    protected Expression right;

    public Conjuction(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString(boolean withParentheses) {
        return (withParentheses ? "(" : "") 
            + left.toString(!(left instanceof Conjuction)) 
            + " ∧ " 
            + right.toString(!(right instanceof Conjuction)) 
            + (withParentheses ? ")" : "");
    }

    @Override
    public List<Expression> logicalConclusions(Expression other, List<Expression> logicalConclusions, int callIteration) {
        if(callIteration >= 2){
            return logicalConclusions;
        }

        if(this.left.isEqual(this.right)){
            logicalConclusions.add(this.left);
            logicalConclusions = this.left.logicalConclusions(other, logicalConclusions, callIteration + 1);
            return logicalConclusions;
        }

        return other.logicalConclusions(this, logicalConclusions, callIteration + 1);
    }

    @Override
    public Expression CNF() {
        if(this.left.isEqual(this.right)){
            return this.left;
        }

        left = left.CNF();
        right = right.CNF();
        return this;
    }
} 
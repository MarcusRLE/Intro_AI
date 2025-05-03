package com.logic;

import java.util.Arrays;
import java.util.List;

public class Implication implements Expression {
    protected Expression left;
    protected Expression right;

    public Implication(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean implies(Expression exp) {
        return false;
    }

    @Override
    public boolean hasContradiction(Expression exp) {
        return false;
    }

    @Override
    public void sort() {

    }

    @Override
    public Expression CNF() {
        this.left = left.CNF();
        this.right = right.CNF();
        Expression cnf = new Disjunction(Arrays.asList(new Negation(left), right));

        return cnf.CNF();
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
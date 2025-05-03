package com.logic;

import java.util.List;

public class Negation implements Expression {
    protected Expression expression;

    public Negation(Expression expression) {
        this.expression = expression;
    }

    @Override
    public Expression CNF() {
        // Nested negation
        if (expression instanceof Negation) {
            Negation negation = (Negation) expression;
            return negation.expression.CNF();
        }

        this.expression = expression.CNF();
        if (expression instanceof Disjunction) {
            Disjunction disjunction = (Disjunction) expression;
            Expression left = new Negation(disjunction.left);
            left = left.CNF();
            Expression right = new Negation(disjunction.right);
            right = right.CNF();
            // Apply De Morgan's Law
            return new Conjunction(left, right);
        } else if (expression instanceof Conjunction) {
            Conjunction conjunction = (Conjunction) expression;
            Expression left = new Negation(conjunction.left);
            left = left.CNF();
            Expression right = new Negation(conjunction.right);
            right = right.CNF();
            // Apply De Morgan's Law
            return new Disjunction(left, right);
        } else return this;
    }

    @Override
    public List<Expression> logicalConclusions(Expression other, List<Expression> logicalConclusions, int callIteration) {
        if(callIteration >= 2){
            return logicalConclusions;
        }
        return other.logicalConclusions(this, logicalConclusions, callIteration + 1);
    }

    @Override
    public String toString(boolean withParentheses) {
        if (expression instanceof Literal) {
            return "¬" + expression.toString(false);
        } else {
            return "¬(" + expression.toString(false) + ")";
        }
    }
} 
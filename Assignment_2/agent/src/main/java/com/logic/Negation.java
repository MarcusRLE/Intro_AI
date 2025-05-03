package com.logic;

import java.util.ArrayList;
import java.util.List;

public class Negation implements Expression {
    protected Expression expression;

    public Negation(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void sort() {

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
            List<Expression> newExpressions = new ArrayList<>();
            for (Expression expression : disjunction.expressions) {
                Expression newExpression = new Negation(expression);
                newExpression = newExpression.CNF();
                newExpressions.add(newExpression);
            }
            // Apply De Morgan's Law
            return new Conjunction(newExpressions);
        } else if (expression instanceof Conjunction) {
            Conjunction conjunction = (Conjunction) expression;
            List<Expression> newExpressions = new ArrayList<>();
            for (Expression expression : conjunction.expressions) {
                Expression newExpression = new Negation(expression);
                newExpression = newExpression.CNF();
                newExpressions.add(newExpression);
            }
            // Apply De Morgan's Law
            return new Disjunction(newExpressions);
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
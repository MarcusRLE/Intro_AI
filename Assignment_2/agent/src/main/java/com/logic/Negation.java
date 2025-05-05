package com.logic;

import java.util.ArrayList;
import java.util.List;

public class Negation implements Expression {
    protected Expression expression;
    int weight;

    public Negation(Expression expression) {
        this.expression = expression;
        this.weight = randomWeight();
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public void setNextTerm(Expression nextTerm) {
        if(expression == null){
            expression = nextTerm;
        } else {
            if(expression.hasEmptyTerm()){
                expression.setNextTerm(nextTerm);
            } else {
                // Shouldn't happen
            }
        }
    }

    @Override
    public boolean hasEmptyTerm() {
        if(expression == null){
            return true;
        } else {
            return expression.hasEmptyTerm();
        }
    }

    @Override
    public boolean implies(Expression exp) {
        if (!this.expression.implies(exp)) {
            return true;
            
        }
        if (this.expression instanceof Conjunction) {
            Conjunction conjunction = (Conjunction) this.expression;
            for (Expression expression : conjunction.expressions) {
                if (!expression.implies(exp)) {
                    return true;
                }
            }
            
        }

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
    public List<Expression> resolution(Expression other) throws Contradiction {
        List<Expression> conclusions = new ArrayList<>();
        if (other instanceof Conjunction) {
            for (Expression exp: ((Conjunction)other).expressions) {
                if ((new Negation(exp)).isEqual(this)) {
                    throw new Contradiction("Contradiction");
                }
            }
        } else {
           if ((new Negation(other)).isEqual(this)) {
                throw new Contradiction("Contradiction");
            }
        }
        return conclusions;
    }


    @Override
    public String toString(boolean withParentheses) {
        String exprStr = expression != null ? expression.toString(withParentheses) : "[ EMPTY ]";
        if (expression instanceof Literal) {
            return "¬" + exprStr;
        } else {
            return "¬(" + exprStr + ")";
        }
    }

    @Override
    public boolean isConsistent() {
        return expression.isConsistent();
    }

    @Override
    public Expression copy() {
        Expression innerCopy;
        if (expression == null) {
            innerCopy = null;
        } else {
            innerCopy = expression.copy();
        }
        Expression copy = new Negation(innerCopy);
        copy.setWeight(weight);
        return copy;
    }
} 

package com.logic;

import java.util.List;

public class Disjunction implements Expression {
    protected Expression left;
    protected Expression right;

    public Disjunction(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public Expression CNF() {
        if(this.left.isEqual(this.right)){
            return this.left;
        }

        this.left = left.CNF();
        this.right = right.CNF();
        if (left instanceof Conjuction) {
            Conjuction conjuction = (Conjuction) left;
            return new Conjuction(new Disjunction(right, conjuction.left), new Disjunction(right, conjuction.right));
        } else if (right instanceof Conjuction) {
            Conjuction conjuction = (Conjuction) right;
            return new Conjuction(new Disjunction(left, conjuction.left), new Disjunction(left, conjuction.right));
        } else return this;
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

        if(this.left.isEqual(new Negation(other))){
            logicalConclusions.add(this.right);
        } else if(this.right.isEqual(new Negation(other))){
            logicalConclusions.add(this.left);
        } 

        if(other instanceof Disjunction){
            Disjunction disjunction = (Disjunction) other;
            if(this.left.isEqual(new Negation(disjunction.left))){
                logicalConclusions.add(new Disjunction(this.right, disjunction.right));
            } else if(this.right.isEqual(new Negation(disjunction.right))){
                logicalConclusions.add(new Disjunction(this.left, disjunction.left));
            } else if(this.left.isEqual(new Negation(disjunction.right))){
                logicalConclusions.add(new Disjunction(this.right, disjunction.left));
            } else if(this.right.isEqual(new Negation(disjunction.left))){
                logicalConclusions.add(new Disjunction(this.left, disjunction.right));
            }
        }

        return other.logicalConclusions(this, logicalConclusions, callIteration + 1);
    }

    @Override
    public String toString(boolean withParentheses) {
        return (withParentheses ? "(" : "") 
            + left.toString(!(left instanceof Disjunction)) 
            + " ∨ " 
            + right.toString(!(right instanceof Disjunction)) 
            + (withParentheses ? ")" : "");
    }
} 
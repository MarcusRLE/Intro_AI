package com.logic;

import java.util.List;

abstract class BinaryTermed implements Expression{
    protected Expression left, right;
    int weight;

    public BinaryTermed(Expression left, Expression right){
        this.left = left;
        this.right = right;
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
        if(left == null){
            left = nextTerm;
        } else if (left.hasEmptyTerm()){
            left.setNextTerm(nextTerm);
        } else if (right == null){
            right = nextTerm;
        } else if (right.hasEmptyTerm()){
            right.setNextTerm(nextTerm);
        }
    }

    @Override
    public boolean hasEmptyTerm() {
        if(left == null || right == null) {
            return true;
        }
        return left.hasEmptyTerm() || right.hasEmptyTerm();
    }

    @Override
    public String toString(boolean withParentheses) {
        String connector = "";
        if(this instanceof Implication){
            connector = " => ";
        } else if (this instanceof Biconditional){
            connector = " <=> ";
        }
        String result = withParentheses ? "(" : "";
        result += this.left != null ? this.left.toString(withParentheses) : "[ EMPTY ]";
        result += connector;
        result += this.right != null ? this.right.toString(withParentheses) : "[ EMPTY ]";
        return result;
    }

    @Override
    public boolean isConsistent() {
        return this.CNF().isConsistent();
    }

    @Override
    public List<Expression> resolution(Expression other) throws Contradiction {
        return this.CNF().resolution(other);
    }

    @Override
    public boolean implies(Expression exp) {
        if(this.isEqual(exp)) {
            return true;
        }
        return this.right.implies(exp) || this.left.equals(exp);
    }

}

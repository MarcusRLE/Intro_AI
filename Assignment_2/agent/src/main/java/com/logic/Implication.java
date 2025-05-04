package com.logic;

import java.util.ArrayList;
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
        String result = withParentheses ? "(" : "";
        result += this.left != null ? this.left.toString(withParentheses) : "[ EMPTY ]";
        result += " => ";
        result += this.right != null ? this.right.toString(withParentheses) : "[ EMPTY ]";
        return result;
    }

    @Override
    public List<Expression> resolution(Expression other) {
        List<Expression> conclusions = new ArrayList<>();
        if(this.left.isEqual(other)){
            conclusions.add(this.right);
        } else if(this.right.isEqual(new Negation(other))){
            conclusions.add(new Negation(this.left));
        }
        return conclusions;
    }
} 
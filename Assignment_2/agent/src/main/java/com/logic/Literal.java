package com.logic;

import java.util.ArrayList;
import java.util.List;

public class Literal implements Expression {
    protected String name;

    public Literal(String name) {
        this.name = name;
    }

    @Override
    public void setNextTerm(Expression nextTerm) {
        // Shouldn't happen
        throw new UnsupportedOperationException("Should never be called");
    }

    @Override
    public boolean hasEmptyTerm() {
        return name == null || name.isEmpty();
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
        return this;
    }

    @Override
    public String toString(boolean withParentheses) {
        if (name == null) {
            return "[ EMPTY ]";
        }
        return name;
    }

    @Override
    public List<Expression> resolution(Expression other) {
        List<Expression> conclusions = new ArrayList<>();
        return conclusions;
    }

    public void setName(String name){
        this.name = name;
    }
} 
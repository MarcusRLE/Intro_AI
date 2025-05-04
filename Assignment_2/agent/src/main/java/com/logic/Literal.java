package com.logic;

import java.util.ArrayList;
import java.util.List;

public class Literal implements Expression {
    protected String name;

    public Literal(String name) {
        this.name = name;
    }

    @Override
    public boolean implies(Expression exp) {
        return equals(exp);
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
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Literal literal = (Literal) o;
        return name.equals(literal.name);
    }
} 

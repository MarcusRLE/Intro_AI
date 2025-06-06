package com.logic;

import java.util.ArrayList;
import java.util.List;

public class Literal implements Expression {
    protected String name;
    int weight;

    public Literal(String name) {
        this.name = name;
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
        // Shouldn't happen
        throw new UnsupportedOperationException("Should never be called");
    }

    @Override
    public boolean hasEmptyTerm() {
        return name == null || name.isEmpty();
    }

    @Override
    public boolean implies(Expression exp) {
        return this.isEqual(exp);
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
    public boolean isConsistent() {
        return true;
    }

    @Override
    public Expression copy() {
        Expression copy = new Literal(name);
        copy.setWeight(weight);
        return copy;
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
        } else if (other instanceof Implication) {
            Implication implication = ((Implication)other);
            if(implication.left.isEqual(this)){
                conclusions.add(implication.right);
            } else if(implication.right.isEqual(new Negation(this))){
                conclusions.add(new Negation(implication.left));
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
  
    public void setName(String name){
        this.name = name;
    }
}

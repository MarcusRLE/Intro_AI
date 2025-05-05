package com.logic;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Implication extends BinaryTermed {


    public Implication(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    String connector() {
        return " => ";
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
    Expression selfCopyWithExp(Expression right, Expression left) {
        Expression copy = new Implication(left, right);
        copy.setWeight(this.weight);
        return copy;
    }
} 

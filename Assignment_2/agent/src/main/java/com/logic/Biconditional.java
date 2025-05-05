package com.logic;

import java.util.List;

public class Biconditional extends BinaryTermed{

    public Biconditional(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public boolean implies(Expression exp) {
        throw new UnsupportedOperationException("Unimplemented method");
    }

    @Override
    public boolean hasContradiction(Expression exp) {
        throw new UnsupportedOperationException("Unimplemented method");
    }

    @Override
    public void sort() {

    }

    @Override
    public Expression CNF() {
        Expression rightImpl = new Implication(left, right);
        Expression leftImpl = new Implication(right, left);
        Expression rightImplToCnf = rightImpl.CNF();
        Expression leftImplToCnf = leftImpl.CNF();
        return new Conjunction(List.of(rightImplToCnf, leftImplToCnf));
    }

    @Override
    public Expression copy() {
        Expression rightCopy = right == null ? null : right.copy();
        Expression leftCopy = left == null ? null : left.copy();
        Expression copy = new Biconditional(leftCopy, rightCopy);
        copy.setWeight(this.weight);
        return copy;
    }
}

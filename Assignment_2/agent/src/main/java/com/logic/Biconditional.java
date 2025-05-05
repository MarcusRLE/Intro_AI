package com.logic;

import java.util.List;

public class Biconditional extends BinaryTermed{

    public Biconditional(Expression left, Expression right) {
        super(left, right);
    }

    @Override
    public boolean implies(Expression exp) {
        return this.CNF().implies(exp);
    }

    @Override
    public boolean hasContradiction(Expression exp) {
        return this.CNF().hasContradiction(exp);
    }

    @Override
    public void sort() {

    }

    @Override
    String connector() {
        return " <=> ";
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
    Expression selfCopyWithExp(Expression right, Expression left) {
        Expression copy = new Biconditional(left, right);
        copy.setWeight(this.weight);
        return copy;
    }
}

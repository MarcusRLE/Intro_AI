package com.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Conjunction implements Expression {
    protected List<Expression> expressions;

    public Conjunction(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String toString(boolean withParentheses) {
        StringBuilder result = new StringBuilder(withParentheses ? "(" : "");
        for (int i = 0; i < this.expressions.size(); i++) {
            result.append(this.expressions.get(i).toString(true));
            if (i < this.expressions.size() - 1) {
                result.append(" ∧ ");
            } else {
                result.append(withParentheses ? ")" : "");
            }
        }
        return result.toString();
    }

    @Override
    public List<Expression> resolution(Expression other) throws Contradiction {
        List<Expression> conclusions = new ArrayList<>();

        List<Expression> copy = new ArrayList<>(expressions);
        boolean isNewExpression = false;
        System.out.println("from con res: " + copy.size());
        for (Expression exp : copy){
            if(other.isEqual(new Negation(exp))){
                throw new Contradiction("Contradiction: " + exp.toString(false) + "contradicts " + this.toString(false));
                // copy.remove(exp);
                // isNewExpression = true;
            }
        }
        // if(isNewExpression){
        //     conclusions.add(new Conjunction(copy));
        // }

        return conclusions;
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
        List<Expression> unique = new ArrayList<>();
        for (Expression exp : expressions) {
            if (unique.stream().noneMatch(e -> e.isEqual(exp))) {
                unique.add(exp);
            }
        }
        if (unique.size() == 1){
            return unique.get(0);
        }

        List <Expression> cnfMapped = unique.stream().map(Expression::CNFrecursive).collect(Collectors.toList());

        return new Conjunction(cnfMapped);
    }
} 

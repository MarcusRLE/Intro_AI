package com.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Conjunction extends MultipleTermed implements Expression {

    public Conjunction(List<Expression> expressions) {
        super();
        this.expressions = expressions;
    }

    @Override
    public String toString(boolean withParentheses) {
        StringBuilder result = new StringBuilder(withParentheses ? "(" : "");
        for (int i = 0; i < this.expressions.size(); i++) {
            Expression exp = this.expressions.get(i);
            result.append(exp != null ? exp.toString(true) : "[ EMPTY ]");
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
        for (Expression exp : copy){
            if(other.isEqual(new Negation(exp))){
                throw new Contradiction("Contradiction: " + exp.toString(false) + "contradicts " + this.toString(false));
                // copy.remove(exp);
                // isNewExpression = true;
            }
            conclusions.addAll(exp.resolution(other));
        }
        // if(isNewExpression){
        //     conclusions.add(new Conjunction(copy));
        // }

        return conclusions;
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

    @Override
    public Expression copy() {
        List<Expression> expressionsCopy = new ArrayList<>();
        for (Expression exp : expressions) {
            Expression innerCopy = exp == null ? null : exp.copy();
            expressionsCopy.add(innerCopy);
        }
        Expression copy = new Disjunction(expressionsCopy);
        copy.setWeight(this.weight);
        return copy;
    }
} 

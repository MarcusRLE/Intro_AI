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
            result.append(this.expressions.get(i).toString(withParentheses));
            if (i < this.expressions.size() - 1) {
                result.append(" ∧ ");
            } else {
                result.append(withParentheses ? ")" : "");
            }
        }
        return result.toString();
    }

    @Override
    public List<Expression> logicalConclusions(Expression other, List<Expression> logicalConclusions, int callIteration) {
        if(callIteration >= 2){
            return logicalConclusions;
        }

        if(this.left.isEqual(this.right)){
            logicalConclusions.add(this.left);
            logicalConclusions = this.left.logicalConclusions(other, logicalConclusions, callIteration + 1);
            return logicalConclusions;
        }

        return other.logicalConclusions(this, logicalConclusions, callIteration + 1);
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
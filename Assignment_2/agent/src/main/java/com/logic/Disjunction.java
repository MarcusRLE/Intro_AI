package com.logic;

import java.util.*;
import java.util.stream.Collectors;

public class Disjunction extends MultipleTermed {

    public Disjunction(List<Expression> expressions) {
        super();
        if (expressions == null) {
            this.expressions = null;
            return;
        }
        List<Expression> newExpressions = new ArrayList<>(expressions);

        for (Expression expression : expressions) {
            if (expression instanceof Disjunction) {
                newExpressions.remove(expression);
                for (Expression exp: ((Disjunction) expression).expressions) {
                    newExpressions.add(exp.copy());
                }
            }
        }
        this.expressions = newExpressions;
    }

    @Override
    public boolean implies(Expression exp) {
        for (Expression expr : expressions) {
            if (expr.implies(exp)) {
                return true;
            }
        }
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

        if (unique.size() == 1) {
            return unique.get(0);
        }

        List<Expression> cnfMapped = unique.stream().map(Expression::CNFrecursive).collect(Collectors.toList());
        Optional<Expression> reduced = cnfMapped.stream().reduce((a, b) -> {
            if (a instanceof Conjunction)
                return nestedConj((Conjunction) a, b);
            else if (b instanceof Conjunction)
                return nestedConj((Conjunction) b, a);
            else
                return new Disjunction(Arrays.asList(a, b));
        });

        cnfMapped = reduced.map(Collections::singletonList)
                .orElse(Collections.emptyList());

        if (cnfMapped.size() == 1) {
            return cnfMapped.get(0);
        } else {
            return new Disjunction(cnfMapped);
        }
    }

    private Expression nestedConj(Conjunction conj, Expression other) {
        List<Expression> conjExp = new ArrayList<>();
        for (Expression exp : conj.expressions) {
            conjExp.add(new Disjunction(Arrays.asList(exp, other)));
        }
        return new Conjunction(conjExp);
    }

    @Override
    public List<Expression> resolution(Expression other) {
        List<Expression> conclusions = new ArrayList<>();

        List<Expression> unique = new ArrayList<>();
        for (Expression exp : expressions) {
            if (unique.stream().noneMatch(e -> e.isEqual(exp))) {
                unique.add(exp.copy());
            }
        }

        if (unique.size() == 1) { return List.of(unique.get(0).copy()); }

        unique.removeIf(exp -> exp.isEqual(new Negation(other)));

        if (unique.size() == 1) { return List.of(unique.get(0).copy()); }

        if (other instanceof Disjunction) {
            for (Expression thisExp : unique) {
                for (Expression otherExp : ((Disjunction) other).expressions) {
                    if (thisExp.isEqual(new Negation(otherExp))) {
                        List<Expression> thisExps = unique.stream().filter(e -> e.isEqual(thisExp))
                                .collect(Collectors.toList());
                        List<Expression> otherExps = unique.stream()
                                .filter(e -> e.isEqual(otherExp) || e.isEqual(thisExp)).collect(Collectors.toList());
                        for (Expression exp: otherExps) { thisExps.add(exp.copy()); }
                        for (Expression exp: thisExps) { conclusions.add(exp.copy()); }
                    }
                }
            }
        } else if (other instanceof Conjunction) {
            // for (Expression thisExp : unique) {
            for (Expression otherExp : ((Conjunction) other).expressions) {
                unique.removeIf(exp -> exp.isEqual(new Negation(otherExp)));
            }
            // }
            if (unique.size() == 1) {
                for (Expression exp: unique) { conclusions.add(exp.copy()); }
                for (Expression exp: ((Conjunction) other).expressions) { conclusions.add(exp.copy()); }
                return conclusions;
            }
        }

        return conclusions;
    }

    @Override
    public String toString(boolean withParentheses) {
        StringBuilder result = new StringBuilder(withParentheses ? "(" : "");
        for (int i = 0; i < this.expressions.size(); i++) {
            Expression exp = this.expressions.get(i);
            result.append(exp != null ? exp.toString(true) : "[ EMPTY ]");
            if (i < this.expressions.size() - 1) {
                result.append(" ∨ ");
            } else {
                result.append(withParentheses ? ")" : "");
            }
        }
        return result.toString();
    }

    @Override
    public Expression copy() {
        ArrayList<Expression> expressionsCopy = new ArrayList<>();
        for (Expression exp : expressions) {
            expressionsCopy.add(exp.copy());
        }
        return new Disjunction(expressionsCopy);
    }
}

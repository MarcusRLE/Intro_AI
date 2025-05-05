package com.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public interface Expression {

    default int randomWeight() {
        Random rand = new Random();
        return rand.nextInt(100) + 1;
    }

    default Expression CNFrecursive(){
        Expression cnf = this.CNF();
        String ownStr = this.toString(false);
        String cnfStr = cnf.toString(false);

        if(ownStr.equals(cnfStr)){
            return this;
        } else {
            return cnf.CNFrecursive();
        }
    }

    void setNextTerm(Expression nextTerm);

    boolean hasEmptyTerm();

    boolean implies(Expression exp);

    boolean hasContradiction(Expression exp);

    void sort();

    Expression CNF();

    default boolean isEqual(Expression other){
        other = other.CNF();
        Expression own = this.CNF();

        if (own instanceof Conjunction && other instanceof Conjunction) {
            List<Expression> expressionsCopy = new ArrayList<>(((Conjunction) own).expressions);

            for (Expression expression : ((Conjunction) other).expressions) {
                expressionsCopy.removeIf(e -> e.isEqual(expression));
            }

            return expressionsCopy.isEmpty();
        } else if (own instanceof Disjunction && other instanceof Disjunction) {
            List<Expression> expressionsCopy = new ArrayList<>(((Disjunction) own).expressions);

            for (Expression expression : ((Disjunction) other).expressions) {
                expressionsCopy.removeIf(e -> e.isEqual(expression));
            }

            return expressionsCopy.isEmpty();
        }

        else if(own instanceof Negation && other instanceof Negation){
            return ((Negation)own).expression.isEqual(((Negation)other).expression);
        }
        else if(own instanceof Implication && other instanceof Implication){
            return ((Implication)own).left.isEqual(((Implication)other).left) && ((Implication)own).right.isEqual(((Implication)other).right);
        }
        else if(own instanceof Literal && other instanceof Literal){
            return ((Literal)own).name.equals(((Literal)other).name);
        }
        else{
            return false;
        }
    }


    List<Expression> resolution(Expression other) throws Contradiction;

    String toString(boolean withParentheses);

    boolean isConsistent();

    public Expression copy();

    public static List<Expression> removeDuplicates(List<Expression> list) {
        List<Expression> deduplicated = new ArrayList<>();
        for (Expression e : list) {
            if (deduplicated.stream().noneMatch(d -> d.isEqual(e))) {
                deduplicated.add(e);
            }
        }
        return deduplicated;
    }

    public static boolean sameContent(List<Expression> list1, List<Expression> list2) {
        if (list1.size() != list2.size()) { return false; }
        List<Expression> list2copy = new ArrayList<>(list2);
        for (Expression exp1 : list1) {
            boolean found = false;
            for (int i = 0; i < list2copy.size(); i++) {
                if (exp1.isEqual(list2copy.get(i))) {
                    list2copy.remove(i);
                    found = true;
                }
            }
            if (!found) {
                return false;
            }
        }
        return list2copy.isEmpty();
    }
}

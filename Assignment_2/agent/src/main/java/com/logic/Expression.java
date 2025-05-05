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
        List<Expression> newList = new ArrayList<>();
        for (Expression e : list) {
            boolean alreadyExists = false;
            for (Expression existing : newList) {
                if (e.isEqual(existing)) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                newList.add(e);
            }
        }
        return newList;
    }
}

package com.logic;

import java.util.List;

public interface Expression {
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

    void sort();

    Expression CNF();

    default boolean isEqual(Expression other){
        other = other.CNF();
        Expression own = this.CNF();

        if(own instanceof Conjunction && other instanceof Conjunction){
            return ((Conjunction)own).left.isEqual(((Conjunction)other).left) && ((Conjunction)own).right.isEqual(((Conjunction)other).right)
                || ((Conjunction)own).left.isEqual(((Conjunction)other).right) && ((Conjunction)own).right.isEqual(((Conjunction)other).left);
        }
        else if(own instanceof Disjunction && other instanceof Disjunction){
            return ((Disjunction)own).left.isEqual(((Disjunction)other).left) && ((Disjunction)own).right.isEqual(((Disjunction)other).right)
                || ((Disjunction)own).left.isEqual(((Disjunction)other).right) && ((Disjunction)own).right.isEqual(((Disjunction)other).left);
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


    List<Expression> logicalConclusions(Expression other, List<Expression> logicalConclusions, int callIteration);

    String toString(boolean withParentheses);
}
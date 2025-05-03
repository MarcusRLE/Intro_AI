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

    Expression CNF();

    default boolean isEqual(Expression other){
        other = other.CNF();
        Expression own = this.CNF();

        if(own instanceof Conjuction && other instanceof Conjuction){
            return ((Conjuction)own).left.isEqual(((Conjuction)other).left) && ((Conjuction)own).right.isEqual(((Conjuction)other).right)
                || ((Conjuction)own).left.isEqual(((Conjuction)other).right) && ((Conjuction)own).right.isEqual(((Conjuction)other).left);
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
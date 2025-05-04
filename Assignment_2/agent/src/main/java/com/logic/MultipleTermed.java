package com.logic;

import java.util.List;

public abstract class MultipleTermed implements Expression {
    List<Expression> expressions;

    public List<Expression> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public void setNextTerm(Expression nextTerm) {
        for (int i = 0; i < expressions.size(); i++) {
            if (expressions.get(i) == null) {
                expressions.set(i, nextTerm);
                break;
            } else if (expressions.get(i).hasEmptyTerm()) {
                    expressions.get(i).setNextTerm(nextTerm);
                    break;
            }
        }
    }

    @Override
    public boolean hasEmptyTerm() {
        for (Expression exp : expressions) {
            if(exp == null){
                return true;
            } else if(exp.hasEmptyTerm()){
                return true;
            }
        }
        return false;
    }
}

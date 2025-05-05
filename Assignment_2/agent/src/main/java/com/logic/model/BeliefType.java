package com.logic.model;

import com.logic.*;

public enum BeliefType {

    LITERAL(1, new Literal(null)),
    NEGATION(2, new Negation(null)),
    CONJUNCTION(3, new Conjunction(null)),
    DISJUNCTION(4, new Disjunction(null)),
    IMPLICATION(5, new Implication(null, null)),
    BICONDITIONAL(6, new Biconditional(null, null)),;

    private final int code;
    private final Expression nullExp;

    BeliefType(int code, Expression nullExp) {
        this.code = code;
        this.nullExp = nullExp;
    }

    public int getCode() {
        return code;
    }
    public Expression getNullExp() { return nullExp; }

    public static BeliefType fromCode(int code) {
        for (BeliefType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid BeliefType code: " + code);
    }
}

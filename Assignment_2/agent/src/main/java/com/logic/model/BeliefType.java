package com.logic.model;

public enum BeliefType {

    LITERAL(1),
    NEGATION(2),
    CONJUNCTION(3),
    DISJUNCTION(4),
    IMPLICATION(5);

    private final int code;

    BeliefType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static BeliefType fromCode(int code) {
        for (BeliefType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid BeliefType code: " + code);
    }
}

package com.logic;

public class util {

    public static String falseEquality(Expression exp1, Expression exp2) {
        String result = "\n";
        result += exp1.toString(false) + " should equal " + exp2.toString(false);
        result += "\nGot:\n";
        result += exp1.CNF().toString(false) + " compared to " + exp2.CNF().toString(false);
        return result;
    }
}

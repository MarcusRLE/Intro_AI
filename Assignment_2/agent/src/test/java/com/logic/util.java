package com.logic;

import java.util.ArrayList;
import java.util.List;

public class util {

    public static String falseEquality(Expression exp1, Expression exp2) {
        String result = "\n";
        result += exp1.toString(false) + " should equal " + exp2.toString(false);
        result += "\nGot:\n";
        result += exp1.CNF().toString(false) + " compared to " + exp2.CNF().toString(false);
        return result;
    }

    public static boolean sameContent(List<Expression> list1, List<Expression> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

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

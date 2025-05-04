package com.logic;

import com.logic.controller.BeliefController;
import com.logic.view.BeliefBuilderView;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        BeliefController beliefController = new BeliefController();
        BeliefBuilderView view = new BeliefBuilderView(beliefController);
        view.buildNewBelief();
    }
}

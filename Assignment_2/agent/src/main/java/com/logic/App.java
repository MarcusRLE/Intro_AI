package com.logic;

import com.logic.controller.BeliefController;
import com.logic.view.BeliefActionView;
import com.logic.view.BeliefBuilderView;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        BeliefController beliefController = new BeliefController();
        BeliefActionView viewAction = new BeliefActionView(beliefController);

        while(true) {
            viewAction.chooseAction();
        }

    }
}

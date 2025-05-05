package com.logic;

import com.logic.controller.BeliefController;
import com.logic.view.BeliefActionView;

public class App {
    public static void main( String[] args ) {
        BeliefController beliefController = new BeliefController();
        BeliefActionView viewAction = new BeliefActionView(beliefController);

        while(!beliefController.getExitProgram()) {
            viewAction.chooseAction();
        }

    }
}

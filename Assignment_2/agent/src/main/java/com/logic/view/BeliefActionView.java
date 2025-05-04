package com.logic.view;

import com.logic.Expression;
import com.logic.controller.BeliefController;

import java.util.Scanner;

public class BeliefActionView {
    private BeliefController beliefController;
    private BeliefBuilderView beliefBuilderView;
    private Scanner scanner;

    public BeliefActionView(BeliefController beliefController) {
        this.beliefController = beliefController;
        this.beliefBuilderView = new BeliefBuilderView(beliefController);
        this.scanner = new Scanner(System.in);
    }

    public void chooseAction() {
        System.out.println("\nPlease choose an action:");
        System.out.println("[1] Build new belief\n" +
                "[2] See belief set");
        int chosenAction = getNumberInput(1,2);

        switch (chosenAction) {
            case 1:
                buildNewBelief();
                break;
            case 2:
                System.out.println(beliefController.getBeliefs().toString());
                break;
            default:
                break;
        }
    }

    public void buildNewBelief() {
        beliefBuilderView.buildNewBelief();
        System.out.println("New belief Complete");
        System.out.println(beliefController.getCurrentNewBelief().toString(false));

        System.out.println(
                "[1] Add new belief"
        );

        int chosenAction = getNumberInput(1,1);
        switch (chosenAction) {
            case 1:
                Expression newBelief = beliefController.getCurrentNewBelief();
                beliefController.addNewBelief(newBelief);
                break;
        }
    }

    public int getNumberInput(int min, int max) {
        int chosenNumber;

        try {
            chosenNumber = Integer.parseInt(scanner.nextLine());
            if (chosenNumber < min || chosenNumber > max) {
                System.out.println("Please enter a number between " + min + " and " + max);
                chosenNumber = getNumberInput(min, max);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number between " + min + " and " + max);
            chosenNumber = getNumberInput(min, max);
        }

        return chosenNumber;
    }


}

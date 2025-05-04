package com.logic.view;

import com.logic.BeliefSet;
import com.logic.Expression;
import com.logic.controller.BeliefController;

import java.util.List;
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
                "[2] See belief set\n" +
                "[3] Exit");
        int chosenAction = getNumberInput(1,3);

        switch (chosenAction) {
            case 1:
                beliefBuilderView.buildNewBelief();
                newBeliefAction();
                break;
            case 2:
                System.out.println(beliefController.getBeliefs().toString());
                break;
            case 3:
                beliefController.setExitProgram(true);
                break;
            default:
                break;
        }
    }

    public void newBeliefAction() {
        System.out.println(beliefController.getCurrentNewBelief().toString(false));

        System.out.println(
                "[1] Add new belief\n" +
                        "[2] Discard belief\n" +
                        "[3] Check for contradiction\n" +
                        "[4] See logical conclusions from new belief\n" +
                        "[5] Exit"
        );

        int chosenAction = getNumberInput(1,4);
        Expression newBelief = beliefController.getCurrentNewBelief();
        switch (chosenAction) {
            case 1:
                beliefController.addNewBelief(newBelief);
                return;
            case 2:
                return;
            case 3:
                boolean hasContradiction = beliefController.hasContradiction(newBelief);
                String msg = hasContradiction ? "Contradiction detected: {" + beliefController.getContradiction() + "}" : "No contradiction detected";
                System.out.println(msg);
                break;
            case 4:
                BeliefSet conclusions = new BeliefSet(beliefController.logicalConclusion(newBelief));
                String set = conclusions.toString();
                System.out.println(set);
                break;
            case 5:
                beliefController.setExitProgram(true);
                return;
        }
        newBeliefAction();
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

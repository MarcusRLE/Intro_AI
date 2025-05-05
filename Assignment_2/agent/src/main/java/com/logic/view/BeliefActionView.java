package com.logic.view;

import com.logic.BeliefSet;
import com.logic.BeliefSetImpl;
import com.logic.Contradiction;
import com.logic.Expression;
import com.logic.controller.BeliefController;
import com.logic.model.UserAction;

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
        System.out.println("================================");
        printBeliefSet();
        System.out.println();
        System.out.println("Please choose an action:");
        List<UserAction> actions = getUserActions();
        getAndExecuteCommand(actions);
    }

    public List<UserAction> getUserActions() {
        return List.of(
                new UserAction("Build new belief", true, () -> {
                    beliefBuilderView.buildNewBelief();
                    newBeliefAction();
                }),
                new UserAction("See logical entailment of belief state", true, () -> {
                    try {
                        List<Expression> logicalEnt = beliefController.checkLogicalEntailment();
                        BeliefSet beliefSet = new BeliefSetImpl(logicalEnt);
                        System.out.println("Logical entailment of belief state: " + beliefSet);
                    } catch (Contradiction c) {
                        System.out.println("Contradiction found - Remove by building new belief and contracting");
                    }
                }),
                new UserAction("Clear Belief", true, () -> {
                    beliefController.clearBeliefState();
                }),
                new UserAction("Exit", false, () -> {
                    beliefController.setExitProgram(true);
                })
        );

    }

    public void newBeliefAction() {
        if(beliefController.getAbortBuilder()){
            return;
        }
        printBeliefSet();
        printCurrentNewBelief();
        System.out.println();
        List<UserAction> actions = getBeliefUserActions();
        boolean willReturn = getAndExecuteCommand(actions);

        if(willReturn){
            return;
        }
        newBeliefAction();
    }

    public void printCurrentNewBelief() {
        System.out.println("New Belief: " + beliefController.getCurrentNewBelief().toString(false));
    }

    public void printBeliefSet(){
        System.out.println("Belief set: " + beliefController.getBeliefs());
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


    private List<UserAction> getBeliefUserActions() {
        Expression newBelief = beliefController.getCurrentNewBelief();

        return List.of(
                new UserAction("Add with revision", true, () -> {
                    try {
                        beliefController.addWithRevision(newBelief);
                    } catch (Contradiction c) {
                        System.out.println("Contradiction found when revising new belief");
                    }
                }),
                new UserAction("Add with expansion", true, () -> {
                    beliefController.addWithExpansion(newBelief);
                }),
                new UserAction("Contract with new belief", true, () -> {
                    beliefController.contract(newBelief);
                }),
                new UserAction("Discard belief", true, () -> {
                    // Do nothing
                }),
                new UserAction("Check for contradictions",false, () -> {
                    boolean hasContradiction = beliefController.hasContradiction(newBelief);
                    String msg = hasContradiction ? "Contradiction detected" : "No contradiction detected";
                    System.out.println(msg);
                }),
                new UserAction("Exit", true, () -> {
                    beliefController.setExitProgram(true);
                })
        );
    }

    private boolean getAndExecuteCommand(List<UserAction> actions){
        int size = actions.size();

        for(int i = 0; i < size; i++) {
            System.out.println("[" + (i + 1) + "] " + actions.get(i).toString());
        }

        int chosenAction = getNumberInput(1, actions.size());
        UserAction action = actions.get(chosenAction - 1);
        action.getCommand().executeCommand();

        return action.willReturn();
    }

}

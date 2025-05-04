package com.logic.view;

import com.logic.controller.BeliefController;
import com.logic.model.BeliefType;

import java.util.Scanner;

public class BeliefBuilderView {

    public BeliefController beliefController;

    public BeliefBuilderView(BeliefController beliefController) {
        this.beliefController = beliefController;
    }

    public void buildNewBelief(){
        System.out.println("Create new belief to insert in belief set");
        Scanner scanner = new Scanner(System.in);
        BeliefType chosenBelief = BeliefType.fromCode(getBeliefType(scanner));
        beliefController.setCurrentNewBelief(chosenBelief);
        if(beliefController.currentHasMultipleTerms()){
            beliefController.setSizeOfBelief(getNumberOfTerms(scanner));
        }
        while (!beliefController.isComplete()){
            buildTerm(scanner);

        }
    }

    public void buildTerm(Scanner scanner) {
        if (beliefController.currentTermIsLiteral()) {
            System.out.println("Enter name of Literal term (one letter)");
            String name = "" + getCharInput(scanner);
            beliefController.setLiteralTerm(name);
        } else {
            printCurrentNewBelief();
            System.out.println("Create next term");
            BeliefType chosenBelief = BeliefType.fromCode(getBeliefType(scanner));
            beliefController.setCurrentTerm(chosenBelief);
            if(beliefController.currentHasMultipleTerms()){
                beliefController.setSizeOfBelief(getNumberOfTerms(scanner));
            }
            beliefController.defineNextTerm();
        }
    }

    public void printCurrentNewBelief(){
        System.out.println("Current newBelief");
        System.out.println(beliefController.getCurrentNewBelief().toString(false));
        System.out.println("---------------------------------------");
    }

    public int getBeliefType(Scanner scanner) {
        StringBuilder msg = new StringBuilder("Enter Belief Type:");
        for (BeliefType beliefType : BeliefType.values()) {
            msg.append("\n[").append(beliefType.getCode()).append("] ").append(beliefType.name());
        }
        System.out.println(msg);
        return getTypeInput(scanner);
    }

    public int getTypeInput(Scanner scanner) {
        int chosenNumber;
        try{
            chosenNumber = Integer.parseInt(scanner.nextLine());
            if (chosenNumber <1 || chosenNumber > 5) {
                System.out.println("Please enter a number between 1 and 5");
                chosenNumber = getTypeInput(scanner);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number in digits only");
            chosenNumber = getTypeInput(scanner);
        }
        return chosenNumber;
    }

    public int getNumberOfTerms(Scanner scanner) {
        System.out.println("Enter number of terms: ");
        return getNumberInput(scanner);
    }

    public int getNumberInput(Scanner scanner) {
        int chosenNumber;
        try {
            chosenNumber = Integer.parseInt(scanner.nextLine());
            if(chosenNumber < 2){
                System.out.println("Must be larger than 1");
                chosenNumber = getNumberInput(scanner);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a number in digits only");
            chosenNumber = getNumberInput(scanner);
        }
        return chosenNumber;
    }

    public char getCharInput(Scanner scanner) {
        String input = scanner.nextLine();
        if (input.length() == 1) {
            char ch = input.charAt(0);
            if (ch >= 'A' && ch <= 'Z') {
                return ch;
            }
        }
        System.out.println("Please enter a valid character");
        return getCharInput(scanner);
    }

}

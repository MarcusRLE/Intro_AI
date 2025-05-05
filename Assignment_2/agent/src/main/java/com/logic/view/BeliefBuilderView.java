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
            if(beliefController.getAbortBuilder()){
                System.out.println();
                System.out.println("Abort building term");
                return;
            }
            buildTerm(scanner);
        }
        System.out.println("New belief Complete");
    }

    public void buildTerm(Scanner scanner) {
        if (beliefController.currentTermIsLiteral()) {
            System.out.println("Enter name of Literal term (one letter)");
            String name = "" + getCharInput(scanner);
            beliefController.setLiteralTerm(name);
        } else {
            printCurrentNewBelief();
            System.out.println("Create next term");
            int chosenTerm = getBeliefType(scanner);
            if (chosenTerm == BeliefType.values().length + 1){
                beliefController.setAbortBuilder(true);
                return;
            }
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
        StringBuilder msg = new StringBuilder("Choose belief type:");
        for (BeliefType beliefType : BeliefType.values()) {
            msg.append("\n[").append(beliefType.getCode()).append("] ").append(beliefType.name());
        }
        int abortOption = BeliefType.values().length + 1;
        msg.append("\n[").append(abortOption).append("] Abort");
        System.out.println(msg);
        return getTypeInput(scanner);
    }

    public int getTypeInput(Scanner scanner) {
        int chosenNumber;
        int size = BeliefType.values().length + 1;
        try{
            chosenNumber = Integer.parseInt(scanner.nextLine());
            if (chosenNumber <1 || chosenNumber > size) {
                System.out.println("Please enter a number between 1 and " + size);
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
            char ch = input.toUpperCase().charAt(0);
            if (ch >= 'A' && ch <= 'z') {
                return ch;
            }
        }
        System.out.println("Please enter a valid character");
        return getCharInput(scanner);
    }

}

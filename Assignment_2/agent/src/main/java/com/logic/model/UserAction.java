package com.logic.model;

public class UserAction {
    private String text;
    private ActionCommand command;
    private boolean willReturn;

    public UserAction(String text, boolean willReturn, ActionCommand command) {
        this.text = text;
        this.command = command;
        this.willReturn = willReturn;
    }

    public String getText() {
        return text;
    }

    public ActionCommand getCommand() {
        return command;
    }

    public boolean willReturn() {
        return willReturn;
    }

    @Override
    public String toString() {
        return text;
    }
}

package org.example.viewmodel;
import javafx.event.ActionEvent;


public class Command {
    private final Runnable action;

    public Command(Runnable action) {
        this.action = action;
    }

    public void execute(ActionEvent event) {
        action.run();
    }
}

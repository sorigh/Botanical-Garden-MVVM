package org.example.viewmodel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class Command extends SimpleObjectProperty<EventHandler<ActionEvent>> {
    public Command(Runnable action) {
        super(event -> action.run());
    }
}
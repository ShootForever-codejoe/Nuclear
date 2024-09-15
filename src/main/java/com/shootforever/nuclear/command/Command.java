package com.shootforever.nuclear.command;

public abstract class Command {
    protected final String name;

    protected Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void execute(String[] args);
}

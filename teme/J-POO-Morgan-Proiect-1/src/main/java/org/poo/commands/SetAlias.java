package org.poo.commands;

import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class SetAlias implements Command {
    private final List<User> users;
    private final CommandInput input;

    public SetAlias(final List<User> users, final CommandInput command) {
        this.users = users;
        this.input = command;
    }

    public void execute() {
        for (final User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                user.addAlias(input.getAlias(), input.getAccount());
            }
        }
    }
}

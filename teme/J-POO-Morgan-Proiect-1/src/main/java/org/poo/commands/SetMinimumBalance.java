package org.poo.commands;

import org.poo.bankData.Account;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class SetMinimumBalance implements Command {
    private final List<User> users;
    private final CommandInput input;

    public SetMinimumBalance(final List<User> users, final CommandInput command) {
        this.users = users;
        this.input = command;
    }

    public void execute() {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                if (account.getIBAN().equals(input.getAccount())) {
                    account.setMinBalance(input.getMinBalance());
                }
            }
        }
    }
}

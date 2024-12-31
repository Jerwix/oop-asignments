package org.poo.commands;

import org.poo.bankData.Card;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.List;

public final class CreateOneTimeCard implements Command {

    private final List<User> users;
    private final CommandInput input;

    public CreateOneTimeCard(final List<User> users, final CommandInput command) {
        this.users = users;
        this.input = command;
    }

    /**
     * same idea as CreateCard, but the flag for oneTimeUse is set
     */
    @Override
    public void execute() {
        final Card card = new Card(Utils.generateCardNumber(), 1);
        CreateCard.addCard(card, users, input);
    }
}

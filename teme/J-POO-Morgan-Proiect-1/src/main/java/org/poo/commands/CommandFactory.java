package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.bankData.Exchange;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class CommandFactory {
    /**
     * Basic factory design pattern
     * @param command details of the command
     * @param users list of all the users
     * @param output node to pass the output to
     * @param exchange for using the transaction methods
     * @return the appropriate command
     */
    public static Command generateCommand(final CommandInput command, final List<User> users,
                                          final ArrayNode output,
                                          final Exchange exchange) {
        return switch (command.getCommand()) {
            case "printUsers" -> new PrintUsers(users, output, command.getTimestamp());
            case "addAccount" -> new AddAccount(users, command);
            case "createCard" -> new CreateCard(users, command);
            case "addFunds" -> new AddFunds(users, command);
            case "deleteAccount" -> new DeleteAccount(users, command, output);
            case "createOneTimeCard" -> new CreateOneTimeCard(users, command);
            case "deleteCard" -> new DeleteCard(users, command);
            case "payOnline" -> new PayOnline(users, command, output, exchange);
            case "sendMoney" -> new SendMoney(users, command, exchange);
            case "printTransactions" -> new PrintTransactions(users, command, output);
            case "setAlias" -> new SetAlias(users, command);
            case "checkCardStatus" -> new CheckCardStatus(users, command, output);
            case "setMinimumBalance" -> new SetMinimumBalance(users, command);
            case "splitPayment" -> new SplitPayment(users, command, exchange);
            default -> null;
        };
    }
}

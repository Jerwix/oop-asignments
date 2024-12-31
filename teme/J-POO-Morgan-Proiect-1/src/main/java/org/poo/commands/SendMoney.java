package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankData.Account;
import org.poo.bankData.Exchange;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class SendMoney implements Command {
    private final List<User> users;
    private final CommandInput input;
    private final Exchange exchange;

    public SendMoney(final List<User> users, final CommandInput command, final Exchange exchange) {
        this.users = users;
        this.input = command;
        this.exchange = exchange;
    }

    /**
     * loops trough users, accounts until the sender and receiver are found
     * executes the transfer
     * logs the exchange or the exception into the sender's history
     */
    public void execute() {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                if (account.getIBAN().equals(input.getAccount())) {
                    for (final User user2 : users) {
                        for (final Account account2 : user2.getAccounts()) {
                            if (account2.getIBAN().equals(user.getAlias(input.getReceiver()))
                                || account2.getIBAN().equals(input.getReceiver())) {
                                try {
                                    exchange.transfer(account, account2, input.getAmount());
                                } catch (final Exception e) {
                                    final ObjectMapper mapper = new ObjectMapper();
                                    final ObjectNode root = mapper.createObjectNode();
                                    root.put("timestamp", input.getTimestamp());
                                    root.put("description", e.getMessage());
                                    user.getHistory().add(root);
                                    return;
                                }
                                exchange.log(user, account, account2, input);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}

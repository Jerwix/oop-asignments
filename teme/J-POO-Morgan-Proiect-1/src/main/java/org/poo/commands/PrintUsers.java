package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.bankData.Account;
import org.poo.bankData.Card;
import org.poo.bankData.User;

import java.util.List;

@Data
@NoArgsConstructor
public final class PrintUsers implements Command {

    private List<User> users;
    private ArrayNode output;
    private int timeStamp;

    public PrintUsers(final List<User> users, final ArrayNode output, final int timeStamp) {
        this.users = users;
        this.output = output;
        this.timeStamp = timeStamp;
    }

    /**
     * loops through users, accounts, cards to add them to the output
     */
    public void execute() {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode root = mapper.createObjectNode();
        root.put("command", "printUsers");
        final ArrayNode usersList = mapper.createArrayNode();
        for (final User userI : users) {
            final ObjectNode userNode = mapper.createObjectNode();
            userNode.put("firstName", userI.getFirstName());
            userNode.put("lastName", userI.getLastName());
            userNode.put("email", userI.getEmail());
            final ArrayNode accountsList = mapper.createArrayNode();
            for (final Account accountI : userI.getAccounts()) {
                final ObjectNode accountNode = mapper.createObjectNode();
                accountNode.put("IBAN", accountI.getIBAN());
                accountNode.put("balance", accountI.getBalance());
                accountNode.put("currency", accountI.getCurrency());
                accountNode.put("type", accountI.getType());
                final ArrayNode cardsList = mapper.createArrayNode();
                for (final Card cardI : accountI.getCards()) {
                    final ObjectNode cardNode = mapper.createObjectNode();
                    cardNode.put("cardNumber", cardI.getCardNumber());
                    cardNode.put("status", cardI.getStatus());
                    cardsList.add(cardNode);
                }
                accountNode.set("cards", cardsList);
                accountsList.add(accountNode);
            }
            userNode.set("accounts", accountsList);
            usersList.add(userNode);
        }
        root.set("output", usersList);
        root.put("timestamp", timeStamp);
        output.add(root);
    }
}

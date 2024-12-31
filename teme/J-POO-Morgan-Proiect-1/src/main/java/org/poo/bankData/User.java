package org.poo.bankData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.fileio.UserInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
public final class User {
    private String firstName;
    private String lastName;
    private String email;
    private List<Account> accounts;
    private ArrayNode history;
    private HashMap<String, String> aliasMap;

    public User(final String firstName, final String lastName, final String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        accounts = new ArrayList<>();
        aliasMap = new HashMap<>();
        final ObjectMapper objectMapper = new ObjectMapper();
        history = objectMapper.createArrayNode();
    }

    /**
     * parses the input and makes a list of users
     * @param users the input of the program
     * @return userList as a new List of type <User>
     */
    public static List<User> readUsers(final UserInput[] users) {
        final List<User> userList = new ArrayList<>();
        for (final UserInput user : users) {
            userList.add(new User(user.getFirstName(), user.getLastName(), user.getEmail()));
        }
        return userList;
    }

    /**
     * Maps the alias to an IBAN
     * @param alias the new alias
     * @param IBAN account to receive alias
     */
    public void addAlias(final String alias, final String IBAN) {
        aliasMap.put(alias, IBAN);
    }

    public String getAlias(final String alias) {
        if (aliasMap.containsKey(alias)) {
            return aliasMap.get(alias);
        }
        return alias;
    }
}

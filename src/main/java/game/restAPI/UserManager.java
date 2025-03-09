// temporary hash map to save created users until database is implemented
// Manages user registration and authentication

package game.restAPI;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    // Temporary in-memory store for users (username -> password)
    private static final Map<String, String> users = new HashMap<>();

    // Register a new user, return false if user already exists
    public boolean registerUser(String username, String password) {
        if (users.containsKey(username)) {
            return false;  // User already exists
        }
        users.put(username, password);
        return true;  // User registered successfully
    }

    // Authenticate a user by username and password
    public boolean authenticateUser(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}

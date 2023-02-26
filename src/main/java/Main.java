import jdbc.SimpleJDBCRepository;
import jdbc.User;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        SimpleJDBCRepository repository = new SimpleJDBCRepository();

        // Create a new user
        User newUser = new User("sandro", "john.doe@example.com", 2);
        Long userId = repository.createUser(newUser);
        System.out.println("New user created with ID: " + userId);

        // Find a user by ID
        User foundUser = repository.findUserById(userId);
        System.out.println("Found user: " + foundUser.toString());

        // Update a user
        foundUser.setFirstName("genadi");
        repository.updateUser(foundUser);
        System.out.println("User updated: " + foundUser);


        // Find a user by name
        List<User> usersWithName = (List<User>) repository.findUserByName("sandro");
        System.out.println("Users with name 'John Doe': " + usersWithName);

        // Find all users
        List<User> allUsers = repository.findAllUser();
        System.out.println("All users: " + allUsers);
        System.out.println("All users size: " + allUsers.size());

        // Delete a user
        repository.deleteUser(userId);
        System.out.println("User deleted with ID: " + userId);

        // Users after deletion
        List<User> newUsers = repository.findAllUser();
        System.out.println("All users size after deletion: " + newUsers.size());
    }
}

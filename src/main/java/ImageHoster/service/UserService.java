package ImageHoster.service;

import ImageHoster.model.User;
import ImageHoster.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Method to persist new user object in the database through repository
     * @param newUser   User object to be persisted in the database
     */
    public void registerUser(User newUser) {
        userRepository.registerUser(newUser);
    }

    /**
     * Method to check if User exists in the database with the username and password passed
     * @param user  User object to be validated
     * @return      User object if user exists in the database, else return null
     */
    public User login(User user) {
        User existingUser = userRepository.checkUser(user.getUsername(), user.getPassword());
        if (existingUser != null) {
            return existingUser;
        } else {
            return null;
        }
    }

}

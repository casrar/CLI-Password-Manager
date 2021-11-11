import java.util.Iterator;
import java.io.Serializable;

public class Manager implements Serializable {
    HashDictionary<String, User> userLogin;

    public Manager() {
        userLogin = new HashDictionary<>();
    }

    // Takes in a username and a password as a String and checks to see if the username is
    // already contained. If the username is not contained then a new User object is created
    // and added to the userLogin dictionary.
    public boolean createAcct(String username, String password) {
        if (contains(username)) {
            System.out.println("This username is taken.");
            return false;
        }
        User newUser = new User(username, password);
        userLogin.put(username, newUser);
        System.out.println("Account created.");
        return true;
    }

    // Checks to see if the username and password entered are valid.
    public boolean login(String username, String password) {
        if (contains(username)) {
            userLogin.get(username).equals(password);
            return true;
        }
        return false;
    }

    // If the username and password entered are valid then the User object is removed from the
    // userLogin dictionary.
    public boolean deleteAcct(String username, String password) {
        if (contains(username)) {
            if (userLogin.get(username).loginForUser.getPassword().equals(password)) {
                userLogin.remove(username);
                System.out.println("Account removed!");
                return true;
            }
        }
        System.out.println("That account does not exist.");
        return false;
    }


    // Iterates through all the contained usernames and checks if the username the user entered is contained.
    private boolean contains(String username) {
        Iterator<String> usernameToCheck = userLogin.keys();

        while (usernameToCheck.hasNext()) {
            if (usernameToCheck.next().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // TESTING
    // Lists all accounts stored in the userLogin dictionary.
    public void listAcct() {
        Iterator<User> usersToCheck = userLogin.elements();

        while (usersToCheck.hasNext()) {
            User temp = usersToCheck.next();

            System.out.println(temp.loginForUser.username);
        }
    }
}

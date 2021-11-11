import java.util.Iterator;
import java.io.Serializable;

public class User implements Serializable {
    AList<LoginsForSites> loginsForSites; // Array List of websites that logins are stored in.
    UserLogins loginForUser; // Class that stores the username and password of the user.

    // Constructor for the user takes in the username and the password.
    // The loginsForSites list is initialized along with the users login.
    public User(String username, String password) {
        loginsForSites = new AList<>();
        loginForUser = new UserLogins(username, password);
    }

    // Adds a LoginsForSites object to the list of the users saved logins for websites.
    public void addWebsiteLogin(LoginsForSites websiteLogin) {
        loginsForSites.add(websiteLogin);
    }

    // Removes a LoginsForSites object to the list of the users saved logins for websites.
    // Uses an index based system that provides the user with the index of specified login.
    public boolean removeWebsiteLogin(int index) {
        if (index >= 0 && index < loginsForSites.size()) {
            loginsForSites.remove(index);
            return true;
        }
        return false;
    }

    // Allows user to update any login the have
    public boolean updateWebsiteLogin(int index, String user, String pass) {
        if (user.trim().length() > 0 && pass.trim().length() > 0) {
            LoginsForSites newLogin = new LoginsForSites(user, pass, loginsForSites.get(index).website);
            loginsForSites.set(index, newLogin);
            return true;
        }
        return false;
    }

    // List all contained sites for the specific user, also lists the index of the login information
    // to enable deletion of logins.
    public void listWebsiteLogins() {
        int count = 0;
        Iterator<LoginsForSites> loginsForSitesIterator = loginsForSites.listIterator();

        while (loginsForSitesIterator.hasNext()) {
            LoginsForSites traversalLoginsForSite = loginsForSitesIterator.next();
            System.out.println("(Site Index: " + count + ") Username: " + traversalLoginsForSite.username +
                    " | Password: " + traversalLoginsForSite.password +
                    " | Website: " + traversalLoginsForSite.website);
            count++;
        }

        if (count == 0) {
            System.out.println("No logins exist!");
        }
    }

    // Lists all website logins with the same website name
    public void listWebsiteLogins(String site) {
        System.out.print("Logins for " + site + ": ");
        int count = 0;
        AList<LoginsForSites> temp = new AList<>();
        for (int i = 0; i < loginsForSites.size(); i++) {
            LoginsForSites login = loginsForSites.get(i);
            if (login.website.toLowerCase().equals(site.toLowerCase().trim())) {
                if (count == 0) {
                    System.out.println();
                }
                System.out.println("(Site Index: " + i + ") Username: " + login.username +
                        " | Password: " + login.password);
                count++;
            }
        }
        if (count == 0) {
            System.out.print("None!\n");
        }
    }

    // This stores the login of the specific user, as their username and password will
    // be unique to them.
    public class UserLogins implements Serializable {
        String username;
        String password;

        public UserLogins(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
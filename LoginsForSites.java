import java.io.Serializable;

public class LoginsForSites implements Serializable {
    String username;
    String password;
    String website;

    public LoginsForSites(String username, String password, String website) {
        this.username = username;
        this.password = password;
        this.website = website;
    }
}

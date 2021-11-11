import java.io.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordManagerApp {
    private static final String usernamePattern = "(^\\w)(?<!_)[\\w!@#$%&*]{2,15}";
                                                   // First token must begin with char or number
                                                   // Must be followed by any char, number, _, or !,@,#,$,%,&,*,$
                                                   // Second token be between 2 - 15 char
                                                   // In total 1st token and 2nd will be between len 3-16
    private static Manager run = new Manager();
    private static Stage stage = Stage.STARTUP;
    private static Scanner input = new Scanner(System.in);
    private static String username = "";

    public static void main(String[] args) {
        File f = new File("passman.dat");
        if (f.exists()) {
            readFromFile();
        }

        System.out.println("|!| ---- Welcome! ---- |!|");
        startupLoop();
    }

    // STAGE MESSAGES
    public static void displayMenu(Stage currentStage) {
        System.out.println("Please enter a number: ");
        switch (currentStage) {
            case STARTUP:
                System.out.println("(1) Sign in");
                System.out.println("(2) Create account");
                System.out.println("(3) About");
                System.out.println("(4) Exit");
                break;
            case INUSER:
                System.out.println("(1) List all logins");
                System.out.println("(2) Get specific login(s)");
                System.out.println("(3) Add a new login");
                System.out.println("(4) Update a login");
                System.out.println("(5) Remove a login");
                System.out.println("(6) Sign out");
                System.out.println("(7) Exit");
                break;
        }
    }

    // Displays error messages based on what stage of the program user is in.
    public static void displayError(Stage currentStage) {
        switch (currentStage) {
            case STARTUP:
                System.out.println("Please enter a valid number (1-4).");
                break;
            case INUSER:
                System.out.println("Please enter a valid number (1-7).");
                break;
        }
    }

    // USER MANAGEMENT
    // Startup menu and commands
    public static void startupLoop() {
        boolean loopCondition = true;
        boolean switchLoop = false;
        String userInput = "";
        int parsedInput = -1;


        while (loopCondition) {
            displayMenu(stage);
            try {
                userInput = input.nextLine();
                parsedInput = Integer.parseInt(userInput);
                switch (parsedInput) {
                    case 1: // Sign in
                        switchLoop = signIn();
                        loopCondition = !switchLoop;
                        break;
                    case 2: // Create account
                        switchLoop = createAccount();
                        loopCondition = !switchLoop;
                        if (switchLoop){
                            saveToFile();
                        }
                        break;
                    case 3: // Info
                        System.out.println("Made by Casey Morar as a final project in data structures.");
                        break;
                    case 4: // Exit
                        return;
                    default: // Invalid input
                        System.out.println("Invalid input!");
                }
                if (switchLoop) {
                    break;
                }
            } catch (Exception e) {
                System.out.println("Error! " + "\"" + userInput + "\"" + " is not a valid input.");
                displayError(stage);
            }
        }
        if (switchLoop) {
            stage = Stage.INUSER;
            userInLoop();
        }
    }

    // In account menu and commands
    public static void userInLoop() {
        boolean loopCondition = true;
        int parsedInput = -1;
        String userInput = "";

        while (loopCondition) {
            displayMenu(stage);
            try {
                userInput = input.nextLine();
                parsedInput = Integer.parseInt(userInput);
                switch (parsedInput) {
                    case 1: // Display entire list
                        run.userLogin.get(username).listWebsiteLogins();
                        break;
                    case 2: // Display sublist of certain website
                        System.out.println("Enter the website to search for:");
                        String site = input.nextLine();
                        run.userLogin.get(username).listWebsiteLogins(site);
                        break;
                    case 3: // Add new login
                        String tempUser, tempPassword, tempWebsite;
                        System.out.println("Username: ");
                        tempUser = input.nextLine();
                        System.out.println("Password: ");
                        tempPassword = input.nextLine();
                        System.out.println("Website: ");
                        tempWebsite = input.nextLine();
                        LoginsForSites tempLogin = new LoginsForSites(tempUser, tempPassword, tempWebsite);
                        run.userLogin.get(username).addWebsiteLogin(tempLogin);
                        saveToFile();      
                        break;
                    case 4: // Update login
                        run.userLogin.get(username).listWebsiteLogins();
                        System.out.println("Enter the index of the login you would like to update:");
                        try {
                            int index = Integer.parseInt(input.nextLine());
                            if (index >=0 && index < run.userLogin.get(username).loginsForSites.size()) {
                                LoginsForSites thisLogin = run.userLogin.get(username).loginsForSites.get(index);
                                System.out.println("The username is: " + thisLogin.username);
                                System.out.println("Enter a new username or type '/s' to skip.");
                                String user = input.nextLine();
                                if (user.equals("/s")) {
                                    user = thisLogin.username;
                                }
                                System.out.println("The password is: " + thisLogin.password);
                                System.out.println("Enter a new password or type '/s' to skip.");
                                String pass = input.nextLine();
                                if (pass.equals("/s")) {
                                    pass = thisLogin.password;
                                }
                                if (run.userLogin.get(username).updateWebsiteLogin(index, user, pass)) {
                                    System.out.println("Login updated successfully!");
                                } else {
                                    System.out.println("Could not update!");
                                }
                            } else {
                                System.out.println("Index not found.");
                            }
                        } catch (Exception e) {
                            System.out.println("There was an error updating that!");
                        }
                        break;
                    case 5: // Remove login
                        run.userLogin.get(username).listWebsiteLogins();
                        System.out.println("Enter the index of the login you would like to remove:");
                        try {
                            int index = Integer.parseInt(input.nextLine());
                            run.userLogin.get(username).removeWebsiteLogin(index);
                            System.out.println("Login removed successfully!");
                            saveToFile();
                        } catch (Exception e) {
                            System.out.println("There was an error removing that!");
                        }
                        break;
                    case 6: // Sign out
                        username = null;
                        loopCondition = false;
                        break;
                    case 7: // Exit
                        return;
                    default:
                        System.out.println("Invalid input!");
                }
            } catch (Exception e) {
                System.out.println("Error! " + "\"" + userInput + "\"" + " is not a valid input.");
                displayError(stage);
            }
        }

        if (username == null) {
            System.out.println("You have been signed out!");
            stage = Stage.STARTUP;
            startupLoop();
        }
    }

    // Creates and adds account to the Manager HashDictionary
    public static boolean createAccount() {
        String name = "";
        String pass = "";
        System.out.println("Enter a username:");
        name = input.nextLine().trim();
        if (validUsername(name)) {
            System.out.println("Enter a password:");
            pass = input.nextLine().trim();
            if (pass.length() >= 6) {
                if (run.createAcct(name, pass)) {
                    username = name;
                    System.out.println("Account successfully created!");
                    System.out.println("Username: " + username);
                    System.out.println("Password: " + pass);
                    return true;
                }
            } else {
                System.out.println("Password is too short! It must be greater than or equal to 6 characters long.");
            }
        } else {
            System.out.println("Username is invalid! It must be 3-16 characters, and start with a number or letter.");
        }
        return false;
    }

    // Checks if the username is valid
    public static boolean validUsername(String username) {
        Pattern pattern = Pattern.compile(usernamePattern);
        Matcher matcher = pattern.matcher(username);
        return matcher.matches();
    }

    // Checks if the username input corresponds to an actual account
    public static boolean signIn() {
        String name = "";
        String pass = "";
        System.out.println("Enter your username:");
        name = input.nextLine().trim();
        System.out.println("Enter your password:");
        pass = input.nextLine().trim();
        if (run.login(name, pass)) {
            username = name;
            return true;
        } else {
            System.out.println("Could not sign you in!");
            return false;
        }
    }


    // FILE IO
    public static void saveToFile() {
        try {
            FileOutputStream fos = new FileOutputStream(new File("passman.dat"), false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(run);
            oos.close();
            fos.close();
            System.out.println("Saved.");
        } catch (Exception e) {
            System.out.println("There was an error saving to file.");
        }
    }

    public static void readFromFile() {
        try {
            FileInputStream fis = new FileInputStream(new File("passman.dat"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            run = (Manager)ois.readObject();
            ois.close();
            fis.close();
            System.out.println("Loaded.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("There was an error reading from the file.");
        }
    }
}

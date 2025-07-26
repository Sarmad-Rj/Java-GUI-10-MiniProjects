package P6_Password_Generator;

import com.mongodb.client.*;
import org.bson.Document;
import java.util.Random;
import java.util.Scanner;
import static com.mongodb.client.model.Filters.eq;

public class PasswordManagerCLI {
    private static final String CHARS = "qwertyuiopasdfghjklzxcvbnm[]QWERTYUIOPASDFGHJKLZXCVBNM}{;',./<>?:1234567890!@#$%^&*()_+-=";
    private static final Scanner scanner = new Scanner(System.in);
    private static MongoCollection<Document> collection;

    public static void connectMongo() {
        String uri = System.getenv("MONGODB_URI");
        if (uri == null) {
            System.out.println("Error: MONGODB_URI environment variable not set.");
            System.exit(1);
        }
        MongoClient client = MongoClients.create(uri);
        MongoDatabase database = client.getDatabase("password_manager_db");
        collection = database.getCollection("passwords");
    }

    public static String generatePassword(int length) {
        Random random = new Random();
        StringBuilder pwd = new StringBuilder();
        for (int i = 0; i < length; i++) {
            pwd.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return pwd.toString();
    }

    public static void savePassword(String platform, String username, String password) {
        Document doc = new Document("platform", platform)
                .append("username", username)
                .append("password", password);
        collection.insertOne(doc);
        System.out.println("Password saved successfully.\n");
    }

    public static void showPasswords() {
        FindIterable<Document> docs = collection.find();
        boolean found = false;
        System.out.println("~".repeat(70));
        for (Document doc : docs) {
            found = true;
            System.out.println("ID: " + doc.getObjectId("_id"));
            System.out.println("Platform: " + doc.getString("platform"));
            System.out.println("Username: " + doc.getString("username"));
            System.out.println("Password: " + doc.getString("password"));
            System.out.println("-".repeat(70));
        }
        if (!found) {
            System.out.println("No passwords saved yet.");
        }
        System.out.println("~".repeat(70));
    }

    public static void updatePassword() {
        showPasswords();
        System.out.print("Enter the ID (_id) of the password you want to update: ");
        String id = scanner.nextLine();
        System.out.print("New platform: ");
        String platform = scanner.nextLine();
        System.out.print("New username: ");
        String username = scanner.nextLine();
        System.out.print("New password: ");
        String password = scanner.nextLine();

        collection.updateOne(eq("_id", new org.bson.types.ObjectId(id)),
                new Document("$set", new Document("platform", platform)
                        .append("username", username)
                        .append("password", password)));
        System.out.println("Password updated successfully.\n");
    }

    public static void deletePassword() {
        showPasswords();
        System.out.print("Enter the ID (_id) of the password you want to delete: ");
        String id = scanner.nextLine();
        collection.deleteOne(eq("_id", new org.bson.types.ObjectId(id)));
        System.out.println("Password deleted successfully.\n");
    }

    public static void main(String[] args) {
        connectMongo();
        while (true) {
            System.out.println("~".repeat(50));
            System.out.println("===== Password Manager =====");
            System.out.println("1. Generate a password");
            System.out.println("2. Generate and save a password");
            System.out.println("3. Show saved passwords");
            System.out.println("4. Update a password entry");
            System.out.println("5. Delete a password entry");
            System.out.println("6. Exit");
            System.out.print("Enter your choice (1-6): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("~".repeat(50));
                    System.out.print("Enter password length: ");
                    int len1 = Integer.parseInt(scanner.nextLine());
                    String pwd1 = generatePassword(len1);
                    System.out.println("Generated password: \"" + pwd1 + "\"");
                    System.out.print("Do you want to save this password? (yes/no): ");
                    String save = scanner.nextLine().toLowerCase();
                    if (save.equals("yes")) {
                        System.out.print("Enter platform name: ");
                        String platform1 = scanner.nextLine();
                        System.out.print("Enter username: ");
                        String username1 = scanner.nextLine();
                        savePassword(platform1, username1, pwd1);
                    }
                    break;
                case "2":
                    System.out.println("~".repeat(50));
                    System.out.print("Enter password length: ");
                    int len2 = Integer.parseInt(scanner.nextLine());
                    String pwd2 = generatePassword(len2);
                    System.out.println("Generated password: \"" + pwd2 + "\"");
                    System.out.print("Enter platform name: ");
                    String platform2 = scanner.nextLine();
                    System.out.print("Enter username: ");
                    String username2 = scanner.nextLine();
                    savePassword(platform2, username2, pwd2);
                    break;
                case "3":
                    System.out.println("~".repeat(50));
                    showPasswords();
                    break;
                case "4":
                    System.out.println("~".repeat(50));
                    updatePassword();
                    break;
                case "5":
                    System.out.println("~".repeat(50));
                    deletePassword();
                    break;
                case "6":
                    System.out.println("~".repeat(50));
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

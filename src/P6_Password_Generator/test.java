package P6_Password_Generator;
import io.github.cdimascio.dotenv.Dotenv;

public class test {
    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load(); // loads from .env automatically
        String uri = dotenv.get("MONGODB_URI");
        System.out.println(uri);
        if (uri == null || uri.isEmpty()) {
            System.out.println("Error: MONGODB_URI is not set in your .env file.");
            System.exit(1);
        }
    }
}

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        String[] passwords = {
            "admin123",
            "staff123",
            "cleaner123"
        };

        System.out.println("BCrypt Password Hashes (strength: 10):");
        System.out.println("==========================================");

        for (String password : passwords) {
            String hash = encoder.encode(password);
            System.out.println("Password: " + password);
            System.out.println("Hash:     " + hash);
            System.out.println();
        }
    }
}

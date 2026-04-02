package password_manager;
import java.io.*;
public class PasswordManager {

	public void addAccount(Credential c) {
		try {
			FileWriter writer = new FileWriter("passwords.txt",true);
			writer.write(c.getService()+';'+c.getUsername()+';'+c.getPassword()+"\n");
			writer.close();
			System.out.println("[SUCCESS] Account for " + c.getService() + " saved correctly.");
		} catch (Exception e) {
			System.out.println("[ERROR] Failed to save account. Please try again.");
		}
	}
	
	public void searchService(String service) {
		try {
		BufferedReader reader = new BufferedReader(new FileReader("passwords.txt"));
        String line;
        boolean found = false;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            if (parts[0].equalsIgnoreCase(service)) {
                
                System.out.println("\n[ACCOUNT FOUND]");
                System.out.println("➤ Username: " + parts[1]);
                System.out.println("➤ Password: " + parts[2]+"\n");
                found = true;
            }
        }

        reader.close();

        if (!found) { System.out.println("[INFO] No result found for: " + service);}
        
		} catch (Exception e) { System.out.println("[ERROR] Problem accessing the file during search.");}
	}
	
	public static void showPasswords() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("passwords.txt"));
            String line;

            System.out.println("\n--- Saved Passwords ---");

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                
                System.out.println("Account: " + parts[0]);
                System.out.println("Username: " + parts[1]);
                System.out.println("Password: " + parts[2]);
                System.out.println("------------------------");
            }

            reader.close();
        } catch (Exception e) {
            System.out.println("[INFO] The vault is currently empty or file not found.");
        }
    }
	
	public static void removeService(String service) {
        try {
            File inputFile = new File("passwords.txt");
            File tempFile = new File("temp.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (!parts[0].equalsIgnoreCase(service)) {
                    writer.write(line + System.lineSeparator());
                } else {
                    found = true;
                }
            }

            writer.close();
            reader.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);

            if (found) {
                System.out.println("[SUCCESS] Entry for '" + service + "' removed from vault.");
            } else {
                System.out.println("[INFO] Specified account not found.");
            }
        } catch (Exception e) {
			System.out.println("[ERROR] File operation failed during deletion.");
		}
    }

	public boolean authenticate() {
    Scanner sc = new Scanner(System.in);
    String master = "";

    try {
        File file = new File("master.txt");

        if (!file.exists() || file.length() == 0) {
            System.out.print("[INFO] Vault is empty. Please set a master password: ");
            master = sc.nextLine();

            FileWriter writer = new FileWriter(file);
            writer.write(master);
            writer.close();

            System.out.println("[SUCCESS] Master password saved successfully.");
            return true;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String savedPassword = reader.readLine();
        reader.close();

        System.out.print("Enter master password: ");
        master = sc.nextLine();

        if (savedPassword.equals(master)) {
            return true;
        } else {
            System.out.println("[ERROR] Incorrect password.");
        }

    } catch (IOException e) {
        System.out.println("[ERROR] File error occurred.");
    }

    return false;
	}
}

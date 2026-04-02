package password_manager;
import java.util.Scanner;
	
public class Main {

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		PasswordManager pm = new PasswordManager();
		int choice =-1;
		char check;
		boolean flag=true;

		if(!pm.authenticate()) {
		    return;
		}
		
		while(flag) {
			System.out.println("\n===============================");
			System.out.println("      PASSWORD MANAGER CLI     ");
			System.out.println("===============================");
            System.out.println("1. Add New Account");
            System.out.println("2. Remove Account");
            System.out.println("3. Search Account");
            System.out.println("4. Show All Accounts");
            System.out.println("5. Exit");
            System.out.print("Your choice: ");
			choice = s.nextInt();
			s.nextLine();
			
			switch(choice) {
			case 1:
				System.out.println("\n--- ADDING NEW CREDENTIALS ---");
				System.out.print("➤ Service Name: ");
				String service = s.nextLine();
				System.out.print("➤ Username: ");
				String username = s.nextLine();
				System.out.print("➤ Password: ");
				String password = s.nextLine();
				pm.addAccount(new Credential(service,username,password));	
				break;
			case 2:
				System.out.print("\n➤ Service Name to delete: ");
				service = s.nextLine();
				pm.searchService(service);
				System.out.print("⚠ Confirm deletion? [y/n]: ");
				check = s.next().charAt(0);
				
				switch(check) {
					case 'y':
						pm.removeService(service);
						break;
					case 'n':
						System.out.println("[INFO] Deletion aborted.");
						break;
					default:
						System.out.println("[!] Invalid option.");
				}
				break;
			case 3:
				System.out.print("\n➤ Service Name to search: ");
				service = s.nextLine();
				pm.searchService(service);
				break;
			case 4:
				pm.showPasswords();
				break;
			case 5:
				System.out.println("\n[SUCCESS] Closing Manager. Secure your vault!");
				flag=false;
				break;
			default:
				System.out.println("[!] Selection error. Please try again.");
			}	
		}
	}
}

import java.text.SimpleDateFormat;
import java.util.Scanner;

/*
*ITC205 Asg2 - Group7 (Jasemine, Ian, Meraj)
*/

public class Main {

	private static Scanner IN;
	private static library LIB;
	private static String MENU;
	private static Calendar CAL;
	private static SimpleDateFormat SDF;

	private static String getMenu() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("\nLibrary Main Menu\n\n")
				.append("  M  : Add member\n")
				.append("  LM : List members\n")
				.append("\n")
				.append("  B  : Add book\n")
				.append("  LB : List books\n")
				.append("  FB : Fix books\n")
				.append("\n")
				.append("  L  : Take out a loan\n")
				.append("  R  : Return a loan\n")
				.append("  LL : List loans\n")
				.append("\n")
				.append("  P  : Pay fine\n")
				.append("\n")
				.append("  T  : Increment date\n")
				.append("  Q  : Quit\n")
				.append("\n")
				.append("Please enter you choice : ");

		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		try {
			IN = new Scanner(System.in);
			LIB = library.INSTANCE();
			CAL = Calendar.getInstance();
			SDF = new SimpleDateFormat("dd/MM/yyyy");

			for (member member : LIB.Members()) {
				output(member);
			}
			output(" ");
			for (book book : LIB.Books()) {
				output(book);
			}

			MENU = getMenu();

			boolean userSelection = false;

			while (!userSelection) {

				output("\n" + SDF.format(CAL.Date()));
				String userEntry = input(MENU);

				switch (userEntry.toUpperCase()) {

				case "M":
					addMember();
					break;

				case "LM":
					listMembers();
					break;

				case "B":
					addBook();
					break;

				case "LB":
					listBooks();
					break;

				case "FB":
					fixBooks();
					break;

				case "L":
					borrowBook();
					break;

				case "R":
					returnBook();
					break;

				case "LL":
					listCurrentLoans();
					break;

				case "P":
					payFine();
					break;

				case "T":
					incrementDate();
					break;

				case "Q":
					userSelection = true;
					break;

				default:
					output("\nInvalid option\n");
					break;
				}

				library.SAVE();
			}
		} catch (RuntimeException e) {
			output(e);
		}
		output("\nEnded\n");
	}

	private static void payFine() {
		new PayFineUi(new PayFineControl()).run();
	}

	private static void listCurrentLoans() {
		output("");
		for (loan loan : LIB.CurrentLoans()) {
			output(loan + "\n");
		}
	}

	private static void listBooks() {
		output("");
		for (book book : LIB.Books()) {
			output(book + "\n");
		}
	}

	private static void listMembers() {
		output("");
		for (member member : LIB.Members()) {
			output(member + "\n");
		}
	}

	private static void borrowBook() {
		new BorrowBookUi(new BorrowBookControl()).run();
	}

	private static void returnBook() {
		new ReturnBookUi(new ReturnBookControl()).run();
	}

	private static void fixBooks() {
		new FixBookUi(new FixBookControl()).run();
	}

	private static void incrementDate() {
		try {
			int days = Integer.valueOf(input("Enter number of days: ")).intValue();
			CAL.incrementDate(days);
			LIB.checkCurrentLoans();
			output(SDF.format(CAL.Date()));

		} catch (NumberFormatException e) {
			output("\nInvalid number of days\n");
		}
	}

	private static void addBook() {

		String author = input("Enter author: ");
		String title = input("Enter title: ");
		String callNo = input("Enter call number: ");
		book book = LIB.Add_book(author, title, callNo);
		output("\n" + book + "\n");

	}

	private static void addMember() {
		try {
			String lastName = input("Enter last name: ");
			String firstName = input("Enter first name: ");
			String email = input("Enter email: ");
			int phoneNo = Integer.valueOf(input("Enter phone number: ")).intValue();
			member member = LIB.Add_mem(lastName, firstName, email, phoneNo);
			output("\n" + member + "\n");

		} catch (NumberFormatException e) {
			output("\nInvalid phone number\n");
		}

	}

	private static String input(String prompt) {
		System.out.print(prompt);
		return IN.nextLine();
	}

	private static void output(Object object) {
		System.out.println(object);
	}

}

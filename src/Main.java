import java.text.SimpleDateFormat;
import java.util.Scanner;

/*
*ITC205 Asg2 - Group7 (Jasmine, Ian, Meraj)
*/

public class Main {

	private static Scanner userInput;
	private static Library library;
	private static String menu;
	private static Calendar calender;
	private static SimpleDateFormat simpleDateFormat;

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
			userInput = new Scanner(System.in);
			library = Library.INSTANCE();
			calender = Calendar.getInstance();
			simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

			for (Member member : library.members()) {
				output(member);
			}
			output(" ");
			for (book book : library.books()) {
				output(book);
			}

			menu = getMenu();

			boolean userSelection = false;

			while (!userSelection) {

				output("\n" + simpleDateFormat.format(calender.Date()));
				String userEntry = input(menu);

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

				Library.save();
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
		for (loan loan : library.currentLoans()) {
			output(loan + "\n");
		}
	}

	private static void listBooks() {
		output("");
		for (book book : library.books()) {
			output(book + "\n");
		}
	}

	private static void listMembers() {
		output("");
		for (Member member : library.members()) {
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
			calender.incrementDate(days);
			library.checkCurrentLoans();
			output(simpleDateFormat.format(calender.Date()));

		} catch (NumberFormatException e) {
			output("\nInvalid number of days\n");
		}
	}

	private static void addBook() {

		String author = input("Enter author: ");
		String title = input("Enter title: ");
		String callNo = input("Enter call number: ");
		book book = library.addBook(author, title, callNo);
		output("\n" + book + "\n");

	}

	private static void addMember() {
		try {
			String lastName = input("Enter last name: ");
			String firstName = input("Enter first name: ");
			String email = input("Enter email: ");
			int phoneNo = Integer.valueOf(input("Enter phone number: ")).intValue();
			Member member = library.addMember(lastName, firstName, email, phoneNo);
			output("\n" + member + "\n");

		} catch (NumberFormatException e) {
			output("\nInvalid phone number\n");
		}

	}

	private static String input(String prompt) {
		System.out.print(prompt);
		return userInput.nextLine();
	}

	private static void output(Object object) {
		System.out.println(object);
	}

}

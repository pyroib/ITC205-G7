import java.text.SimpleDateFormat;
import java.util.Scanner;

//ITC205 Asg2 - Group7 (Jasmine, Ian, Meraj)

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
				memberOutput(member);
			}
			memberOutput(" ");
			for (Book book : library.books()) {
				memberOutput(book);
			}

			menu = getMenu();

			boolean userSelection = false;

			while (!userSelection) {

				memberOutput("\n" + simpleDateFormat.format(calender.setDate()));
				String userEntry = memberInput(menu);

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
					memberOutput("\nInvalid option\n");
					break;
				}

				Library.save();
			}
		} catch (RuntimeException e) {
			memberOutput(e);
		}
		memberOutput("\nEnded\n");
	}

	
	private static void payFine() {
		PayFineControl payFineControl = new PayFineControl();
		PayFineUi payFineUi = new PayFineUi(payFineControl);
		payFineUi.run();	
	}

	
	private static void listCurrentLoans() {
		memberOutput("");
		for (loan loan : library.currentLoans()) {
			memberOutput(loan + "\n");
		}
	}

	
	private static void listBooks() {
		memberOutput("");
		for (Book book : library.books()) {
			memberOutput(book + "\n");
		}
	}

	
	private static void listMembers() {
		memberOutput("");
		for (Member member : library.members()) {
			memberOutput(member + "\n");
		}
	}

	
	private static void borrowBook() {
		BorrowBookControl borrowBookControl = new BorrowBookControl();
		BorrowBookUi borrowBookUi = new BorrowBookUi(borrowBookControl);
		borrowBookUi.run();	
	}


	private static void returnBook() {
		ReturnBookControl returnBookControl = new ReturnBookControl();
		ReturnBookUi returnBookUi = new ReturnBookUi(returnBookControl);
		returnBookUi.run();	
	}

	
	private static void fixBooks() {
		FixBookControl fixBookControl = new FixBookControl();
		FixBookUi fixBookUi = new FixBookUi(fixBookControl);
		fixBookUi.run();	
	}

	
	private static void incrementDate() {
		try {
			int days = Integer.valueOf(memberInput("Enter number of days: ")).intValue();
			calender.incrementDate(days);
			library.checkCurrentLoans();
			memberOutput(simpleDateFormat.format(calender.setDate()));

		} catch (NumberFormatException e) {
			memberOutput("\nInvalid number of days\n");
		}
	}

	
	private static void addBook() {
		String author = memberInput("Enter author: ");
		String title = memberInput("Enter title: ");
		String callNo = memberInput("Enter call number: ");
		Book book = library.addBook(author, title, callNo);
		memberOutput("\n" + book + "\n");

	}

	
	private static void addMember() {
		try {
			String lastName = memberInput("Enter last name: ");
			String firstName = memberInput("Enter first name: ");
			String email = memberInput("Enter email: ");
			int phoneNo = Integer.valueOf(memberInput("Enter phone number: ")).intValue();
			Member member = library.addMember(lastName, firstName, email, phoneNo);
			memberOutput("\n" + member + "\n");

		} catch (NumberFormatException e) {
			memberOutput("\nInvalid phone number\n");
		}

	}

	
	private static String memberInput(String prompt) {
		System.out.print(prompt);
		return userInput.nextLine();
	}

	
	private static void memberOutput(Object object) {
		System.out.println(object);
	}

	
}


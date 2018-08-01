import java.util.Scanner;

public class FixBookUi {

	public static enum UiState {
		INITIALISED, READY, FIXING, COMPLETED
	};

	private FixBookControl control;
	private Scanner input;
	private UiState state;

	public FixBookUi(FixBookControl control) {
		this.control = control;
		input = new Scanner(System.in);
		state = UiState.INITIALISED;
		control.setUi(this);
	}

	public void setState(UiState state) {
		this.state = state;
	}

	public void run() {
		printOutput("Fix Book Use Case UI\n");

		while (true) {

			switch (state) {

			case READY:
				String bookStr = getUserInput("Scan Book (<enter> completes): ");
				if (bookStr.length() == 0) {
					control.scanningComplete();
				} else {
					try {
						int bookId = Integer.valueOf(bookStr).intValue();
						control.bookScanned(bookId);
					} catch (NumberFormatException e) {
						printOutput("Invalid bookId");
					}
				}
				break;

			case FIXING:
				String answer = getUserInput("Fix Book? (Y/N) : ");
				boolean mustFix = false;
				if (answer.toUpperCase().equals("Y")) {
					mustFix = true;
				}
				control.fixBook(mustFix);
				break;

			case COMPLETED:
				printOutput("Fixing process complete");
				return;

			default:
				printOutput("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + state);
			}
		}
	}

	private String getUserInput(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}

	private void printOutput(Object object) {
		System.out.println(object);
	}

	public void display(Object object) {
		printOutput(object);
	}

}

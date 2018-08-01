import java.util.Scanner;

public class PayFineUi {

	public static enum UiState {
		INITIALISED, READY, PAYING, COMPLETED, CANCELLED
	};

	private PayFineControl control;
	private Scanner input;
	private UiState state;

	public PayFineUi(PayFineControl control) {
		this.control = control;
		input = new Scanner(System.in);
		state = UiState.INITIALISED;
		control.setUi(this);
	}

	public void setState(UiState state) {
		this.state = state;
	}

	public void run() {
		printOutput("Pay Fine Use Case UI\n");

		while (true) {

			switch (state) {

			case READY:
				String memberStr = userInput("Swipe member card (press <enter> to cancel): ");
				if (memberStr.length() == 0) {
					control.cancelState();
					break;
				}
				try {
					int memberId = Integer.valueOf(memberStr).intValue();
					control.cardSwiped(memberId);
				} catch (NumberFormatException e) {
					printOutput("Invalid memberId");
				}
				break;

			case PAYING:
				double amount = 0;
				String amountStr = userInput("Enter amount (<Enter> cancels) : ");
				if (amountStr.length() == 0) {
					control.cancelState();
					break;
				}
				try {
					amount = Double.valueOf(amountStr).doubleValue();
				} catch (NumberFormatException e) {
				}
				if (amount <= 0) {
					printOutput("Amount must be positive");
					break;
				}
				control.payFine(amount);
				break;

			case CANCELLED:
				printOutput("Pay Fine process cancelled");
				return;

			case COMPLETED:
				printOutput("Pay Fine process complete");
				return;

			default:
				printOutput("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + state);
			}
		}
	}

	private String userInput(String prompt) {
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

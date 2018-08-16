import java.util.Scanner;

public class BorrowBookUi {

	
    public static enum UiState {
        INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED
    };
    private BorrowBookControl control;
    private Scanner input;
    private UiState state;

    
    public BorrowBookUi(BorrowBookControl control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UiState.INITIALISED;
        control.setUi(this);
    }

    
    public void setState(UiState state) {
        this.state = state;
    }

    
    public void run() {
        printOutput("Borrow Book Use Case UI\n");
        while (true) {
            switch (state) {
                case CANCELLED:
                    printOutput("Borrowing Cancelled");
                    return;
                case READY:
                    String memberString = getInput("Swipe member card (press <enter> to cancel): ");
                    int memberLength = memberString.length();
                    if (memberLength == 0) {
                        control.cancelBorrowing();
                        break;
                    }
                    try {
                        int memberId = Integer.valueOf(memberString).intValue();
                        control.swipedCard(memberId);
                    } catch (NumberFormatException excepted1) {
                        printOutput("Invalid Member Id");
                    }
                    break;
            case RESTRICTED:
                getInput("Press <any key> to cancel");
                control.cancelBorrowing();
                break;
            case SCANNING:
                String bookString = getInput("Scan Book (<enter> completes): ");
                int bookLength = bookString.length();
                if (bookLength == 0) {
                    control.completeBorrowing();
                    break;
                }
                try {
                    int bookId = Integer.valueOf(bookString).intValue();
                    control.scannedBook(bookId);
                } catch (NumberFormatException excepted2) {
                    printOutput("Invalid Book Id");
                }
                break;
            case FINALISING:
                String answer = getInput("Commit loans? (Y/N): ");
                boolean answerNo = answer.toUpperCase().equals("N");
                if (answerNo) {
                    control.cancelBorrowing();
                } else {
                    control.commitLoans();
                    getInput("Press <any key> to complete ");
                }
                break;
            case COMPLETED:
                printOutput("Borrowing Completed");
                return;
            default:
                printOutput("Unhandled state");
                throw new RuntimeException("BorrowBookUi : unhandled state :" + state);
            }
        }
    }
    
    
    public void displayOutput(Object object) {
        printOutput(object);
    }
    
    
    private String getInput(String prompt) {
        System.out.print(prompt);
        String userInput = input.nextLine();
        return userInput;
    }

    
    private void printOutput(Object object) {
        System.out.println(object);
    }

    
}

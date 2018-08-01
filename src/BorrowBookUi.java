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

    private String getInput(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    private void printOutput(Object object) {
        System.out.println(object);
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
              if (memberString.length() == 0) {
                control.cancel();
                break;
              }
              try {
                int memberId = Integer.valueOf(memberString).intValue();
                control.swiped(memberId);
              } catch (NumberFormatException e) {
                printOutput("Invalid Member Id");
              }
              break;
            case RESTRICTED:
              getInput("Press <any key> to cancel");
              control.cancel();
              break;
            case SCANNING:
              String bookString = getInput("Scan Book (<enter> completes): ");
              if (bookString.length() == 0) {
                control.complete();
                break;
              }
              try {
                int bookId = Integer.valueOf(bookString).intValue();
                control.scanned(bookId);
              } catch (NumberFormatException e) {
                printOutput("Invalid Book Id");
              }
              break;
            case FINALISING:
              String answer = getInput("Commit loans? (Y/N): ");
              if (answer.toUpperCase().equals("N")) {
                control.cancel();
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

    public void display(Object object) {
        printOutput(object);
    }

}

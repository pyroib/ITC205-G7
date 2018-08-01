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
        output("Borrow Book Use Case UI\n");
        while (true) {
          switch (state) {
            case CANCELLED:
              output("Borrowing Cancelled");
              return;
            case READY:
              String memberString = input("Swipe member card (press <enter> to cancel): ");
              if (memberString.length() == 0) {
                control.cancel();
                break;
              }
              try {
                int memberId = Integer.valueOf(memStr).intValue();
                control.swiped(memberId);
              } catch (NumberFormatException e) {
                output("Invalid Member Id");
              }
              break;
            case RESTRICTED:
              input("Press <any key> to cancel");
              control.cancel();
              break;
            case SCANNING:
              String bookString = input("Scan Book (<enter> completes): ");
              if (bookString.length() == 0) {
                control.complete();
                break;
              }
              try {
                int bookId = Integer.valueOf(bookStr).intValue();
                control.scanned(bookId);
              } catch (NumberFormatException e) {
                output("Invalid Book Id");
              }
              break;
            case FINALISING:
              String answer = input("Commit loans? (Y/N): ");
              if (answer.toUpperCase().equals("N")) {
                control.cancel();
              } else {
                control.commitLoans();
                input("Press <any key> to complete ");
              }
              break;
            case COMPLETED:
              output("Borrowing Completed");
              return;
            default:
              output("Unhandled state");
              throw new RuntimeException("BorrowBookUi : unhandled state :" + state);
          }
        }
    }

    public void display(Object object) {
        output(object);
    }

}

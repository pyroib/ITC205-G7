import java.util.Scanner;

public class ReturnBookUi {

    public static enum UiState { INITIALISED, READY, INSPECTING, COMPLETED };

    private ReturnBookControl control;
    private Scanner input;
    private UiState state;

    
    public ReturnBookUi(ReturnBookControl control) {
        this.control = control;
        input = new Scanner(System.in);
        state = UiState.INITIALISED;
        control.setUi(this);
    }

    
    public void run() {
        printOutput("Return Book Use Case UI\n");
        while (true) {
          switch (state) {
            case INITIALISED:
              break;

            case READY:
              String bookStr = getInput("Scan Book (<enter> completes): ");
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

            case INSPECTING:
              String answer = getInput("Is book damaged? (Y/N): ");
              boolean isDamaged = false;
              if (answer.toUpperCase().equals("Y")) {
                isDamaged = true;
              }
              control.dischargeLoan(isDamaged);
              break;
                   
            case COMPLETED:
              printOutput("Return processing complete");
              break;

            default:
              printOutput("Unhandled state");
              throw new RuntimeException("ReturnBookUI : unhandled state :" + state);
        }
      }
    }

    
    private String getInput(String prompt) {
        System.out.print(prompt);
        String promptInput = input.nextLine();
        return promptInput;
    }

    
    private void printOutput(Object object) {
        System.out.println(object);
    }

    
    public void displayOutput(Object object) {
        printOutput(object);
    }

    
    public void setState(UiState state) {
        this.state = state;
    }

}

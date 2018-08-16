import java.util.Scanner;

public class ReturnBookUi {
    
    public static enum UiState {
        INITIALISED, READY, INSPECTING, COMPLETED
    };
    private ReturnBookControl returnBookControl;
    private Scanner userInput;
    private UiState uiState;
    
    
    public ReturnBookUi(ReturnBookControl control) {
        this.returnBookControl = control;
        userInput = new Scanner(System.in);
        uiState = UiState.INITIALISED;
        control.setUi(this);
    }
    
    
    public void runReturnBook() {
        printOutput("Return Book Use Case UI\n");
        while (true) {
            switch (uiState) {
                case INITIALISED:
                    break;
                
                case READY:
                    String BookInputString = getInput("Scan Book (<enter> completes): ");
                    int BookInputStringLength = BookInputString.length();
                    if (BookInputStringLength == 0) {
                        returnBookControl.scanningComplete();
                    } else {
                        try {
                            int bookId = Integer.valueOf(BookInputString).intValue();
                            returnBookControl.bookScanned(bookId);
                        } catch (NumberFormatException numberFormatException) {
                            printOutput("Invalid bookId");
                        }
                    }
                    break;
                
                case INSPECTING:
                    String answer = getInput("Is book damaged? (Y/N): ");
                    boolean isDamaged = false;
                    String answerUpper = answer.toUpperCase();
                    if (answerUpper.equals("Y")) {
                        isDamaged = true;
                    }
                    returnBookControl.dischargeLoan(isDamaged);
                    break;
                
                case COMPLETED:
                    printOutput("Return processing complete");
                    break;
                
                default:
                    printOutput("Unhandled state");
                    throw new RuntimeException("ReturnBookUI : unhandled state :" + uiState);
            }
        }
    }
    
    
    private String getInput(String prompt) {
        System.out.print(prompt);
        String promptInput = userInput.nextLine();
        return promptInput;
    }
    
    
    public void printOutput(Object object) {
        System.out.println(object);
    }
    
    
    public void setState(UiState state) {
        this.uiState = state;
    }
    
}

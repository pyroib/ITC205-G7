import java.util.Scanner;

public class FixBookUi {
    
    public static enum UiState {
        INITIALISED, READY, FIXING, COMPLETED
    };
    private FixBookControl fixBookControl;
    private Scanner userInput;
    private UiState uiState;
    
    
    public FixBookUi(FixBookControl fixBookControl) {
        this.fixBookControl = fixBookControl;
        userInput = new Scanner(System.in);
        uiState = UiState.INITIALISED;
        fixBookControl.setUi(this);
    }
    
    
    public void setUiState(UiState state) {
        this.uiState = state;
    }
    
    
    public void runFixBookUi() {
        printOutput("Fix Book Use Case UI\n");
        
        while (true) {
            
            switch (uiState) {
                
                case READY:
                    String bookStr = getUserInput("Scan Book (<enter> completes): ");
                    int bookLength = bookStr.length();
                    if (bookLength == 0) {
                        fixBookControl.scanningComplete();
                    } else {
                        try {
                            int bookId = Integer.valueOf(bookStr).intValue();
                            fixBookControl.bookScanned(bookId);
                        } catch (NumberFormatException numberFormatException) {
                            printOutput("Invalid bookId");
                        }
                    }
                    break;
                
                case FIXING:
                    String answer = getUserInput("Fix Book? (Y/N) : ");
                    String uppercaseAnswer = answer.toUpperCase();
                    boolean mustFix = false;
                    if (uppercaseAnswer.equals("Y")) {
                        mustFix = true;
                    }
                    fixBookControl.fixBook(mustFix);
                    break;
                
                case COMPLETED:
                    printOutput("Fixing process complete");
                    return;
                
                default:
                    printOutput("Unhandled state");
                    throw new RuntimeException("FixBookUI : unhandled state :" + uiState);
            }
        }
    }
    
    
    private String getUserInput(String prompt) {
        System.out.print(prompt);
        return userInput.nextLine();
    }
    
    
    public void printOutput(Object object) {
        System.out.println(object);
    }
    
}

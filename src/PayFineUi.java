import java.util.Scanner;

public class PayFineUi {
    
    
    public static enum UiState {
        INITIALISED, READY, PAYING, COMPLETED, CANCELLED
    };
    private PayFineControl payFineControl;
    private Scanner scannerInput;
    private UiState uiState;
    
    
    public PayFineUi(PayFineControl control) {
        this.payFineControl = control;
        scannerInput = new Scanner(System.in);
        uiState = UiState.INITIALISED;
        control.setUi(this);
    }
    
    
    public void setState(UiState state) {
        this.uiState = state;
    }
    
    
    public void run() {
        printOutput("Pay Fine Use Case UI\n");
        
        while (true) {
            
            switch (uiState) {
                
                case READY:
                    String memberCard = userInput("Swipe member card (press <enter> to cancel): ");
                    int memberCardLength = memberCard.length();
                    if (memberCardLength == 0) {
                        payFineControl.cancelState();
                        break;
                    }
                    try {
                        int memberId = Integer.valueOf(memberCard).intValue();
                        payFineControl.cardSwiped(memberId);
                    } catch (NumberFormatException numberFormatException) {
                        printOutput("Invalid memberId");
                    }
                    break;
                
                case PAYING:
                    double amountPaying = 0;
                    String amountPayingInput = userInput("Enter amount (<Enter> cancels) : ");
                    int amountPayingInputLength = amountPayingInput.length();
                    if (amountPayingInputLength == 0) {
                        payFineControl.cancelState();
                        break;
                    }
                    try {
                        amountPaying = Double.valueOf(amountPayingInput).doubleValue();
                    } catch (NumberFormatException numberFormatException) {
                        
                    }
                    if (amountPaying <= 0) {
                        printOutput("Amount must be positive");
                        break;
                    }
                    payFineControl.payFine(amountPaying);
                    break;
                
                case CANCELLED:
                    printOutput("Pay Fine process cancelled");
                    return;
                
                case COMPLETED:
                    printOutput("Pay Fine process complete");
                    return;
                
                default:
                    printOutput("Unhandled state");
                    throw new RuntimeException("FixBookUI : unhandled state :" + uiState);
            }
        }
    }
    
    
    public void printOutput(Object object) {
        System.out.println(object);
    }
    
    
    private String userInput(String prompt) {
        System.out.print(prompt);
        return scannerInput.nextLine();
    }
    

}

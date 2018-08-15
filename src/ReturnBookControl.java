public class ReturnBookControl {
    
    
    private enum ControlState {
        INITIALISED, READY, INSPECTING
    };
    private ReturnBookUi ui;
    private ControlState controlState;
    private Library library;
    private Loan currentLoan;
    
    
    public ReturnBookControl() {
        this.library = Library.INSTANCE();
        controlState = ControlState.INITIALISED;
    }
    
    
    public void setUi(ReturnBookUi ui) {
        Boolean currentControlStateInitialised  = controlState.equals(ControlState.INITIALISED);
        
        if (!currentControlStateInitialised) {
            throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED " + 
                    "state");
        }
        
        this.ui = ui;
        ui.setState(ReturnBookUi.UiState.READY);
        controlState = ControlState.READY;
    }
    
    
    public void bookScanned(int bookId) {
        Boolean currentControlStateReady = controlState.equals(ControlState.READY);
        if (!currentControlStateReady) {
            throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY " + 
                    "state");
        }
        
        Book currentBook = library.book(bookId);
        if (currentBook == null) {
            ui.printOutput("Invalid Book Id");
            return;
        }
        
        Boolean isCurrentBookOnLoan = currentBook.onLoan();
        if (!isCurrentBookOnLoan) {
            ui.printOutput("Book has not been borrowed");
            return;
        }
        
        currentLoan = library.getLoanByBookId(bookId);
        double overDueFine = 0.0;
       
        Boolean isCurrentLoanOverDue = currentLoan.isOverDue();
        if (isCurrentLoanOverDue) {
            overDueFine = library.calculateOverDueFine(currentLoan);
        }
        
        ui.printOutput("Inspecting");
        String currentBookString = currentBook.toString();
        ui.printOutput(currentBookString);
        
        String currentLoanString = currentBook.toString();
        ui.printOutput(currentLoanString);
        
        if (isCurrentLoanOverDue) {
            ui.printOutput(String.format("\nOverdue fine : $%.2f", overDueFine));
        }
        
        ui.setState(ReturnBookUi.UiState.INSPECTING);
        controlState = ControlState.INSPECTING;
    }
    
    public void scanningComplete() {
        Boolean currentControlStateReady = controlState.equals(ControlState.READY);
        if (!currentControlStateReady) {
            throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in " + 
                    "READY state");
        }
        
        ui.setState(ReturnBookUi.UiState.COMPLETED);
    }
    
    public void dischargeLoan(boolean isDamaged) {
        Boolean currentControlStateInpecting = controlState.equals(ControlState.INSPECTING);
        
        if (!currentControlStateInpecting) {
            throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in " + 
                    "INSPECTING state");
        }
        
        library.dischargeLoan(currentLoan, isDamaged);
        currentLoan = null;
        ui.setState(ReturnBookUi.UiState.READY);
        controlState = ControlState.READY;
    }
    
}

public class ReturnBookControl {
    
    private ReturnBookUi UI;
    private enum ControlState {
        INITIALISED, READY, INSPECTING
    };
    private ControlState state;
    private Library library;
    private Loan currentLoan;
  
    
    @SuppressWarnings("static-access")
    public ReturnBookControl() {
        this.library = library.INSTANCE();
        state = ControlState.INITIALISED;
    }
    
    
    public void setUi(ReturnBookUi UI) {
        if (!state.equals(ControlState.INITIALISED)) {
          throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED "
                  + "state");
        }
        
        this.UI = UI;
        UI.setState(ReturnBookUi.UiState.READY);
        state = ControlState.READY;
    }

  
    public void bookScanned(int bookId) {
        if (!state.equals(ControlState.READY)) {
          throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY "
                  + "state");
        }
        
        Book currentBook = library.book(bookId);
        if (currentBook == null) {
          UI.displayOutput("Invalid Book Id");
          return;
        }
        
        if (!currentBook.onLoan()) {
          UI.displayOutput("Book has not been borrowed");
          return;
        }
        
        currentLoan = library.getLoanByBookId(bookId);
        double overDueFine = 0.0;
        if (currentLoan.isOverDue()) {
          overDueFine = library.calculateOverDueFine(currentLoan);
        }
        
        UI.displayOutput("Inspecting");
        UI.displayOutput(currentBook.toString());
        UI.displayOutput(currentLoan.toString());
        if (currentLoan.isOverDue()) {
          UI.displayOutput(String.format("\nOverdue fine : $%.2f", overDueFine));
        }
        
        UI.setState(ReturnBookUi.UiState.INSPECTING);
        state = ControlState.INSPECTING;
    }
    
    
    public void scanningComplete() {
        if (!state.equals(ControlState.READY)) {
          throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in "
                  + "READY state");
        }
        
        UI.setState(ReturnBookUi.UiState.COMPLETED);
    }

  
    public void dischargeLoan(boolean isDamaged) {
        if (!state.equals(ControlState.INSPECTING)) {
          throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in "
                  + "INSPECTING state");
        }
        
        library.dischargeLoan(currentLoan, isDamaged);
        currentLoan = null;
        UI.setState(ReturnBookUi.UiState.READY);
        state = ControlState.READY;
    }
  
    
}

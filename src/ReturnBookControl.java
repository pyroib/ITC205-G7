public class ReturnBookControl {

    private ReturnBookUi ui;

    private enum CONTROL_STATE {
        INITIALISED, READY, INSPECTING
    };

    private CONTROL_STATE state;

    private library library;
    private loan currentLoan;

    public ReturnBookControl() {
        this.library = library.INSTANCE();
        state = CONTROL_STATE.INITIALISED;
    }

    public void setUI(ReturnBookUi ui) {
        if (!state.equals(CONTROL_STATE.INITIALISED)) {
          throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
        }
        
        this.ui = ui;
        ui.setState(ReturnBookUi.UiState.READY);
        state = CONTROL_STATE.READY;
    }

    public void bookScanned(int bookId) {
        if (!state.equals(CONTROL_STATE.READY)) {
          throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
        }

        book currentBook = library.Book(bookId);
        if (currentBook == null) {
          ui.displayOutput("Invalid Book Id");
          return;
        }

        if (!currentBook.On_loan()) {
          ui.displayOutput("Book has not been borrowed");
          return;
        }

        currentLoan = library.getLoanByBookId(bookId);
        double overDueFine = 0.0;
        if (currentLoan.isOverDue()) {
          overDueFine = library.calculateOverDueFine(currentLoan);
        }

        ui.displayOutput("Inspecting");
        ui.displayOutput(currentBook.toString());
        ui.displayOutput(currentLoan.toString());

        if (currentLoan.isOverDue()) {
          ui.displayOutput(String.format("\nOverdue fine : $%.2f", overDueFine));
        }
        
        ui.setState(ReturnBookUi.UiState.INSPECTING);
        state = CONTROL_STATE.INSPECTING;
    }

    public void scanningComplete() {
        if (!state.equals(CONTROL_STATE.READY)) {
          throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
        }
        
        ui.setState(ReturnBookUi.UiState.COMPLETED);
    }

    public void dischargeLoan(boolean isDamaged) {
        if (!state.equals(CONTROL_STATE.INSPECTING)) {
          throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
        }
        library.dischargeLoan(currentLoan, isDamaged);
        currentLoan = null;
        ui.setState(ReturnBookUi.UiState.READY);
        state = CONTROL_STATE.READY;
    }

}

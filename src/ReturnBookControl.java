public class ReturnBookControl {
    
    private ReturnBookUI UI;

    private enum ControlState {
        INITIALISED, 
        READY, 
        INSPECTING
    };

    private ControlState state;

    private library library;
    private loan currentLoan;

    @SuppressWarnings("static-access")
    public ReturnBookControl() {
        this.library = library.INSTANCE();
        state = ControlState.INITIALISED;
    }

    public void setUI(ReturnBookUI UI) {
        if (!state.equals(ControlState.INITIALISED)) {
            throw new RuntimeException("ReturnBookControl: cannot call setUI "
                    + "except in INITIALISED state");
        }
        this.UI = UI;
        UI.setState(ReturnBookUI.UI_STATE.READY);
        state = ControlState.READY;
    }

    public void bookScanned(int bookId) {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call "
                    + "bookScanned except in READY state");
        }
        book currentBook = library.Book(bookId);

        if (currentBook == null) {
            UI.display("Invalid Book Id");
            return;
        }
        if (!currentBook.On_loan()) {
            UI.display("Book has not been borrowed");
            return;
        }
        currentLoan = library.getLoanByBookId(bookId);
        double overDueFine = 0.0;
        if (currentLoan.isOverDue()) {
            overDueFine = library.calculateOverDueFine(currentLoan);
        }
        UI.display("Inspecting");
        UI.display(currentBook.toString());
        UI.display(currentLoan.toString());

        if (currentLoan.isOverDue()) {
            UI.display(String.format("\nOverdue fine : $%.2f", overDueFine));
        }
        UI.setState(ReturnBookUI.UI_STATE.INSPECTING);
        state = ControlState.INSPECTING;
    }

    public void scanningComplete() {
        if (!state.equals(ControlState.READY)) {
            throw new RuntimeException("ReturnBookControl: cannot call scanningComplete "
                    + "except in READY state");
        }
        UI.setState(ReturnBookUI.UI_STATE.COMPLETED);
    }

    public void dischargeLoan(boolean isDamaged) {
        if (!state.equals(ControlState.INSPECTING)) {
            throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan "
                    + "except in INSPECTING state");
        }
        library.dischargeLoan(currentLoan, isDamaged);
        currentLoan = null;
        UI.setState(ReturnBookUI.UI_STATE.READY);
        state = ControlState.READY;
    }

}

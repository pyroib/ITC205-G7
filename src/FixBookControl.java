public class FixBookControl {
    
    
    private FixBookUi ui;
    private ControlState controlState;
    private Library library;
    private Book currentBook;
    
    private enum ControlState {
        INITIALISED, READY, FIXING
    };
    
    
    @SuppressWarnings("static-access")
    public FixBookControl() {
        this.library = library.INSTANCE();
        controlState = ControlState.INITIALISED;
    }
    
    
    public void setUi(FixBookUi ui) {
        Boolean currentControlStateInitialised = controlState.equals(ControlState.READY);
        
        if (!currentControlStateInitialised) {
            throw new RuntimeException("FixBookControl: cannot call setUI except in " + 
        "INITIALISED state");
        }
        this.ui = ui;
        ui.setState(FixBookUi.UiState.READY);
        controlState = ControlState.READY;
    }
    
    
    public void bookScanned(int bookId) {
        Boolean currentControlStateReady = controlState.equals(ControlState.READY);
        
        if (!currentControlStateReady) {
            throw new RuntimeException("FixBookControl: cannot call bookScanned except in " + 
                    "READY state");
        }
        
        currentBook = library.book(bookId);
        
        if (currentBook == null) {
            ui.printOutput("Invalid bookId");
            return;
        }
        
        Boolean isBookDamaged = currentBook.isDamaged();
        
        if (!isBookDamaged) {
            ui.printOutput("\"Book has not been damaged");
            return;
        }
        
        String displayMessage = currentBook.toString();
        
        ui.printOutput(displayMessage);
        ui.setState(FixBookUi.UiState.FIXING);
        controlState = ControlState.FIXING;
    }
    
    
    public void fixBook(boolean mustFix) {
        Boolean currentControlStateFixing = controlState.equals(ControlState.FIXING);
        
        if (!currentControlStateFixing) {
            throw new RuntimeException("FixBookControl: cannot call fixBook except in " + 
                    "FIXING state");
        }
        if (mustFix) {
            library.repairBook(currentBook);
        }
        currentBook = null;
        ui.setState(FixBookUi.UiState.READY);
        controlState = ControlState.READY;
    }
    
    
    public void scanningComplete() {
        Boolean currentControlStateReady = controlState.equals(ControlState.READY);
        if (!currentControlStateReady) {
            throw new RuntimeException("FixBookControl: cannot call scanningComplete " + 
                    "except in READY state");
        }
        ui.setState(FixBookUi.UiState.COMPLETED);
    }
    
}

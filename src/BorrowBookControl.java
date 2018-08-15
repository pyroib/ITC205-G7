import java.util.ArrayList;
import java.util.List;

public class BorrowBookControl {
    
    private BorrowBookUi ui;
    private Library library;
    private Member member;  
    private enum ControlState { 
        INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED 
    };
    private ControlState state;
    private List<Book> pending;
    private List<Loan> completed;
    private Book book;
    
    
    public BorrowBookControl() {
        this.library = library.INSTANCE();
        state = ControlState.INITIALISED;
    }
    

    public void setUi(BorrowBookUi ui) {
    	boolean borrowState = state.equals(ControlState.INITIALISED);
        if (!borrowState) {
            throw new RuntimeException("BorrowBookControl: cannot call setUI except "
                + "in INITIALISED state");
        }
        this.ui = ui;
        ui.setState(BorrowBookUi.UiState.READY);
        state = ControlState.READY;        
    }

        
    public void swipedCard(int memberId) {
    	boolean swipeState = state.equals(ControlState.READY);
        if (!swipeState) {
            throw new RuntimeException("BorrowBookControl: cannot call cardSwiped "
                + "except in READY state");
        }    
        member = library.getMember(memberId);
        if (member == null) {
            ui.displayOutput("Invalid memberId");
            return;
        }
        boolean canBorrow = library.memberCanBorrow(member);
        if (canBorrow) {
            pending = new ArrayList<>();
            ui.setState(BorrowBookUi.UiState.SCANNING);
            state = ControlState.SCANNING; 
        } else {
            ui.displayOutput("Member cannot borrow at this time");
            ui.setState(BorrowBookUi.UiState.RESTRICTED); 
        }
    }
    
    
    public void scannedBook(int bookId) {
        book = null;
        boolean scanning = state.equals(ControlState.SCANNING);
        if (!scanning) {
            throw new RuntimeException("BorrowBookControl: cannot call "
                + "bookScanned except in SCANNING state");
        }    
        book = library.book(bookId);
        if (book == null) {
            ui.displayOutput("Invalid bookId");
            return;
        }
        boolean bookAvailable = book.isAvailable();
        if (!bookAvailable) {
            ui.displayOutput("Book cannot be borrowed");
            return;
        }
        pending.add(book);
        for (Book book : pending) {
        	String bookInformation = book.toString();
            ui.displayOutput(bookInformation);
        }
        boolean limitReached = library.loansRemainingForMember(member) - pending.size() == 0;
        if (limitReached) {
            ui.displayOutput("Loan limit reached");
            completeBorrowing();
        }
    }
    
    
    public void completeBorrowing() {
    	boolean borrowingSize = pending.size() == 0;
        if (borrowingSize) {
            cancelBorrowing();
        } else {
            ui.displayOutput("\nFinal Borrowing List");
            for (Book book : pending) {
            	String bookInformation = book.toString();
                ui.displayOutput(bookInformation);
            }
            completed = new ArrayList<Loan>();
            ui.setState(BorrowBookUi.UiState.FINALISING);
            state = ControlState.FINALISING;
        }
    }


    public void commitLoans() {
    	boolean finaliseLoan = state.equals(ControlState.FINALISING);
        if (!finaliseLoan) {
            throw new RuntimeException("BorrowBookControl: cannot call "
                + "commitLoans except in FINALISING state");
        }    
        for (Book book : pending) {
            Loan loan = library.issueLoan(book, member);
            completed.add(loan);            
        }
        ui.displayOutput("Completed Loan Slip");
        for (Loan loan : completed) {
        	String loanInformation = loan.toString();
            ui.displayOutput(loanInformation);
        }
        ui.setState(BorrowBookUi.UiState.COMPLETED);
        state = ControlState.COMPLETED;
    }

    
    public void cancelBorrowing() {
        ui.setState(BorrowBookUi.UiState.CANCELLED);
        state = ControlState.CANCELLED;
    }
    
    
}

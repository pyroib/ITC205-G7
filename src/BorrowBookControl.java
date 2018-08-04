import java.util.ArrayList;
import java.util.List;

public class BorrowBookControl {
    
    private BorrowBookUi ui;
    private library library;
    private member member;
    private enum ControlState { INITIALISED, READY, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED };
    private ControlState state;
    private List<book> pending;
    private List<Loan> completed;
    private book book;
    
    
    public BorrowBookControl() {
        this.library = library.INSTANCE();
        state = ControlState.INITIALISED;
    }
    

    public void setUi(BorrowBookUi ui) {
        if (!state.equals(ControlState.INITIALISED)) {
          throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
        }
        this.ui = ui;
        ui.setState(BorrowBookUi.UiState.READY);
        state = ControlState.READY;        
    }

        
    public void swiped(int memberId) {
        if (!state.equals(ControlState.READY)) {
          throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
        }    
        member = library.getMember(memberId);
        if (member == null) {
          ui.display("Invalid memberId");
          return;
        }
        if (library.memberCanBorrow(member)) {
          pending = new ArrayList<>();
          ui.setState(BorrowBookUi.UiState.SCANNING);
          state = ControlState.SCANNING; 
        } else {
          ui.display("Member cannot borrow at this time");
          ui.setState(BorrowBookUi.UiState.RESTRICTED); 
        }
    }
    
    
    public void scanned(int bookId) {
        book = null;
        if (!state.equals(ControlState.SCANNING)) {
          throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
        }    
        book = library.Book(bookId);
        if (book == null) {
          ui.display("Invalid bookId");
          return;
        }
        if (!book.Available()) {
          ui.display("Book cannot be borrowed");
          return;
        }
        pending.add(book);
        for (book book : pending) {
          ui.display(book.toString());
        }
        if (library.loansRemainingForMember(member) - pending.size() == 0) {
          ui.display("Loan limit reached");
          complete();
        }
    }
    
    
    public void complete() {
        if (pending.size() == 0) {
          cancel();
        } else {
          ui.display("\nFinal Borrowing List");
          for (book book : pending) {
            ui.display(book.toString());
          }
          completed = new ArrayList<Loan>();
          ui.setState(BorrowBookUi.UiState.FINALISING);
          state = ControlState.FINALISING;
        }
    }


    public void commitLoans() {
        if (!state.equals(ControlState.FINALISING)) {
          throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
        }    
        for (book book : pending) {
          Loan loan = library.issueLoan(book, member);
          completed.add(loan);            
        }
        ui.display("Completed Loan Slip");
        for (Loan loan : completed) {
          ui.display(loan.toString());
        }
        ui.setState(BorrowBookUi.UiState.COMPLETED);
        state = ControlState.COMPLETED;
    }

    
    public void cancel() {
        ui.setState(BorrowBookUi.UiState.CANCELLED);
        state = ControlState.CANCELLED;
    }
    
    
}

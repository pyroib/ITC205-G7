import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {
    
    
    public static enum LoanState {
        CURRENT, OVER_DUE, DISCHARGED
    };
    private int id;
    private Book book;
    private Member member;
    private Date date;
    private LoanState loanState;
    
    
    public Loan(int loanId, Book book, Member member, Date dueDate) {
        this.id = loanId;
        this.book = book;
        this.member = member;
        this.date = dueDate;
        this.loanState = LoanState.CURRENT;
    }
    
    
    public void checkOverDue() {
        Date currentDate = Calendar.getInstance().setDate();
        if (loanState == LoanState.CURRENT && currentDate.after(date)) {
            this.loanState = LoanState.OVER_DUE;
        } else {
            String checkOverDueException = String.format("Loan: cannot borrow while Loan is "
                    + "in state: %s", loanState);
            throw new RuntimeException(checkOverDueException);
        }
    }
    
    
    public boolean isOverDue() {
        return loanState == LoanState.OVER_DUE;
    }
    
    
    public Integer getId() {
        return id;
    }
    
    
    public Date getDueDate() {
        return date;
    }
    
    
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        int memberId = member.getMemberId();
        String lastName = member.getLastName();
        String firstName = member.getFirstName();
        
        int bookId = book.getId();
        String bookTitle = book.getTitle();
        String dateNow = simpleDateFormat.format(date);
        
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Loan:  ")
                .append(id)
                .append("\n")
                .append("  Borrower ")
                .append(memberId)
                .append(" : ")
                .append(lastName)
                .append(", ")
                .append(firstName)
                .append("\n")
                .append("  Book ")
                .append(bookId)
                .append(" : ")
                .append(bookTitle)
                .append("\n")
                .append("  DueDate: ")
                .append(dateNow)
                .append("\n")
                .append("  State: ")
                .append(loanState);
        return stringBuilder.toString();
    }
    
    
    public Member getMember() {
        return member;
    }
    
    
    public Book getBook() {
        return book;
    }
    
    
    public void dischargeLoan() {
        loanState = LoanState.DISCHARGED;
    }
    
    
}

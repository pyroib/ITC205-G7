import java.io.Serializable;

@SuppressWarnings("serial")
public class Book implements Serializable {

    private String title;
    private String author;
    private String callNo;
    private int id;
    
    private enum BookState {AVAILABLE, ON_LOAN, DAMAGED, RESERVED};
    private BookState bookState;

    public Book(String author, String title, String callNo, int id) {
        this.author = author;
        this.title = title;
        this.callNo = callNo;
        this.id = id;
        this.bookState = BookState.AVAILABLE;
    }

    
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Book: ")
            .append(id)
            .append("\n")
            .append("  Title:  ")
            .append(title)
            .append("\n")
            .append("  Author: ")
            .append(author)
            .append("\n")
            .append("  CallNo: ")
            .append(callNo)
            .append("\n")
            .append("  State:  ")
            .append(bookState);
        return stringBuilder.toString();
    }

    
    public Integer getId() {
        return id;
    }

    
    public String getTitle() {
        return title;
    }

    
    public boolean isAvailable() {
        return bookState == BookState.AVAILABLE;
    }

    
    public boolean onLoan() {
        return bookState == BookState.ON_LOAN;
    }

    
    public boolean isDamaged() {
        return bookState == BookState.DAMAGED;
    }

    
    public void borrowBook() {
        if (bookState.equals(BookState.AVAILABLE)) {
          bookState = BookState.ON_LOAN;
        } else {
          throw new RuntimeException(String.format("Book: cannot borrow while book is in state: %s", bookState));
        }
    }

    
    public void returnBook(boolean DAMAGED) {
        if (bookState.equals(BookState.ON_LOAN)) {
          if (DAMAGED) {
            bookState = BookState.DAMAGED;
          } else {
            bookState = BookState.AVAILABLE;
          }
        } else {
          throw new RuntimeException(String.format("Book: cannot Return while book is in state: %s", bookState));
        }
    }

    
    public void repairBook() {
        if (bookState.equals(BookState.DAMAGED)) {
          bookState = BookState.AVAILABLE;
        } else {
          String exceptionString = String.format("Book: cannot repair while book is in state: %s", bookState);
          throw new RuntimeException(exceptionString);
        }
    }

    
}
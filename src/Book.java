import java.io.Serializable;

@SuppressWarnings("serial")
public class Book implements Serializable {

    private String title;
    private String author;
    private String callNo;
    private int id;
    
    private enum State {AVAILABLE, ON_LOAN, DAMAGED, RESERVED};
    private State state;

    public Book(String author, String title, String callNo, int id) {
        this.author = author;
        this.title = title;
        this.callNo = callNo;
        this.id = id;
        this.state = State.AVAILABLE;
    }

    
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Book: ").append(id).append("\n")
        .append("  Title:  ").append(title).append("\n")
        .append("  Author: ").append(author).append("\n")
        .append("  CallNo: ").append(callNo).append("\n")
        .append("  State:  ").append(state);
        return stringBuilder.toString();
    }

    
    public Integer getId() {
        return id;
    }

    
    public String getTitle() {
        return title;
    }

    
    public boolean available() {
        return state == State.AVAILABLE;
    }

    
    public boolean onLoan() {
        return state == State.ON_LOAN;
    }

    
    public boolean damaged() {
        return state == State.DAMAGED;
    }

    
    public void borrow() {
        if (state.equals(State.AVAILABLE)) {
          state = State.ON_LOAN;
        } else {
          throw new RuntimeException(String.format("Book: cannot borrow while book is in state: %s", state));
        }
    }

    
    public void returned(boolean DAMAGED) {
        if (state.equals(State.ON_LOAN)) {
          if (DAMAGED) {
            state = State.DAMAGED;
          } else {
            state = State.AVAILABLE;
          }
        } else {
          throw new RuntimeException(String.format("Book: cannot Return while book is in state: %s", state));
        }
    }

    
    public void repair() {
        if (state.equals(State.DAMAGED)) {
          state = State.AVAILABLE;
        } else {
          throw new RuntimeException(String.format("Book: cannot repair while book is in state: %s", state));
        }
    }

    
}
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Library implements Serializable {

  private static final String LIBRARY_FILE = "library.obj";
  private static final int LOAN_LIMIT = 2;
  private static final int LOAN_PERIOD = 2;
  private static final double FINE_PER_DAY = 1.0;
  private static final double MAX_FINES_OWED = 5.0;
  private static final double DAMAGE_FEE = 2.0;

  private static Library self;
  private int bookId;
  private int memberId;
  private int libraryId;
  private Date loadDate;

  private Map<Integer, book> catalog;
  private Map<Integer, Member> members;
  private Map<Integer, loan> loans;
  private Map<Integer, loan> currentLoans;
  private Map<Integer, book> damagedBooks;

  
  private Library() {
    catalog = new HashMap<>();
    members = new HashMap<>();
    loans = new HashMap<>();
    currentLoans = new HashMap<>();
    damagedBooks = new HashMap<>();
    bookId = 1;
    memberId = 1;
    libraryId = 1;
  }

  
  public static synchronized Library INSTANCE() {
    if (self == null) {
      Path path = Paths.get(LIBRARY_FILE);
      if (Files.exists(path)) {
        try (ObjectInputStream objectOutputStream = new ObjectInputStream(new FileInputStream(LIBRARY_FILE));) {
          self = (Library) objectOutputStream.readObject();
          Calendar.getInstance().setDate(self.loadDate);
          objectOutputStream.close();
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      } else {
        self = new Library();
      }
    }
    return self;
  }

  
  public static synchronized void save() {
    if (self != null) {
      self.loadDate = Calendar.getInstance().Date();
      try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(LIBRARY_FILE));) {
        objectOutputStream.writeObject(self);
        objectOutputStream.flush();
        objectOutputStream.close();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  
  public int bookId() {
    return bookId;
  }

  
  public int memberId() {
    return memberId;
  }

  
  private int nextBookId() {
    return bookId++;
  }

  
  private int nextMemberId() {
    return memberId++;
  }

  
  private int nextLibraryId() {
    return libraryId++;
  }

  
  public List<Member> members() {
    return new ArrayList<Member>(members.values());
  }

  
  public List<book> books() {
    return new ArrayList<book>(catalog.values());
  }

  
  public List<loan> currentLoans() {
    return new ArrayList<loan>(currentLoans.values());
  }

  
  public Member addMember(String lastName, String firstName, String email, int phoneNo) {
    Member newMember = new Member(lastName, firstName, email, phoneNo, nextMemberId());
    members.put(newMember.getId(), newMember);
    return newMember;
  }

  
  public book addBook(String a, String t, String c) {
    book newBook = new book(a, t, c, nextBookId());
    catalog.put(newBook.ID(), newBook);
    return newBook;
  }

  
  public Member getMember(int memberId) {
    if (members.containsKey(memberId)) {
      return members.get(memberId);
    }
    return null;
  }

  
  public book book(int bookId) {
    if (catalog.containsKey(bookId)) {
      return catalog.get(bookId);
    }
    return null;
  }

  
  public int loanLimit() {
    return LOAN_LIMIT;
  }

  
  public boolean memberCanBorrow(Member member) {
    if (member.getNumberOfCurrentLoans() == LOAN_LIMIT) {
      return false;
    }

    if (member.getFinesOwed() >= MAX_FINES_OWED) {
      return false;
    }

    for (loan loan : member.getLoans()) {
      if (loan.isOverDue()) {
        return false;
      }
    }

    return true;
  }

  
  public int loansRemainingForMember(Member member) {
    return LOAN_LIMIT - member.getNumberOfCurrentLoans();
  }

  
  public loan issueLoan(book book, Member member) {
    Date dueDate = Calendar.getInstance().getDueDate(LOAN_PERIOD);
    loan loan = new loan(nextLibraryId(), book, member, dueDate);
    member.takeOutLoan(loan);
    book.Borrow();
    loans.put(loan.getId(), loan);
    currentLoans.put(book.ID(), loan);
    return loan;
  }

  
  public loan getLoanByBookId(int bookId) {
    if (currentLoans.containsKey(bookId)) {
      return currentLoans.get(bookId);
    }
    return null;
  }

  
  public double calculateOverDueFine(loan loan) {
    if (loan.isOverDue()) {
      long daysOverDue = Calendar.getInstance().getDaysDifference(loan.getDueDate());
      double fine = daysOverDue * FINE_PER_DAY;
      return fine;
    }
    return 0.0;
  }

  
  public void dischargeLoan(loan currentLoan, boolean isDamaged) {
    Member member = currentLoan.Member();
    book book = currentLoan.Book();

    double overDueFine = calculateOverDueFine(currentLoan);
    member.addFine(overDueFine);
    member.dischargeLoan(currentLoan);
    book.Return(isDamaged);
    if (isDamaged) {
      member.addFine(DAMAGE_FEE);
      damagedBooks.put(book.ID(), book);
    }
    currentLoan.Loan();
    currentLoans.remove(book.ID());
  }

  
  public void checkCurrentLoans() {
    for (loan loan : currentLoans.values()) {
      loan.checkOverDue();
    }
  }

  
  public void repairBook(book currentBook) {
    if (damagedBooks.containsKey(currentBook.ID())) {
      currentBook.Repair();
      damagedBooks.remove(currentBook.ID());
    } else {
      throw new RuntimeException("Library: repairBook: book is not damaged");
    }
  }

  
}
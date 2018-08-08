import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("serial")
public class Member implements Serializable {

  private String lastName;
  private String firstName;
  private String email;
  private int phoneNumber;
  private int ID;
  private double fines;
  private Map<Integer, PayLoan> loans;

  
  public Member(String lastName, String firstName, String email, int phoneNumber, int ID) {
    this.lastName = lastName;
    this.firstName = firstName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.ID = ID;

    this.loans = new HashMap<>();
  }

  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Member:  ")
                .append(ID)
                .append("\n")
                .append("  Name:  ")
                .append(lastName)
                .append(", ")
                .append(firstName)
                .append("\n")
                .append("  Email: ")
                .append(email)
                .append("\n")
                .append("  Phone: ")
                .append(phoneNumber)
                .append("\n")
                .append(String.format("  Fines Owed :  $%.2f", fines))
                .append("\n");

    for (PayLoan loan : loans.values()) {
      stringBuilder.append(loan).append("\n");
    }
    return stringBuilder.toString();
  }

  
  public int getId() {
    return ID;
  }

  
  public List<PayLoan> getLoans() {
    List<PayLoan> loansList = new ArrayList<PayLoan>(loans.values()); 
    return loansList;
  }

  
  public int getNumberOfCurrentLoans() {
    return loans.size();
  }

  
  public double getFinesOwed() {
    return fines;
  }

  
  public void takeOutLoan(PayLoan loan) {
    int loanId = loan.getId();
    boolean loanExists = loans.containsKey(loanId);
    if (!loanExists) {
      loans.put(loanId, loan);
    } else {
      throw new RuntimeException("Duplicate loan added to member");
    }
  }

  
  public String getLastName() {
    return lastName;
  }

  
  public String getFirstName() {
    return firstName;
  }

  
  public void addFine(double fine) {
    fines += fine;
  }

  
  public double payFine(double amount) {
    if (amount < 0) {
      throw new RuntimeException("Member.payFine: amount must be positive");
    }
    double change = 0;
    if (amount > fines) {
      change = amount - fines;
      fines = 0;
    } else {
      fines -= amount;
    }
    return change;
  }

  
  public void dischargeLoan(PayLoan loan) {
    int loanId = loan.getId();
    boolean loanExists = loans.containsKey(loanId);
    if (loanExists) {
      loans.remove(loanId);
    } else {
      throw new RuntimeException("No such loan held by member");
    }
  }

  
}

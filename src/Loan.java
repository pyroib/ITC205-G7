import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {

	public static enum LoanState { CURRENT, OVER_DUE, DISCHARGED };

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
		if (loanState == LoanState.CURRENT && Calendar.getInstance().setDate().after(date)) {
			this.loanState = LoanState.OVER_DUE;
		} else {
		String checkOverDueException = String.format("Loan: cannot borrow while Loan is in state: %s", loanState);
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
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Loan:  ")
		.append(id)
		.append("\n")
		.append("  Borrower ")
		.append(member.getId())
		.append(" : ")
		.append(member.getLastName())
		.append(", ")
		.append(member.getFirstName())
		.append("\n")
		.append("  Book ")
		.append(book.getId())
		.append(" : " )
		.append(book.getTitle())
		.append("\n")
		.append("  DueDate: ")
		.append(simpleDateFormat.format(date))
		.append("\n")
		.append("  State: ")
		.append(loanState);		
		return stringBuilder.toString();
	}


	public Member Member() {
		return member;
	}


	public Book Book() {
		return book;
	}


	public void updateLoan() {
		loanState = LoanState.DISCHARGED;		
	}


}

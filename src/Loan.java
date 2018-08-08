import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable {
	
	public static enum LoanState { CURRENT, OVER_DUE, DISCHARGED };
	
	private int id;
	private book book;
	private Member member;
	private Date date;
	private LoanState state;

	
	public Loan(int loanId, book book, Member member, Date dueDate) {
		this.id = loanId;
		this.book = book;
		this.member = member;
		this.date = dueDate;
		this.state = LoanState.CURRENT;
	}

	
	public void checkOverDue() {
		if (state == LoanState.CURRENT &&
			Calendar.getInstance().Date().after(date)) {
			this.state = LoanState.OVER_DUE;			
		}
	}

	
	public boolean isOverDue() {
		return state == LoanState.OVER_DUE;
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
					.append(book.ID())
					.append(" : " )
					.append(book.Title())
					.append("\n")
					.append("  DueDate: ")
					.append(simpleDateFormat.format(date))
					.append("\n")
					.append("  State: ")
					.append(state);
		return stringBuilder.toString();
	}


	public Member Member() {
		return member;
	}


	public book Book() {
		return book;
	}


	public void Loan() {
		state = LoanState.DISCHARGED;		
	}

}

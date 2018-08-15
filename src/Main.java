import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    
    
    private static Scanner userInput;
    private static Library library;
    private static String menu;
    private static Calendar calender;
    private static SimpleDateFormat simpleDateFormat;
    
    
    public static void main(String[] args) {
        try {
            userInput = new Scanner(System.in);
            library = Library.INSTANCE();
            calender = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            for (Member member : library.members()) {
                printOutput(member);
            }
            
            printOutput(" ");
            for (Book book : library.books()) {
                printOutput(book);
            }
            
            menu = getMenu();
            
            boolean userSelection = false;
            
            while (!userSelection) {
                printOutput("\n" + simpleDateFormat.format(calender.setDate()));
                String userEntry = userInput(menu);
                
                switch (userEntry.toUpperCase()) {
                    case "M":
                        addMember();
                        break;
                    
                    case "LM":
                        listMembers();
                        break;
                    
                    case "B":
                        addBook();
                        break;
                    
                    case "LB":
                        listBooks();
                        break;
                    
                    case "FB":
                        fixBooks();
                        break;
                    
                    case "L":
                        borrowBook();
                        break;
                    
                    case "R":
                        returnBook();
                        break;
                    
                    case "LL":
                        listCurrentLoans();
                        break;
                    
                    case "P":
                        payFine();
                        break;
                    
                    case "T":
                        incrementDate();
                        break;
                    
                    case "Q":
                        userSelection = true;
                        break;
                    
                    default:
                        printOutput("\nInvalid option\n");
                        break;
                }
                
                Library.save();
            }
        } catch (RuntimeException runtimeException) {
            printOutput(runtimeException);
        }
        printOutput("\nEnded\n");
    }
    
    
    private static String getMenu() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\nLibrary Main Menu\n\n")
            .append("  M  : Add member\n")
            .append("  LM : List members\n")
            .append("\n")
            .append("  B  : Add book\n")
            .append("  LB : List books\n")
            .append("  FB : Fix books\n")
            .append("\n")
            .append("  L  : Take out a loan\n")
            .append("  R  : Return a loan\n")
            .append("  LL : List loans\n")
            .append("\n")
            .append("  P  : Pay fine\n")
            .append("\n")
            .append("  T  : Increment date\n")
            .append("  Q  : Quit\n")
            .append("\n")
            .append("Please enter you choice : ");
            
        return stringBuilder.toString();
    }
    
    
    private static void payFine() {
        PayFineControl payFineControl = new PayFineControl();
        PayFineUi payFineUi = new PayFineUi(payFineControl);
        payFineUi.run();
    }
    
    
    private static void listCurrentLoans() {
        printOutput("");
        for (Loan loan : library.currentLoans()) {
            printOutput(loan + "\n");
        }
    }
    
    
    private static void listBooks() {
        printOutput("");
        for (Book book : library.books()) {
            printOutput(book + "\n");
        }
    }
    
    
    private static void listMembers() {
        printOutput("");
        for (Member member : library.members()) {
            printOutput(member + "\n");
        }
    }
    
    
    private static void borrowBook() {
        BorrowBookControl borrowBookControl = new BorrowBookControl();
        BorrowBookUi borrowBookUi = new BorrowBookUi(borrowBookControl);
        borrowBookUi.run();
    }
    
    
    private static void returnBook() {
        ReturnBookControl returnBookControl = new ReturnBookControl();
        ReturnBookUi returnBookUi = new ReturnBookUi(returnBookControl);
        returnBookUi.run();
    }
    
    
    private static void fixBooks() {
        FixBookControl fixBookControl = new FixBookControl();
        FixBookUi fixBookUi = new FixBookUi(fixBookControl);
        fixBookUi.run();
    }
    
    
    private static void incrementDate() {
        try {
            int days = Integer.valueOf(userInput("Enter number of days: ")).intValue();
            calender.incrementDate(days);
            library.checkCurrentLoans();
            printOutput(simpleDateFormat.format(calender.setDate()));
            
        } catch (NumberFormatException e) {
            printOutput("\nInvalid number of days\n");
        }
    }
    
    
    private static void addBook() {
        String author = userInput("Enter author: ");
        String title = userInput("Enter title: ");
        String callNo = userInput("Enter call number: ");
        Book book = library.addBook(author, title, callNo);
        printOutput("\n" + book + "\n");
    }
    
    
    private static void addMember() {
        try {
            String lastName = userInput("Enter last name: ");
            String firstName = userInput("Enter first name: ");
            String email = userInput("Enter email: ");
            String memberInput = userInput("Enter phone number: ");
            int phoneNo = Integer.valueOf(memberInput).intValue();
            Member member = library.addMember(lastName, firstName, email, phoneNo);
            printOutput("\n" + member + "\n");
            
        } catch (NumberFormatException e) {
            printOutput("\nInvalid phone number\n");
        }
        
    }
    
    
    private static String userInput(String prompt) {
        System.out.print(prompt);
        return userInput.nextLine();
    }
    
    
    private static void printOutput(Object object) {
        System.out.println(object);
    }
    
}

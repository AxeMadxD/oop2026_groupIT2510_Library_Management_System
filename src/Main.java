import controllers.LibraryController;
import db.DbInitializer;
import db.Database;
import db.PostgresDatabase;
import repositories.BookRepository;
import repositories.LoanRepository;
import repositories.MemberRepository;
import repositories.jdbc.JdbcBookRepository;
import repositories.jdbc.JdbcLoanRepository;
import repositories.jdbc.JdbcMemberRepository;
import services.FineCalculator;
import services.LoanService;
import services.SimpleFineCalculator;
import ui.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        Database db = new PostgresDatabase();
        try {
            DbInitializer.init(db);
        } catch (RuntimeException e) {
            System.out.println("Fatal: cannot initialize database. " + e.getMessage());
            return;
        }

        BookRepository bookRepository = new JdbcBookRepository(db);
        MemberRepository memberRepository = new JdbcMemberRepository(db);
        LoanRepository loanRepository = new JdbcLoanRepository(db);

        FineCalculator fineCalculator = new SimpleFineCalculator();
        LoanService loanService = new LoanService(bookRepository, memberRepository, loanRepository, fineCalculator);

        LibraryController controller = new LibraryController(
                bookRepository,
                memberRepository,
                loanRepository,
                loanService
        );

        ConsoleApp app = new ConsoleApp(controller);
        app.run();
    }
}

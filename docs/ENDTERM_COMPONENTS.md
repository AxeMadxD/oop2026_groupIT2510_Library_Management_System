# Endterm Components (Component Principles)

## Component Mapping

**Catalog Component** (`components.catalog`)
- `components.catalog.CatalogComponent`
- `repositories.BookRepository`
- `repositories.jdbc.JdbcBookRepository`
- `domain.book.*`

**Member Management Component** (`components.member`)
- `components.member.MemberManagementComponent`
- `repositories.MemberRepository`
- `repositories.jdbc.JdbcMemberRepository`
- `domain.member.Member`

**Loan Management Component** (`components.loan`)
- `components.loan.LoanManagementComponent`
- `services.LoanService`
- `repositories.LoanRepository`
- `repositories.jdbc.JdbcLoanRepository`
- `domain.loan.Loan`

**Reporting Component** (`components.reporting`)
- `components.reporting.ReportingComponent`
- `domain.report.LoanReport`
- `services.singleton.FinePolicy`
- `repositories.BookRepository`, `repositories.MemberRepository`, `repositories.LoanRepository`
- `domain.book.Book`, `domain.member.Member`, `domain.loan.Loan`

## Dependencies (arrows)

- `CatalogComponent -> BookRepository`
- `MemberManagementComponent -> MemberRepository`
- `LoanManagementComponent -> LoanService -> BookRepository, MemberRepository, LoanRepository`
- `ReportingComponent -> BookRepository, MemberRepository, LoanRepository, FinePolicy`

## Runtime Wiring

- `Main` creates repositories and services.
- `Main` constructs components.
- `LibraryController` and `ConsoleApp` use only components (no direct repository/service access).
- Data access flows through components to repositories/JDBC/DB.

Flow:
`Main -> Controller/UI -> Components -> Services/Repos -> JDBC/DB`

## Component Principles

**REP (Reuse/Release Equivalence Principle)**
- Each component groups classes that are reused together and can be released together. For example, the Catalog component contains only book listing and lookup concerns. Reporting groups reporting-specific logic and data structures (LoanReport + FinePolicy).

**CCP (Common Closure Principle)**
- Classes that change together are packaged together. If report formatting or fine policy changes, only the Reporting component changes. If borrowing rules change, only Loan Management changes.

**CRP (Common Reuse Principle)**
- Classes inside a component are reused together, reducing unnecessary dependencies. A consumer that needs only catalog browsing depends only on Catalog and does not bring in loan or reporting code.

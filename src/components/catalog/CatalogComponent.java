package components.catalog;

import domain.book.Book;
import domain.book.BookFactory;
import repositories.BookRepository;

import java.util.List;
import java.util.Optional;

public class CatalogComponent {
    private final BookRepository bookRepository;

    public CatalogComponent(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book addBook(String type, String title, String author) {
        Book book = BookFactory.createBook(type, title, author);
        return bookRepository.save(book);
    }

    public List<Book> listAvailableBooks() {
        return bookRepository.findAvailable();
    }

    public List<Book> listAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> findBookById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return bookRepository.findById(Math.toIntExact(id));
    }
}

package repositories;

import domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    Optional<Book> findById(int id);

    List<Book> findAll();

    List<Book> findAvailable();

    void updateAvailability(int bookId, boolean available);
}

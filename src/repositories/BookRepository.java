package repositories;

import domain.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {
    List<Book> findAvailable();

    void updateAvailability(int bookId, boolean available);
}


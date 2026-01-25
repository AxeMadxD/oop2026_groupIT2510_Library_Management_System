package services;

import java.time.LocalDate;

public interface FineCalculator {
    int calculateFine(LocalDate dueDate, LocalDate returnDate);
}

package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SimpleFineCalculator implements FineCalculator {
    private static final int FINE_PER_DAY = 100;

    @Override
    public int calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate == null || !returnDate.isAfter(dueDate)) {
            return 0;
        }
        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        return (int) daysLate * FINE_PER_DAY;
    }
}

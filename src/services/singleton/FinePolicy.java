package services.singleton;

import java.time.temporal.ChronoUnit;
import java.time.LocalDate;

public class FinePolicy {
    private static FinePolicy instance;

    private final int dailyFine;
    private final int maxFine;

    private FinePolicy() {
        this.dailyFine = 5;
        this.maxFine = 100;
    }

    public static FinePolicy getInstance() {
        if (instance == null) {
            instance = new FinePolicy();
        }
        return instance;
    }

    public int calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isBefore(dueDate) || returnDate.isEqual(dueDate)) {
            return 0;
        }
        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
        int fine = (int) (daysLate * dailyFine);
        return Math.min(fine, maxFine);
    }

    public int getDailyFine() { return dailyFine; }
    public int getMaxFine() { return maxFine; }
}


package services;

import services.singleton.FinePolicy;

import java.time.LocalDate;

public class SimpleFineCalculator implements FineCalculator {

    private final FinePolicy finePolicy;

    public SimpleFineCalculator() {
        this.finePolicy = FinePolicy.getInstance();
    }

    @Override
    public int calculateFine(LocalDate dueDate, LocalDate returnDate) {
        return finePolicy.calculateFine(dueDate, returnDate);
    }
}

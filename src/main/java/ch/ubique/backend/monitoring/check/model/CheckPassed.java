package ch.ubique.backend.monitoring.check.model;

import org.jspecify.annotations.NonNull;

public record CheckPassed() implements CheckStatus {

    @Override
    public @NonNull String message() {
        return "check passed";
    }

    @Override
    public @NonNull String toString() {
        return message();
    }
}

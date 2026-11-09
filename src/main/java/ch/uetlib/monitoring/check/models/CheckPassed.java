package ch.uetlib.monitoring.check.models;

public record CheckPassed() implements CheckStatus {

    @Override
    public String message() {
        return "check passed";
    }

    @Override
    public String toString() {
        return message();
    }
}

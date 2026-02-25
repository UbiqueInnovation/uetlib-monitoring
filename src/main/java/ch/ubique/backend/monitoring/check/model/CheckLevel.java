package ch.ubique.backend.monitoring.check.model;

import jakarta.validation.constraints.NotNull;

public enum CheckLevel {
    ERROR,
    WARNING,
    INFO;

    public boolean isSameOrMoreSevereThan(@NotNull final CheckLevel checkLevel) {
        return compareTo(checkLevel) <= 0;
    }
}

package ch.ubique.backend.monitoring.check.model;

import jakarta.validation.constraints.NotNull;

public record CheckResult(
        @NotNull Class<?> check, @NotNull CheckLevel level, @NotNull CheckStatus result) {}

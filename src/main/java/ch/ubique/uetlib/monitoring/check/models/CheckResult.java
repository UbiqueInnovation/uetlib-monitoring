package ch.ubique.uetlib.monitoring.check.models;

import jakarta.validation.constraints.NotNull;

public record CheckResult(
        @NotNull Class<?> check, @NotNull CheckLevel level, @NotNull CheckStatus result) {}

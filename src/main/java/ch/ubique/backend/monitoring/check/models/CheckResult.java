package ch.ubique.backend.monitoring.check.models;

public record CheckResult(Class<?> check, CheckStatus result) {}

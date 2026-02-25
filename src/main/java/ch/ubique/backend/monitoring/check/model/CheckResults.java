package ch.ubique.backend.monitoring.check.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;

public record CheckResults(Collection<CheckResult> checks) {

    @JsonProperty
    public boolean allPassed() {
        return checks.stream().map(CheckResult::result).allMatch(CheckStatus::passed);
    }
}

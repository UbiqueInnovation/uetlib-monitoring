package ch.ubique.uetlib.monitoring.check.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;

public record CheckResults(Collection<CheckResult> checks) {

    @JsonProperty
    public boolean allPassed() {
        return checks.stream().map(CheckResult::result).allMatch(CheckStatus::passed);
    }
}

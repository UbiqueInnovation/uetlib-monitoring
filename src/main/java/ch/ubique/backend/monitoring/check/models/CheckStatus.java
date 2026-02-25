package ch.ubique.backend.monitoring.check.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface CheckStatus permits CheckPassed, CheckFailed {

    CheckStatus CHECK_PASSED = new CheckPassed();

    @JsonProperty
    String message();

    @JsonProperty
    default boolean passed() {
        return this instanceof CheckPassed;
    }
}

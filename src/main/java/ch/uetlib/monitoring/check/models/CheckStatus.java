package ch.uetlib.monitoring.check.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface CheckStatus permits CheckPassed, CheckFailed {

    CheckStatus CHECK_PASSED = new CheckPassed();

    @JsonProperty
    String message();

    // Not a default method: initialising CheckPassed must not initialise this interface, whose
    // CHECK_PASSED constant creates a CheckPassed (class initialisation deadlock).
    @JsonProperty
    boolean passed();
}

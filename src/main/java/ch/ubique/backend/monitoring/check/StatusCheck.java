package ch.ubique.backend.monitoring.check;

import ch.ubique.backend.monitoring.check.models.CheckLevel;
import ch.ubique.backend.monitoring.check.models.CheckResult;
import ch.ubique.backend.monitoring.check.models.CheckStatus;

import org.jspecify.annotations.NonNull;

public interface StatusCheck {

    @NonNull CheckStatus check();

    @NonNull
    default CheckLevel level() {
        return CheckLevel.ERROR;
    }

    @NonNull
    default CheckResult result() {
        return new CheckResult(getClass(), level(), check());
    }

    @NonNull
    default String checkName() {
        return getClass().getSimpleName();
    }
}

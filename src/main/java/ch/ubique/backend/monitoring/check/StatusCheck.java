package ch.ubique.backend.monitoring.check;

import ch.ubique.backend.monitoring.check.model.CheckLevel;
import ch.ubique.backend.monitoring.check.model.CheckResult;
import ch.ubique.backend.monitoring.check.model.CheckStatus;

public interface StatusCheck {

    CheckStatus check();

    default CheckLevel level() {
        return CheckLevel.ERROR;
    }

    default CheckResult result() {
        return new CheckResult(getClass(), level(), check());
    }
}

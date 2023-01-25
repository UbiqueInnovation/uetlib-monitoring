package ch.ubique.backend.monitoring.check;

import ch.ubique.backend.monitoring.check.models.CheckResult;
import ch.ubique.backend.monitoring.check.models.CheckStatus;

public interface StatusCheck {

    CheckStatus check();

    default CheckResult result() {
        return new CheckResult(getClass(), check());
    }
}

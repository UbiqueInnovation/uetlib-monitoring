package ch.ubique.uetlib.monitoring.check;

import ch.ubique.uetlib.monitoring.check.models.CheckLevel;
import ch.ubique.uetlib.monitoring.check.models.CheckResult;
import ch.ubique.uetlib.monitoring.check.models.CheckStatus;

public interface StatusCheck {

    CheckStatus check();

    default CheckLevel level() {
        return CheckLevel.ERROR;
    }

    default CheckResult result() {
        return new CheckResult(getClass(), level(), check());
    }

    default String checkName() {
        return getClass().getSimpleName();
    }
}

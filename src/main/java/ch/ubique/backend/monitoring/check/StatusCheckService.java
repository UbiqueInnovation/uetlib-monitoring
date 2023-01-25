package ch.ubique.backend.monitoring.check;

import ch.ubique.backend.monitoring.check.models.CheckResults;
import java.util.Collection;
import org.springframework.stereotype.Service;

@Service
public final class StatusCheckService {

    private final Collection<StatusCheck> checks;

    public StatusCheckService(final Collection<StatusCheck> checks) {
        this.checks = checks;
    }

    public CheckResults checkAll() {
        return new CheckResults(checks.stream().map(StatusCheck::result).toList());
    }
}

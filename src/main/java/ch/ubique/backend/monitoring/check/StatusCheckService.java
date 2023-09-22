package ch.ubique.backend.monitoring.check;

import ch.ubique.backend.monitoring.check.models.CheckLevel;
import ch.ubique.backend.monitoring.check.models.CheckResults;

import jakarta.validation.constraints.NotNull;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public final class StatusCheckService {

    private final Collection<StatusCheck> checks;

    public StatusCheckService(final Collection<StatusCheck> checks) {
        this.checks = checks;
    }

    public CheckResults checkAll(@NotNull final CheckLevel checkLevel) {
        return new CheckResults(
                checks.stream()
                        .map(StatusCheck::result)
                        .filter(result -> result.level().isSameOrMoreSevereThan(checkLevel))
                        .toList());
    }
}

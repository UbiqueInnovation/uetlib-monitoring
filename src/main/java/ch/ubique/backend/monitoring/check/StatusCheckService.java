package ch.ubique.backend.monitoring.check;

import ch.ubique.backend.core.executor.VirtualThreadUtils;
import ch.ubique.backend.monitoring.check.models.CheckLevel;
import ch.ubique.backend.monitoring.check.models.CheckResults;

import jakarta.validation.constraints.NotNull;

import java.util.Comparator;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public final class StatusCheckService {

    private final Collection<StatusCheck> checks;

    public StatusCheckService(final Collection<StatusCheck> checks) {
        this.checks = checks;
    }

    public CheckResults checkAll(final @NotNull CheckLevel checkLevel) {
        return new CheckResults(
                VirtualThreadUtils.mapAll(StatusCheck::result, checks).stream()
                        .filter(result -> result.level().isSameOrMoreSevereThan(checkLevel))
                        .sorted(Comparator.comparing(result -> result.check().getCanonicalName()))
                        .toList());
    }
}

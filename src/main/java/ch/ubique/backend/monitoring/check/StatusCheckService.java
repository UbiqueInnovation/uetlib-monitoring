package ch.ubique.backend.monitoring.check;

import static ch.ubique.backend.core.utils.StringUtils.requireNonBlankElse;

import ch.ubique.backend.core.executor.VirtualThreadUtils;
import ch.ubique.backend.monitoring.check.models.CheckFailed;
import ch.ubique.backend.monitoring.check.models.CheckLevel;
import ch.ubique.backend.monitoring.check.models.CheckResult;
import ch.ubique.backend.monitoring.check.models.CheckResults;

import jakarta.validation.constraints.NotNull;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

@Service
public final class StatusCheckService {

    private final Logger logger = LoggerFactory.getLogger(StatusCheckService.class);

    private final Collection<StatusCheck> checks;

    public StatusCheckService(final Collection<StatusCheck> checks) {
        this.checks = checks;
    }

    public CheckResults checkAll(
            final @NotNull CheckLevel checkLevel, final @Nullable String checkClassName) {
        final var filteredChecks =
                checkClassName == null
                        ? checks.stream()
                        : checks.stream().filter(check -> check.checkName().equals(checkClassName));
        final var results =
                VirtualThreadUtils.mapAll(this::runCheck, filteredChecks)
                        .filter(result -> result.level().isSameOrMoreSevereThan(checkLevel))
                        .sorted(Comparator.comparing(result -> result.check().getCanonicalName()))
                        .toList();
        return new CheckResults(results);
    }

    private CheckResult runCheck(final @NotNull StatusCheck check) {
        final var checkClass = check.getClass();
        try {
            return check.result();
        } catch (final Exception e) {
            logger.warn("{} threw an uncaught exception.", checkClass.getCanonicalName(), e);
            final var reason = requireNonBlankElse(e.getMessage(), e.getClass().getSimpleName());
            final var context = Map.of("check", checkClass, "exception", e.getClass());
            return new CheckResult(
                    StatusCheckService.class, CheckLevel.ERROR, new CheckFailed(reason, context));
        }
    }
}

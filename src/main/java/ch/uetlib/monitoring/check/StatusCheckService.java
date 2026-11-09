package ch.uetlib.monitoring.check;

import static ch.uetlib.core.utils.StringUtils.requireNonBlankElse;

import ch.uetlib.core.executor.VirtualThreadUtils;
import ch.uetlib.monitoring.check.models.CheckFailed;
import ch.uetlib.monitoring.check.models.CheckLevel;
import ch.uetlib.monitoring.check.models.CheckResult;
import ch.uetlib.monitoring.check.models.CheckResults;

import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public final class StatusCheckService {

    private final Logger logger = LoggerFactory.getLogger(StatusCheckService.class);

    private final Collection<StatusCheck> checks;

    public StatusCheckService(final Collection<StatusCheck> checks) {
        this.checks = checks;
    }

    public CheckResults runChecks(final CheckLevel checkLevel) {
        return runChecks(checkLevel, null);
    }

    public CheckResults runChecks(
            final CheckLevel checkLevel, final @Nullable List<String> checkClassNames) {
        final var filteredChecks =
                checkClassNames == null
                        ? checks.stream()
                        : checks.stream()
                                .filter(check -> checkClassNames.contains(check.checkName()));

        final var results =
                VirtualThreadUtils.mapAll(this::runCheck, filteredChecks)
                        .filter(result -> result.level().isSameOrMoreSevereThan(checkLevel))
                        .sorted(Comparator.comparing(result -> result.check().getCanonicalName()))
                        .toList();
        return new CheckResults(results);
    }

    private CheckResult runCheck(final StatusCheck check) {
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

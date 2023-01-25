package ch.ubique.backend.monitoring.check.models;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public record CheckFailed(@NotBlank String reason, @Nullable Object context)
        implements CheckStatus {

    public CheckFailed(@NotBlank final String reason) {
        this(reason, null);
    }

    @Override
    public String message() {
        return "check failed";
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("reason", reason)
                .append("context", context)
                .toString();
    }
}

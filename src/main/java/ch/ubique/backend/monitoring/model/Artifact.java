package ch.ubique.backend.monitoring.model;

import static ch.ubique.backend.monitoring.utils.VersionUtil.parseVersion;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.zafarkhaja.semver.ParseException;
import com.github.zafarkhaja.semver.Version;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Comparator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Artifact(
        @JsonProperty(required = true) @NotBlank String groupId,
        @JsonProperty(required = true) @NotBlank String artifactId,
        @JsonProperty(required = true) @NotNull @Valid Version version)
        implements Comparable<Artifact> {

    private static final Comparator<Artifact> ARTIFACT_COMPARATOR =
            Comparator.comparing(Artifact::groupId)
                    .thenComparing(Artifact::artifactId)
                    .thenComparing(Artifact::version);

    @NotNull
    public static Artifact from(final de.westemeyer.version.core.model.Artifact artifact) {
        try {
            return new Artifact(
                    artifact.groupId(), artifact.artifactId(), parseVersion(artifact.version()));
        } catch (final IllegalArgumentException e) {
            if (e.getCause() instanceof final ParseException parseException) {
                throw new IllegalArgumentException(
                        "Failed to parse version "
                                + artifact.version()
                                + " for artifact "
                                + artifact,
                        parseException);
            }
            throw e;
        }
    }

    @Override
    public int compareTo(final Artifact o) {
        return ARTIFACT_COMPARATOR.compare(this, o);
    }
}

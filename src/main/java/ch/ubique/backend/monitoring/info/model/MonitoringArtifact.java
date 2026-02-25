package ch.ubique.backend.monitoring.info.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.zafarkhaja.semver.Version;

import de.westemeyer.version.core.model.Artifact;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.jspecify.annotations.NonNull;

import java.util.Comparator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MonitoringArtifact(
        @NotBlank String groupId, @NotBlank String artifactId, @NotNull @Valid Version version)
        implements Comparable<MonitoringArtifact> {

    private static final Comparator<MonitoringArtifact> ARTIFACT_COMPARATOR =
            Comparator.comparing(MonitoringArtifact::groupId)
                    .thenComparing(MonitoringArtifact::artifactId)
                    .thenComparing(MonitoringArtifact::version);

    @NotNull
    public static MonitoringArtifact from(final Artifact artifact) {
        return new MonitoringArtifact(
                artifact.groupId(), artifact.artifactId(), Version.parse(artifact.version()));
    }

    @Override
    public int compareTo(final @NonNull MonitoringArtifact o) {
        return ARTIFACT_COMPARATOR.compare(this, o);
    }
}

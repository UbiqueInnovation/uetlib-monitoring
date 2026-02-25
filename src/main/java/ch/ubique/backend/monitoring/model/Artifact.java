package ch.ubique.backend.monitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.zafarkhaja.semver.Version;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.jspecify.annotations.NonNull;

import java.util.Comparator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Artifact(
        @NotBlank String groupId, @NotBlank String artifactId, @NotNull @Valid Version version)
        implements Comparable<Artifact> {

    private static final Comparator<Artifact> ARTIFACT_COMPARATOR =
            Comparator.comparing(Artifact::groupId)
                    .thenComparing(Artifact::artifactId)
                    .thenComparing(Artifact::version);

    @NotNull
    public static Artifact from(final de.westemeyer.version.core.model.Artifact artifact) {
        return new Artifact(
                artifact.groupId(), artifact.artifactId(), Version.parse(artifact.version()));
    }

    @Override
    public int compareTo(final @NonNull Artifact o) {
        return ARTIFACT_COMPARATOR.compare(this, o);
    }
}

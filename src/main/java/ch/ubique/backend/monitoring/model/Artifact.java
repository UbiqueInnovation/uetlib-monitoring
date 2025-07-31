package ch.ubique.backend.monitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.zafarkhaja.semver.Version;

import jakarta.validation.constraints.NotBlank;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Artifact(
        @NotBlank String groupId,
        @NotBlank String artifactId,
        @NotBlank String artifactName,
        @JsonSerialize(using = ToStringSerializer.class) @NotBlank Version version)
        implements Comparable<Artifact> {

    private static final Comparator<Artifact> ARTIFACT_COMPARATOR =
            Comparator.comparing(Artifact::groupId)
                    .thenComparing(Artifact::artifactId)
                    .thenComparing(Artifact::version);
    ;

    public static Artifact from(
            final de.westemeyer.version.model.Artifact a, final String artifactName) {
        return new Artifact(
                a.getGroupId(), a.getArtifactId(), artifactName, Version.parse(a.getVersion()));
    }

    @Override
    public int compareTo(@NotNull final Artifact o) {
        return ARTIFACT_COMPARATOR.compare(this, o);
    }
}

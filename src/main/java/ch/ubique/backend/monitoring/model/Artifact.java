package ch.ubique.backend.monitoring.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.zafarkhaja.semver.Version;

import jakarta.validation.constraints.NotBlank;

public record Artifact(
        @NotBlank String groupId,
        @NotBlank String artifactId,
        @NotBlank String artifactName,
        @JsonSerialize(using = ToStringSerializer.class) @NotBlank Version version) {

    public static Artifact from(
            final de.westemeyer.version.model.Artifact a, final String artifactName) {
        return new Artifact(
                a.getGroupId(), a.getArtifactId(), artifactName, Version.parse(a.getVersion()));
    }
}

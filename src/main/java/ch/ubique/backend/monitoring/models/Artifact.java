package ch.ubique.backend.monitoring.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.zafarkhaja.semver.Version;

import jakarta.validation.constraints.NotBlank;

public record Artifact(
        @NotBlank String groupId,
        @NotBlank String artifactId,
        @JsonSerialize(using = ToStringSerializer.class) @NotBlank Version version) {}

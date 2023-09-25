package ch.ubique.backend.monitoring.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.zafarkhaja.semver.Version;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

public record InfoResponse(
        @JsonSerialize(using = ToStringSerializer.class) @NotBlank Version projectVersion,
        @NotNull Collection<Artifact> artifacts) {}

package ch.ubique.backend.monitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.zafarkhaja.semver.Version;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InfoResponse(
        @JsonSerialize(using = ToStringSerializer.class) @NotNull @Valid Version projectVersion,
        @JsonSerialize(using = ToStringSerializer.class) @NotNull @Valid Version javaVersion,
        @JsonSerialize(using = ToStringSerializer.class) @NotNull @Valid Version kotlinVersion,
        @NotNull @Valid Artifact whoami,
        @NotNull @Valid SystemInfo systemInfo,
        @NotNull @Valid Collection<Artifact> artifacts) {}

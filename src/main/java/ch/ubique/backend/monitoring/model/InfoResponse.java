package ch.ubique.backend.monitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.zafarkhaja.semver.Version;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.util.Collection;

/**
 * Response for the info endpoint.
 *
 * @param projectVersion
 * @param javaVersion Usually, only the major version. Expect a relaxed semver format, where both
 *     minor and patch might be missing.
 * @param kotlinVersion
 * @param whoami
 * @param systemInfo
 * @param artifacts
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InfoResponse(
        @JsonSerialize(using = ToStringSerializer.class) @NotNull @Valid Version projectVersion,
        @NotNull @Valid String javaVersion,
        @JsonSerialize(using = ToStringSerializer.class) @NotNull @Valid Version kotlinVersion,
        @NotNull @Valid Artifact whoami,
        @NotNull @Valid SystemInfo systemInfo,
        @NotNull @Valid Collection<Artifact> artifacts) {}

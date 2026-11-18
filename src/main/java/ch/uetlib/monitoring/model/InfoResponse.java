package ch.uetlib.monitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.zafarkhaja.semver.Version;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

/**
 * Response for the info endpoint.
 *
 * @param projectVersion
 * @param javaVersion Full runtime version, including the build number.
 * @param javaVendor Runtime vendor.
 * @param kotlinVersion
 * @param springBootVersion
 * @param springFrameworkVersion
 * @param whoami
 * @param systemInfo
 * @param artifacts
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InfoResponse(
        @NotNull @Valid Version projectVersion,
        @NotNull @Valid String javaVersion,
        @NotNull @Valid String javaVendor,
        @NotNull @Valid Version kotlinVersion,
        @NotNull @Valid Version springBootVersion,
        @NotNull @Valid Version springFrameworkVersion,
        @NotNull @Valid Artifact whoami,
        @NotNull @Valid SystemInfo systemInfo,
        @NotNull Collection<@Valid Artifact> artifacts) {}

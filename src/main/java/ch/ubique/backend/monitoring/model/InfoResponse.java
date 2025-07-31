package ch.ubique.backend.monitoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.zafarkhaja.semver.Version;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.springframework.boot.system.JavaVersion;

import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record InfoResponse(
        @JsonSerialize(using = ToStringSerializer.class) @NotBlank Version projectVersion,
        @JsonSerialize(using = ToStringSerializer.class) @NotBlank JavaVersion javaVersion,
        @NotNull Collection<Artifact> artifacts) {}

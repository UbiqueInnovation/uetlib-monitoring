package ch.ubique.backend.monitoring.model;

import ch.ubique.backend.core.validator.NullOrNotBlank;

import jakarta.validation.constraints.NotBlank;

public record ArtifactToFind(
        @NotBlank String groupId,
        @NotBlank String artifactId,
        @NullOrNotBlank String artifactName,
        boolean matchArtifactIdExact) {}

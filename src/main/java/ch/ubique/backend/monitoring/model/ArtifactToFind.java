package ch.ubique.backend.monitoring.model;

import jakarta.validation.constraints.NotBlank;

public record ArtifactToFind(
        @NotBlank String groupId, @NotBlank String artifactId, @NotBlank String artifactName) {}

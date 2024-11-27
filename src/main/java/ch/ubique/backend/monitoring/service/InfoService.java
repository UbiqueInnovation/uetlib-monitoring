package ch.ubique.backend.monitoring.service;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import ch.ubique.backend.monitoring.model.Artifact;
import ch.ubique.backend.monitoring.model.InfoResponse;

import com.github.zafarkhaja.semver.Version;

import de.westemeyer.version.service.ArtifactVersionCollector;

import jakarta.annotation.PostConstruct;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.system.JavaVersion;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;

/**
 * All projects that you want to see the version of <b>must</b> add the {@code
 * artifact-version-maven-plugin} to that artifact's pom or parent pom. Check the usage in the pom:
 * {@see pom.xml}
 */
@Service
public class InfoService {

    private static final Map<String, String> ARTIFACT_NAMES =
            Map.ofEntries(
                    Map.entry("ch.ubique.pushservice:ub-pushconnector", "UB Pushconnector"),
                    Map.entry(
                            "ch.admin.swisstopo.shared:swisstopo-backend-shared-service",
                            "swisstopo Backend Shared"),
                    Map.entry("ch.sac.shared:sac-backend-shared-service", "SAC Backend Shared"));

    private final BuildProperties buildProperties;

    private List<Artifact> artifacts = List.of();

    public InfoService(final BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @PostConstruct
    void resolveArtifacts() {
        final var retrievedArtifacts =
                ArtifactVersionCollector.collectArtifacts().stream()
                        .map(InfoService::mapArtifact)
                        .sorted(
                                Comparator.comparing(Artifact::groupId)
                                        .thenComparing(Artifact::artifactId));
        artifacts = Stream.concat(retrievedArtifacts, ADDITIONAL_ARTIFACTS).toList();
    }

    private static @NotNull Artifact mapArtifact(
            final de.westemeyer.version.model.Artifact artifact) {
        final var artifactName =
                Optional.ofNullable(
                                ARTIFACT_NAMES.get(
                                        artifact.getGroupId() + ":" + artifact.getArtifactId()))
                        .orElseGet(() -> kebabCaseToCapitalizedWords(artifact.getArtifactId()));
        return Artifact.from(artifact, artifactName);
    }

    private static @NotNull String kebabCaseToCapitalizedWords(final String kebabCase) {
        return Arrays.stream(kebabCase.split("-"))
                .map(StringUtils::capitalize)
                .collect(joining(" "));
    }

    private static final Stream<Artifact> ADDITIONAL_ARTIFACTS =
            Stream.of(
                    new Artifact(
                            "org.springframework.boot",
                            "spring-boot",
                            "Spring Boot",
                            Version.parse(requireNonNull(SpringBootVersion.getVersion()))));

    public InfoResponse getInfo() {
        final var javaVersion = JavaVersion.getJavaVersion();
        final var projectVersion = Version.parse(buildProperties.getVersion());
        return new InfoResponse(projectVersion, javaVersion, artifacts);
    }
}

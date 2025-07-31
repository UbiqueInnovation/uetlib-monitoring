package ch.ubique.backend.monitoring.service;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import ch.ubique.backend.monitoring.model.Artifact;
import ch.ubique.backend.monitoring.model.InfoResponse;

import com.github.zafarkhaja.semver.Version;

import de.westemeyer.version.service.ArtifactVersionCollector;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.system.JavaVersion;
import org.springframework.core.SpringVersion;
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

    private static final Collection<Artifact> ADDITIONAL_ARTIFACTS =
            List.of(
                    new Artifact(
                            "org.springframework.boot",
                            "spring-boot",
                            "Spring Boot",
                            Version.parse(requireNonNull(SpringBootVersion.getVersion()))),
                    new Artifact(
                            "org.springframework",
                            "spring-framework",
                            "Spring Framework",
                            Version.parse(requireNonNull(SpringVersion.getVersion()))));

    private final BuildProperties buildProperties;

    private final List<Artifact> artifacts;

    public InfoService(final BuildProperties buildProperties) {
        this.buildProperties = buildProperties;

        final var retrievedArtifacts =
                ArtifactVersionCollector.collectArtifacts().stream()
                        .map(InfoService::mapArtifact)
                        .sorted(
                                Comparator.comparing(Artifact::groupId)
                                        .thenComparing(Artifact::artifactId));
        artifacts = Stream.concat(retrievedArtifacts, ADDITIONAL_ARTIFACTS.stream()).toList();
    }

    private static @NotNull Artifact mapArtifact(
            final de.westemeyer.version.model.Artifact artifact) {
        final var artifactName = kebabCaseToCapitalizedWords(artifact.getArtifactId());
        return Artifact.from(artifact, artifactName);
    }

    private static @NotNull String kebabCaseToCapitalizedWords(final String kebabCase) {
        return Arrays.stream(kebabCase.split("-"))
                .map(StringUtils::capitalize)
                .collect(joining(" "));
    }

    public InfoResponse getInfo() {
        final var javaVersion = JavaVersion.getJavaVersion();
        final var projectVersion = Version.parse(buildProperties.getVersion());
        return new InfoResponse(projectVersion, javaVersion, artifacts);
    }
}

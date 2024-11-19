package ch.ubique.backend.monitoring.service;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import ch.ubique.backend.monitoring.model.Artifact;
import ch.ubique.backend.monitoring.model.ArtifactToFind;
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

@Service
public class InfoService {

    /**
     * When you add to this set, you <b>must</b> add the {@code artifact-version-maven-plugin} to
     * that artifact's pom or parent pom. Check the usage in the pom: {@see pom.xml}
     */
    private static final Set<ArtifactToFind> ARTIFACTS_TO_RETRIEVE =
            Set.of(
                    new ArtifactToFind("ch.ubique", "uetlib", null, false),
                    new ArtifactToFind(
                            "ch.ubique.pushservice", "ub-pushconnector", "UB Pushconnector", true),
                    new ArtifactToFind(
                            "ch.admin.swisstopo.shared",
                            "swisstopo-backend-shared-service",
                            "swisstopo Backend Shared",
                            true),
                    new ArtifactToFind(
                            "ch.sac.shared",
                            "sac-backend-shared-service",
                            "SAC Backend Shared",
                            true));

    private final BuildProperties buildProperties;

    private List<Artifact> artifacts = List.of();

    public InfoService(final BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @PostConstruct
    void resolveArtifacts() {
        final var retrievedArtifacts =
                ARTIFACTS_TO_RETRIEVE.stream()
                        .flatMap(InfoService::findAndMapArtifacts)
                        .sorted(
                                Comparator.comparing(Artifact::groupId)
                                        .thenComparing(Artifact::artifactId));
        artifacts = Stream.concat(retrievedArtifacts, ADDITIONAL_ARTIFACTS).toList();
    }

    private static @NotNull Stream<Artifact> findAndMapArtifacts(
            final ArtifactToFind artifactToFind) {
        final var artifactsFound = findArtifacts(artifactToFind);
        return artifactsFound.map(
                artifactFound -> {
                    final var artifactName =
                            Optional.ofNullable(artifactToFind.artifactName())
                                    .orElseGet(
                                            () ->
                                                    kebabCaseToCapitalizedWords(
                                                            artifactFound.getArtifactId()));
                    return Artifact.from(artifactFound, artifactName);
                });
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

    private static Stream<de.westemeyer.version.model.Artifact> findArtifacts(
            final ArtifactToFind artifactToFind) {
        final var artifactToFindGroupId = artifactToFind.groupId();
        final var artifactToFindArtifactId = artifactToFind.artifactId();
        final var matchArtifactIdExact = artifactToFind.matchArtifactIdExact();

        // create a new sorted result set
        final var artifacts = new HashSet<de.westemeyer.version.model.Artifact>();

        // add all artifact objects to set of artifacts
        ArtifactVersionCollector.iterateArtifacts(
                artifact -> {
                    final var artifactGroupId = artifact.getGroupId();
                    final var artifactArtifactId = artifact.getArtifactId();

                    // check condition to add artifact to set
                    if (artifactGroupId.equals(artifactToFindGroupId)
                            && (matchArtifactIdExact
                                            && artifactArtifactId.equals(artifactToFindArtifactId)
                                    || !matchArtifactIdExact
                                            && artifactArtifactId.startsWith(
                                                    artifactToFindArtifactId))) {
                        // add artifact
                        artifacts.add(artifact);
                    }

                    // keep adding artifacts
                    return false;
                });

        return artifacts.stream();
    }
}

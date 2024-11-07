package ch.ubique.backend.monitoring.service;

import static java.util.Objects.requireNonNull;

import ch.ubique.backend.monitoring.model.Artifact;
import ch.ubique.backend.monitoring.model.ArtifactToFind;
import ch.ubique.backend.monitoring.model.InfoResponse;

import com.github.zafarkhaja.semver.Version;

import de.westemeyer.version.service.ArtifactVersionCollector;

import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.system.JavaVersion;
import org.springframework.stereotype.Service;

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
                    new ArtifactToFind("ch.ubique", "uetlib-core", "Uetlib"),
                    new ArtifactToFind(
                            "ch.ubique.pushservice", "ub-pushconnector", "UB Pushconnector"),
                    new ArtifactToFind(
                            "ch.admin.swisstopo.shared",
                            "swisstopo-backend-shared-service",
                            "TOPO Backend Shared"),
                    new ArtifactToFind(
                            "ch.sac.shared", "sac-backend-shared-service", "SAC Backend Shared"));

    private static final Stream<Artifact> RETRIEVED_ARTIFACTS =
            ARTIFACTS_TO_RETRIEVE.stream()
                    .map(
                            artifact -> {
                                final var groupId = artifact.groupId();
                                final var artifactId = artifact.artifactId();
                                final var artifactName = artifact.artifactName();
                                return Optional.ofNullable(
                                                ArtifactVersionCollector.findArtifact(
                                                        groupId, artifactId))
                                        .map(a -> Artifact.from(a, artifactName))
                                        .orElse(null);
                            })
                    .filter(Objects::nonNull)
                    .sorted(
                            Comparator.comparing(Artifact::groupId)
                                    .thenComparing(Artifact::artifactId));

    private static final Stream<Artifact> ADDITIONAL_ARTIFACTS =
            Stream.of(
                    new Artifact(
                            "org.springframework.boot",
                            "spring-boot",
                            "Spring Boot",
                            Version.parse(requireNonNull(SpringBootVersion.getVersion()))));

    private static final List<Artifact> ARTIFACTS =
            Stream.concat(RETRIEVED_ARTIFACTS, ADDITIONAL_ARTIFACTS).toList();

    private final BuildProperties buildProperties;

    public InfoService(final BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    public InfoResponse getInfo() {
        final var javaVersion = JavaVersion.getJavaVersion();
        final var projectVersion = Version.parse(buildProperties.getVersion());
        return new InfoResponse(projectVersion, javaVersion, ARTIFACTS);
    }
}

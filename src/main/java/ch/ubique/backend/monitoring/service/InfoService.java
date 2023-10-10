package ch.ubique.backend.monitoring.service;

import ch.ubique.backend.monitoring.model.Artifact;
import ch.ubique.backend.monitoring.model.ArtifactToFind;
import ch.ubique.backend.monitoring.model.InfoResponse;

import com.github.zafarkhaja.semver.Version;

import de.westemeyer.version.service.ArtifactVersionCollector;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InfoService {

    /**
     * When you add to this set, you *must* add the {@code artifact-version-maven-plugin} to that
     * artifact's pom or parent pom. Check the usage in the parent pom: {@see backend-jvm-lib/pom.xml}
     */
    private static final Set<ArtifactToFind> ARTIFACTS_TO_RETRIEVE =
            Set.of(
                    new ArtifactToFind("ch.ubique", "backend-jvm-lib-core", "Backend Lib"),
                    new ArtifactToFind(
                            "ch.admin.swisstopo.shared",
                            "swisstopo-backend-shared-service",
                            "TOPO Backend Shared"),
                    new ArtifactToFind(
                            "ch.sac.shared", "sac-backend-shared-service", "SAC Backend Shared"));

    private static final List<Artifact> ARTIFACTS =
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
                                    .thenComparing(Artifact::artifactId))
                    .toList();

    private final BuildProperties buildProperties;

    public InfoService(final BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    public InfoResponse getInfo() {
        final var mavenVersion = Version.valueOf(buildProperties.getVersion());
        return new InfoResponse(mavenVersion, ARTIFACTS);
    }
}

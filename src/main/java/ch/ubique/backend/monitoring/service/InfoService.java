package ch.ubique.backend.monitoring.service;

import ch.ubique.backend.monitoring.model.Artifact;
import ch.ubique.backend.monitoring.model.InfoResponse;

import com.github.zafarkhaja.semver.Version;

import de.westemeyer.version.service.ArtifactVersionCollector;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InfoService {

    private static final Set<Pair<String, String>> ARTIFACTS_TO_RETRIEVE =
            Set.of(
                    Pair.of("ch.ubique", "backend-jvm-lib-core"),
                    Pair.of("ch.admin.swisstopo.shared", "swisstopo-backend-shared-service"),
                    Pair.of("ch.sac.shared", "sac-backend-shared-service"));

    private static final List<Artifact> ARTIFACTS =
            ARTIFACTS_TO_RETRIEVE.stream()
                    .map(
                            pair -> {
                                final var groupId = pair.getLeft();
                                final var artifactId = pair.getRight();
                                return Optional.ofNullable(
                                                ArtifactVersionCollector.findArtifact(
                                                        groupId, artifactId))
                                        .map(
                                                artifact ->
                                                        new Artifact(
                                                                groupId,
                                                                artifactId,
                                                                Version.valueOf(
                                                                        artifact.getVersion())))
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

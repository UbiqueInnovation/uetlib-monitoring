package ch.ubique.backend.monitoring.service;

import static java.util.Objects.requireNonNull;

import ch.ubique.backend.monitoring.model.Artifact;

import com.github.zafarkhaja.semver.Version;

import de.westemeyer.version.service.ArtifactVersionCollector;

import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Service
public class VersionService {

    public static final Version SPRING_BOOT_VERSION =
            Version.parse(requireNonNull(SpringBootVersion.getVersion()));
    public static final Version SPRING_FRAMEWORK_VERSION =
            Version.parse(requireNonNull(SpringVersion.getVersion()));

    private final Collection<Artifact> collectedArtifacts;

    public VersionService() {
        final var additionalArtifacts =
                List.of(
                        new Artifact(
                                "org.springframework.boot", "spring-boot", SPRING_BOOT_VERSION),
                        new Artifact(
                                "org.springframework",
                                "spring-framework",
                                SPRING_FRAMEWORK_VERSION));
        final var serviceLoaderArtifacts =
                ArtifactVersionCollector.collectArtifacts().stream()
                        .map(Artifact::from)
                        .toList();
        collectedArtifacts =
                Stream.concat(serviceLoaderArtifacts.stream(), additionalArtifacts.stream())
                        .sorted()
                        .toList();
    }


    public Collection<Artifact> collectedArtifacts() {
        return collectedArtifacts;
    }
}

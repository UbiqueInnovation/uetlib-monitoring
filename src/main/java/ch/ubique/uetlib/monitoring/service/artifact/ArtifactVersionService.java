package ch.ubique.uetlib.monitoring.service.artifact;

import static ch.ubique.uetlib.monitoring.utils.VersionUtil.parseVersion;
import static java.util.Objects.requireNonNull;

import ch.ubique.uetlib.monitoring.model.Artifact;
import com.github.zafarkhaja.semver.Version;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Service;

@Service
public class ArtifactVersionService {

    public static final Version SPRING_BOOT_VERSION = parseVersion(SpringBootVersion.getVersion());
    public static final Version SPRING_FRAMEWORK_VERSION =
            parseVersion(requireNonNull(SpringVersion.getVersion()));

    private final Collection<Artifact> additionalArtifacts;
    private final Collection<Artifact> serviceLoaderArtifacts;
    private final Collection<Artifact> springBootArtifacts;

    public ArtifactVersionService(
            @Autowired(required = false)
                    final de.westemeyer.version.service.spring.@Nullable ArtifactVersionCollector
                            springBootArtifactVersionCollector) {

        additionalArtifacts =
                List.of(
                        new Artifact(
                                "org.springframework.boot", "spring-boot", SPRING_BOOT_VERSION),
                        new Artifact(
                                "org.springframework",
                                "spring-framework",
                                SPRING_FRAMEWORK_VERSION));

        serviceLoaderArtifacts =
                de.westemeyer.version.service.ArtifactVersionCollector.collectArtifacts().stream()
                        .map(Artifact::from)
                        .toList();

        if (springBootArtifactVersionCollector != null) {
            springBootArtifacts =
                    springBootArtifactVersionCollector.collect().stream()
                            .map(Artifact::from)
                            .toList();
        } else {
            springBootArtifacts = List.of();
        }
    }

    Collection<Artifact> additionalArtifacts() {
        return additionalArtifacts;
    }

    Collection<Artifact> serviceLoaderArtifacts() {
        return serviceLoaderArtifacts;
    }

    Collection<Artifact> springBootArtifacts() {
        return springBootArtifacts;
    }

    public Collection<Artifact> collectedArtifacts() {
        return Stream.of(additionalArtifacts, serviceLoaderArtifacts, springBootArtifacts)
                .flatMap(Collection::stream)
                .sorted()
                .toList();
    }
}

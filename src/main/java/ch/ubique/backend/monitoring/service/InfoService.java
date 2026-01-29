package ch.ubique.backend.monitoring.service;

import static java.util.Objects.requireNonNull;

import ch.ubique.backend.core.utils.TimeUtils;
import ch.ubique.backend.monitoring.model.Artifact;
import ch.ubique.backend.monitoring.model.InfoResponse;
import ch.ubique.backend.monitoring.model.SystemInfo;

import com.github.zafarkhaja.semver.Version;

import de.westemeyer.version.service.ArtifactVersionCollector;

import kotlin.KotlinVersion;

import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.SpringVersion;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

/**
 * All projects that you want to see the version of <b>must</b> add the {@code
 * artifact-version-maven-plugin} to that artifact's pom or parent pom. Check the usage in the pom:
 * {@see pom.xml}
 */
@Service
public class InfoService {

    private static final Version SPRING_BOOT_VERSION =
            Version.parse(requireNonNull(SpringBootVersion.getVersion()));
    private static final Version SPRING_FRAMEWORK_VERSION =
            Version.parse(requireNonNull(SpringVersion.getVersion()));

    private static final Collection<Artifact> ADDITIONAL_ARTIFACTS =
            List.of(
                    new Artifact("org.springframework.boot", "spring-boot", SPRING_BOOT_VERSION),
                    new Artifact(
                            "org.springframework", "spring-framework", SPRING_FRAMEWORK_VERSION));

    private final BuildProperties buildProperties;

    private final List<Artifact> artifacts;

    public InfoService(final BuildProperties buildProperties) {
        this.buildProperties = buildProperties;

        final var retrievedArtifacts =
                ArtifactVersionCollector.collectArtifacts().stream()
                        .map(Artifact::from)
                        .sorted(
                                Comparator.comparing(Artifact::groupId)
                                        .thenComparing(Artifact::artifactId));
        artifacts = Stream.concat(retrievedArtifacts, ADDITIONAL_ARTIFACTS.stream()).toList();
    }

    public InfoResponse getInfo() {
        final var javaVersion = String.valueOf(Runtime.version().feature());
        final var kotlinVersion = Version.parse(KotlinVersion.CURRENT.toString());
        final var projectVersion = Version.parse(requireNonNull(buildProperties.getVersion()));
        final var whoami =
                new Artifact(
                        buildProperties.getGroup(), buildProperties.getArtifact(), projectVersion);

        final var jvmUptime = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
        final var jvmStartTime = TimeUtils.TIME_UTILS_EUROPE_ZURICH.now().minus(jvmUptime);
        final var systemInfo = new SystemInfo(jvmUptime, jvmStartTime);

        return new InfoResponse(
                projectVersion,
                javaVersion,
                kotlinVersion,
                SPRING_BOOT_VERSION,
                SPRING_FRAMEWORK_VERSION,
                whoami,
                systemInfo,
                artifacts);
    }
}

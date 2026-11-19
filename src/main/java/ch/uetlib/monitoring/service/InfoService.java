package ch.uetlib.monitoring.service;

import static ch.uetlib.monitoring.service.artifact.ArtifactVersionService.SPRING_BOOT_VERSION;
import static ch.uetlib.monitoring.service.artifact.ArtifactVersionService.SPRING_FRAMEWORK_VERSION;
import static ch.uetlib.monitoring.utils.VersionUtil.parseVersion;

import static java.util.Objects.requireNonNull;

import ch.uetlib.core.utils.TimeUtils;
import ch.uetlib.monitoring.model.Artifact;
import ch.uetlib.monitoring.model.InfoResponse;
import ch.uetlib.monitoring.model.SystemInfo;
import ch.uetlib.monitoring.service.artifact.ArtifactVersionService;

import kotlin.KotlinVersion;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.time.Duration;

/**
 * All projects that you want to see the version of <b>must</b> add the {@code
 * artifact-version-maven-plugin} to that artifact's pom or parent pom. Check the usage in the
 * {@code pom.xml}.
 */
@Service
public class InfoService {

    private final ArtifactVersionService artifactVersionService;
    private final BuildProperties buildProperties;

    public InfoService(
            final ArtifactVersionService artifactVersionService,
            final BuildProperties buildProperties) {
        this.artifactVersionService = artifactVersionService;
        this.buildProperties = buildProperties;
    }

    public InfoResponse getInfo() {
        final var javaVersion = Runtime.version().toString();
        final var kotlinVersion = parseVersion(KotlinVersion.CURRENT.toString());
        final var projectVersion = parseVersion(requireNonNull(buildProperties.getVersion()));
        final var whoami =
                new Artifact(
                        requireNonNull(buildProperties.getGroup()),
                        requireNonNull(buildProperties.getArtifact()),
                        projectVersion);

        final var jvmUptime = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
        final var jvmStartTime = TimeUtils.TIME_UTILS_EUROPE_ZURICH.now().minus(jvmUptime);
        final var systemInfo = new SystemInfo(jvmUptime, jvmStartTime);

        final var artifacts = artifactVersionService.collectedArtifacts();

        return new InfoResponse(
                projectVersion,
                javaVersion,
                System.getProperty("java.vendor"),
                kotlinVersion,
                SPRING_BOOT_VERSION,
                SPRING_FRAMEWORK_VERSION,
                whoami,
                systemInfo,
                artifacts);
    }
}

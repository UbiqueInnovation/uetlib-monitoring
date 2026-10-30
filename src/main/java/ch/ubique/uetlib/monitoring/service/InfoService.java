package ch.ubique.uetlib.monitoring.service;

import static ch.ubique.uetlib.monitoring.service.artifact.ArtifactVersionService.SPRING_BOOT_VERSION;
import static ch.ubique.uetlib.monitoring.service.artifact.ArtifactVersionService.SPRING_FRAMEWORK_VERSION;
import static ch.ubique.uetlib.monitoring.utils.VersionUtil.parseVersion;
import static java.util.Objects.requireNonNull;

import ch.ubique.uetlib.core.utils.TimeUtils;
import ch.ubique.uetlib.monitoring.model.Artifact;
import ch.ubique.uetlib.monitoring.model.InfoResponse;
import ch.ubique.uetlib.monitoring.model.SystemInfo;
import ch.ubique.uetlib.monitoring.service.artifact.ArtifactVersionService;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import kotlin.KotlinVersion;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

/**
 * All projects that you want to see the version of <b>must</b> add the {@code
 * artifact-version-maven-plugin} to that artifact's pom or parent pom. Check the usage in the pom:
 * {@see pom.xml}
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
        final var javaVersion = String.valueOf(Runtime.version().feature());
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
                kotlinVersion,
                SPRING_BOOT_VERSION,
                SPRING_FRAMEWORK_VERSION,
                whoami,
                systemInfo,
                artifacts);
    }
}

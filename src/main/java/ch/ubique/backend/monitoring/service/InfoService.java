package ch.ubique.backend.monitoring.service;

import static ch.ubique.backend.monitoring.service.VersionService.SPRING_BOOT_VERSION;
import static ch.ubique.backend.monitoring.service.VersionService.SPRING_FRAMEWORK_VERSION;

import static java.util.Objects.requireNonNull;

import ch.ubique.backend.core.utils.TimeUtils;
import ch.ubique.backend.monitoring.model.InfoResponse;
import ch.ubique.backend.monitoring.model.Artifact;
import ch.ubique.backend.monitoring.model.SystemInfo;

import com.github.zafarkhaja.semver.Version;

import kotlin.KotlinVersion;

import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.time.Duration;

/**
 * All projects that you want to see the version of <b>must</b> add the {@code
 * artifact-version-maven-plugin} to that artifact's pom or parent pom. Check the usage in the pom:
 * {@see pom.xml}
 */
@Service
public class InfoService {

    private final VersionService versionService;
    private final BuildProperties buildProperties;

    public InfoService(final VersionService versionService, final BuildProperties buildProperties) {
        this.versionService = versionService;
        this.buildProperties = buildProperties;
    }

    public InfoResponse getInfo() {
        final var javaVersion = String.valueOf(Runtime.version().feature());
        final var kotlinVersion = Version.parse(KotlinVersion.CURRENT.toString());
        final var projectVersion = Version.parse(requireNonNull(buildProperties.getVersion()));
        final var whoami =
                new Artifact(
                        requireNonNull(buildProperties.getGroup()),
                        requireNonNull(buildProperties.getArtifact()),
                        projectVersion);

        final var jvmUptime = Duration.ofMillis(ManagementFactory.getRuntimeMXBean().getUptime());
        final var jvmStartTime = TimeUtils.TIME_UTILS_EUROPE_ZURICH.now().minus(jvmUptime);
        final var systemInfo = new SystemInfo(jvmUptime, jvmStartTime);

        final var artifacts = versionService.collectedArtifacts();

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

package ch.ubique.backend.monitoring.service.artifact.config;

import de.westemeyer.version.service.spring.ArtifactVersionServiceAutoConfiguration;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * Registers the {@code ch.ubique.versions} version service beans together with the {@link
 * de.westemeyer.version.service.spring.ArtifactVersionCollector} that aggregates them, so that
 * {@link ch.ubique.backend.monitoring.service.artifact.ArtifactVersionService} is wired the same
 * way as in a real application.
 */
@SpringBootConfiguration
@ComponentScan(
        basePackages = {"ch.ubique.versions", "ch.ubique.backend.monitoring.service.artifact"})
@Import(ArtifactVersionServiceAutoConfiguration.class)
public class ArtifactVersionServiceTestConfig {}

package ch.ubique.uetlib.monitoring.service.artifact;

import static ch.ubique.uetlib.monitoring.service.artifact.ArtifactVersionService.SPRING_BOOT_VERSION;
import static ch.ubique.uetlib.monitoring.service.artifact.ArtifactVersionService.SPRING_FRAMEWORK_VERSION;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.ubique.uetlib.monitoring.model.Artifact;
import ch.ubique.uetlib.monitoring.service.artifact.config.ArtifactVersionServiceTestConfig;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest(classes = ArtifactVersionServiceTestConfig.class)
class ArtifactVersionServiceTest {

    @Autowired private ArtifactVersionService artifactVersionService;

    @Test
    void testAdditionalArtifacts() {
        final var expectedArtifacts =
                List.of(
                        new Artifact(
                                "org.springframework.boot", "spring-boot", SPRING_BOOT_VERSION),
                        new Artifact(
                                "org.springframework",
                                "spring-framework",
                                SPRING_FRAMEWORK_VERSION));
        assertEquals(expectedArtifacts, List.copyOf(artifactVersionService.additionalArtifacts()));
    }

    @Test
    void testServiceLoaderArtifacts() {
        final var expectedArtifacts =
                Set.of(
                        Pair.of("de.westemeyer", "artifact-version-service"),
                        Pair.of("ch.ubique.uetlib", "uetlib-core"),
                        Pair.of("ch.ubique.uetlib", "uetlib-spring-boot"),
                        Pair.of("ch.ubique.uetlib", "uetlib-test"));
        assertEquals(expectedArtifacts, toIds(artifactVersionService.serviceLoaderArtifacts()));
    }

    @Test
    void testSpringBootArtifacts() {
        final var expectedArtifacts =
                Set.of(
                        Pair.of("ch.ubique.uetlib", "uetlib-monitoring"),
                        Pair.of("de.westemeyer", "artifact-version-service-spring-boot"));
        assertEquals(expectedArtifacts, toIds(artifactVersionService.springBootArtifacts()));
    }

    @Test
    void testCollectedArtifactsContainAllSubCollectionsSorted() {
        final var collectedArtifacts = List.copyOf(artifactVersionService.collectedArtifacts());

        assertTrue(
                collectedArtifacts.containsAll(artifactVersionService.additionalArtifacts()),
                "collected artifacts are missing additional artifacts");
        assertTrue(
                collectedArtifacts.containsAll(artifactVersionService.serviceLoaderArtifacts()),
                "collected artifacts are missing service loader artifacts");
        assertTrue(
                collectedArtifacts.containsAll(artifactVersionService.springBootArtifacts()),
                "collected artifacts are missing spring boot artifacts");
        assertEquals(
                artifactVersionService.additionalArtifacts().size()
                        + artifactVersionService.serviceLoaderArtifacts().size()
                        + artifactVersionService.springBootArtifacts().size(),
                collectedArtifacts.size());
        assertEquals(
                collectedArtifacts.stream().sorted().toList(),
                collectedArtifacts,
                "collected artifacts are not sorted");
    }

    /**
     * Maps artifacts to their group and artifact id, dropping {@code
     * ch.uetlib:uetlib-monitoring} as this is only included when the tests are run from the
     * cli.
     */
    private static Set<Pair<String, String>> toIds(final Collection<Artifact> artifacts) {
        return artifacts.stream()
                .map(artifact -> Pair.of(artifact.groupId(), artifact.artifactId()))
                .collect(Collectors.toSet());
    }
}

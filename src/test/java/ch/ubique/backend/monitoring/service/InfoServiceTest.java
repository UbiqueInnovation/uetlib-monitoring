package ch.ubique.backend.monitoring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.zafarkhaja.semver.Version;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;

import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

class InfoServiceTest {

    @Test
    void testInfoResponse() {
        final var properties = new Properties();
        properties.setProperty("version", "2.3.1");
        final var infoService = new InfoService(new BuildProperties(properties));
        assertEquals(
                String.valueOf(Runtime.version().feature()),
                infoService.getInfo().javaVersion().toString());
        assertEquals(Version.of(2, 3, 1), infoService.getInfo().projectVersion());
    }

    @Test
    void testResolveArtifacts() {
        final var properties = new Properties();
        properties.setProperty("version", "2.3.1");
        final var infoService = new InfoService(new BuildProperties(properties));

        final var expectedArtifacts =
                Set.of(
                        Pair.of("ch.ubique", "uetlib-core"),
                        Pair.of("ch.ubique", "uetlib-test"),
                        Pair.of("ch.ubique", "uetlib-spring-boot"),
                        Pair.of("org.springframework.boot", "spring-boot"),
                        Pair.of("org.springframework", "spring-framework"));
        final var actualArtifacts =
                infoService.getInfo().artifacts().stream()
                        .map(artifact -> Pair.of(artifact.groupId(), artifact.artifactId()))
                        // exclude ch.ubique:ubique-monitoring as this is only included when
                        // the tests are run from the cli
                        .filter(
                                artifact ->
                                        !Pair.of("ch.ubique", "uetlib-monitoring").equals(artifact))
                        .collect(Collectors.toSet());
        assertEquals(new TreeSet<>(expectedArtifacts), new TreeSet<>(actualArtifacts));
    }
}

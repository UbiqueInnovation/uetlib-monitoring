package ch.ubique.backend.monitoring.service;

import static ch.ubique.backend.monitoring.utils.VersionUtil.parseVersion;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.zafarkhaja.semver.Version;

import kotlin.KotlinVersion;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.info.BuildProperties;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

class InfoServiceTest {

    @Test
    void testInfoResponse() {
        final var infoService = mockInfoService();

        assertEquals(
                String.valueOf(Runtime.version().feature()), infoService.getInfo().javaVersion());
        assertEquals(
                parseVersion(String.valueOf(KotlinVersion.CURRENT)),
                infoService.getInfo().kotlinVersion());
        assertEquals(Version.of(2, 3, 1), infoService.getInfo().projectVersion());
    }

    @Test
    void testResolveArtifacts() {
        final var infoService = mockInfoService();

        final var expectedArtifacts =
                Set.of(
                        Pair.of("de.westemeyer", "artifact-version-service"),
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
                                        !artifact.equals(Pair.of("ch.ubique", "uetlib-monitoring")))
                        .collect(Collectors.toSet());
        assertEquals(expectedArtifacts, actualArtifacts);
    }

    private static InfoService mockInfoService() {
        final var properties = new Properties();
        properties.setProperty("group", "ch.ubique");
        properties.setProperty("artifact", "uetlib-monitoring");
        properties.setProperty("version", "2.3.1");
        return new InfoService(new VersionService(), new BuildProperties(properties));
    }
}
